package au.org.housing.service.impl;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.geotools.data.DefaultQuery;
import org.geotools.data.Query;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.GeoTools;
import org.geotools.feature.FeatureCollections;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.util.WeakCollectionCleaner;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Divide;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import au.org.housing.model.Parameter;
import au.org.housing.service.Config;
import au.org.housing.service.DataStoreFactory;
import au.org.housing.service.DataStoreFactoryBuilder;
import au.org.housing.service.ExportService;
import au.org.housing.service.PropertyFilterService;
import au.org.housing.service.UnionService;
import au.org.housing.service.ValidationService;
import au.org.housing.utilities.GeoJSONUtilities;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Polygon;

@Service
public class PropertyFilterServiceImpl implements PropertyFilterService {

	private static final Logger LOGGER = LoggerFactory.getLogger(PropertyFilterServiceImpl.class);

	@Autowired
	private Parameter parameter;	

	@Autowired
	private ValidationService validationService;

	@Autowired
	private ExportService exportService;

	@Autowired
	private UnionService unionService;
	
	SimpleFeatureSource propertyFc;
	SimpleFeatureSource planCodeListFc;
	SimpleFeatureSource planOverlayFc;
	SimpleFeatureSource zonecodesFc;

	FilterFactory2 ff;
	List<Filter> propertyFilters;
	List<Filter> landUseFilters;
	List<Filter> overlayFilters;
	List<Filter> ownershipFilters;
	private Geometry bufferAllParams = null;
	

	//************************* Check if the layer is Metric  ***************************
	private boolean layersValidation() throws IOException {
//		propertyFc = DataStoreFactoryBuilder.getBuilder("WFSDataStoreFactory").getDataStore(MapAttImpl.property).getFeatureSource(MapAttImpl.property);
		propertyFc = Config.getWFSFactory().getDataStore(MapAttImpl.property).getFeatureSource(MapAttImpl.property);
		planCodeListFc = Config.getDefaultFactory().getDataStore(MapAttImpl.planCodes).getFeatureSource(MapAttImpl.planCodes);
		planOverlayFc = Config.getDefaultFactory().getDataStore(MapAttImpl.planOverlay).getFeatureSource(MapAttImpl.planOverlay);	
		zonecodesFc =  Config.getDefaultFactory().getDataStore(MapAttImpl.zonecodesTbl).getFeatureSource(MapAttImpl.zonecodesTbl);
		if (!validationService.propertyValidated(propertyFc, MapAttImpl.property)  || !validationService.isPolygon(propertyFc, MapAttImpl.property) ||
//				|| !validationService.isMetric(propertyFc)
				!validationService.planOverlayValidated(planOverlayFc, MapAttImpl.planOverlay) || !validationService.isMetric(planOverlayFc, MapAttImpl.planOverlay) || !validationService.isPolygon(planOverlayFc, MapAttImpl.planOverlay) ||
				!validationService.planCodeListValidated(planCodeListFc, MapAttImpl.planCodes) ||
				!validationService.zonecodesValidated(zonecodesFc, MapAttImpl.zonecodesTbl) ){
			return false;
		}
		return true;
	}


	//************************* Property Layer Filter  ***************************
	@SuppressWarnings("deprecation")
	public void createFilters( )
			throws Exception {

		if (!layersValidation() ){
			
		}

		SimpleFeatureType sft = propertyFc.getSchema();
		ff = CommonFactoryFinder.getFilterFactory2(GeoTools.getDefaultHints());
		propertyFilters = new ArrayList<Filter>();
		Filter filter = null;

		// **************** create a new featureType ****************
		SimpleFeatureTypeBuilder stb = new SimpleFeatureTypeBuilder();
		stb.setName("FinalData");
		for (AttributeDescriptor attDisc : sft.getAttributeDescriptors()) {
			String name = attDisc.getLocalName();
			Class type = attDisc.getType().getBinding();  
			if (attDisc instanceof GeometryDescriptor) {
				stb.add( name, Polygon.class );
			}else{
				stb.add(name, type);
			}
		}
		System.out.println("Passed 1111");	
		// ********************************* DPI Filter *********************************
		if (parameter.getDpi() != 0) {
			System.out.println("parameter.getDpi() != 0"); 
			Divide divide = ff.divide(ff.property(MapAttImpl.property_svCurrentYear),
					ff.property(MapAttImpl.property_civCurrentYear));
			List<Filter> match = new ArrayList<Filter>();
			filter = ff.greaterOrEqual(divide, ff.literal(parameter.getDpi()));
			match.add(filter);
			filter = ff.less(divide, ff.literal(parameter.getDpi()+0.1));
			match.add(filter);
			Filter filterDPI = ff.and(match);
			propertyFilters.add(filterDPI);
		}

		// ********************************* Land Use Filter  *********************************
		landUseFilters = new ArrayList<Filter>();
		if (parameter.getResidential()) {
			filter = ff.equals(ff.property(MapAttImpl.zonecodes_group1), ff.literal("RESIDENTIAL"));
			landUseFilters(filter);
		}
		if (parameter.getBusiness()) {
			filter = ff.equals(ff.property(MapAttImpl.zonecodes_group1), ff.literal("BUSINESS"));
			landUseFilters(filter);
		}
		if (parameter.getRural()) {
			filter = ff.equals(ff.property(MapAttImpl.zonecodes_group1), ff.literal("RURAL"));
			landUseFilters(filter);
		}
		if (parameter.getMixedUse()) {
			filter = ff.equals(ff.property(MapAttImpl.zonecodes_group1), ff.literal("MIXED USE"));
			landUseFilters(filter);
		}
		if (parameter.getSpecialPurpose()) {
			filter = ff.equals(ff.property(MapAttImpl.zonecodes_group1),ff.literal("SPECIAL PURPOSE"));
			landUseFilters(filter);
		}
		if (parameter.getUrbanGrowthBoundry()) {
			filter = ff.equals(ff.property(MapAttImpl.zonecodes_group1),ff.literal("URBAN GROWTH BOUNDARY"));
			landUseFilters(filter);
		}
		Filter landUseFilter = null;
		if (!landUseFilters.isEmpty()) {
			landUseFilter = ff.or(landUseFilters);
		}

		// ********************************* Buffer All Parameters Filter *********************************
		if (bufferAllParams != null) {
			LOGGER.info("bufferAllParams is not null");
			if (bufferAllParams.isValid()){
				LOGGER.info("bufferAllParams is Valid");
			}

			GeometryDescriptor gd = propertyFc.getSchema().getGeometryDescriptor();
			String gemoName = gd.getName().toString();

			filter = ff.within(ff.property(gemoName),ff.literal(bufferAllParams));
			propertyFilters.add(filter);

//			filter = ff.equals(ff.property("pfi"),ff.literal("5026012"));//
			filter = ff.equals(ff.property("pfi"),ff.literal("3196232"));//public acuisition
//			filter = ff.equals(ff.property("pfi"),ff.literal("3539423"));//inundation
			propertyFilters.add(filter);


		}
		LOGGER.info("Passed 222");
		//************ Ownership  --  Public Acquisition ************		
		ownershipFilters= new ArrayList<Filter>();
		Geometry publicAcquisitionsUnion;
		SimpleFeatureCollection publicAcquisitions = null;
		if (parameter.getPublicAcquision()) {
			System.out.println("------- PUBLIC ACQUISION true");	 
			filter = ff.equals(ff.property(MapAttImpl.planCodes_group1),ff.literal("PUBLIC ACQUISION OVERLAY"));
			publicAcquisitions = overayFilters(filter);			
			System.out.println("publicAcquisitions size==="+publicAcquisitions.size());				
			publicAcquisitionsUnion = (Geometry) unionService.createUnion(publicAcquisitions);			
			GeometryDescriptor gd = propertyFc.getSchema().getGeometryDescriptor();
			String gemoName = gd.getName().toString();
			filter = ff.not(ff.intersects(ff.property(gemoName),ff.literal(publicAcquisitionsUnion)));
			ownershipFilters.add(filter);	
		}
		//************ Ownership  --  CommonWealth ************
		Geometry commonWealthsUnion;
		SimpleFeatureCollection commonWealths = null;
		if ( parameter.getCommonwealth() ) {
			System.out.println("------- Commonwealth true");	 
			filter = ff.equals(ff.property(MapAttImpl.planCodes_group1),ff.literal("COMMONWEALTH LAND"));
			commonWealths = overayFilters(filter);			
			System.out.println("commonWealths size==="+commonWealths.size());
			commonWealthsUnion = (Geometry) unionService.createUnion(commonWealths);
			GeometryDescriptor gd = propertyFc.getSchema().getGeometryDescriptor();
			String gemoName = gd.getName().toString();
			filter = ff.not(ff.intersects(ff.property(gemoName),ff.literal(commonWealthsUnion)));	
			
		}
		Filter ownershipFilter = null;
		if (!ownershipFilters.isEmpty()) {
			ownershipFilter = ff.and(ownershipFilters);
		}

		// ********************************* Merge all the Filters *********************************
		Filter propertyFilter = null;
		SimpleFeatureCollection properties = null;

		if (!propertyFilters.isEmpty() ) {
			if (landUseFilter != null){
				propertyFilters.add(landUseFilter);				
			}
			if (ownershipFilter != null){
				propertyFilters.add(ownershipFilter);				
			}
			propertyFilter = ff.and(propertyFilters);
		}
		if (propertyFilter != null) {
			LOGGER.info("Passed 3333");
			LOGGER.info("propertyFilter.toString()"+propertyFilter.toString()); 

			Query propertyQuery = new Query();
			propertyQuery.setFilter(propertyFilter);
				 
			//			propertyQuery.setMaxFeatures(1000); 

			properties = propertyFc.getFeatures(propertyQuery);

			LOGGER.info("properties.size()g"+properties.size());
			
		} else {
			System.out.println("propertyFilter == null");  
			DefaultQuery query = new DefaultQuery(propertyFc.getSchema().getTypeName());
			Query propertyQuery = new Query();
//			propertyQuery.setMaxFeatures(100);
			// propertyQuery.setPropertyNames(new String[] { "zoning",
			// "lga_code",propertyFc.getSchema().getGeometryDescriptor().getName().getLocalPart()
			// });
			List<Filter> fs = new ArrayList<Filter>();
			//			filter = ff.equals(ff.property("pfi"),ff.literal("4788308"));//floodway
			//			fs.add(filter);
			//			filter = ff.equals(ff.property("pfi"),ff.literal("1317010"));
			//			fs.add(filter);
//						filter = ff.equals(ff.property("pfi"),ff.literal("4787005"));//inundation
//						fs.add(filter);
						filter = ff.equals(ff.property("pfi"),ff.literal("205416131"));//inundation
						fs.add(filter);
			////			filter = ff.equals(ff.property("pfi"),ff.literal("218965278"));//inundation
			////			fs.add(filter);
			////			filter = ff.equals(ff.property("pfi"),ff.literal("2939395"));//inundation
			////			fs.add(filter);
//			filter = ff.equals(ff.property("PFI"),ff.literal("3539423"));//inundation
//			fs.add(filter);
//						filter = ff.equals(ff.property("pfi"),ff.literal("1170714"));
//						fs.add(filter);
			//			filter = ff.or(fs);
			propertyQuery.setFilter(filter);
			properties = propertyFc.getFeatures(propertyQuery);
			System.out.println("properties size()"+properties.size());
			LOGGER.trace("properties size()"+properties.size());
		}
		LOGGER.info("System.currentTimeMillis()"+System.currentTimeMillis());


		if (properties != null && !properties.isEmpty()){
			// ********************************* overlay Filter	 *********************************
			overlayFilters = new ArrayList<Filter>();
			SimpleFeatureCollection featureCollectionNew = FeatureCollections.newCollection();			

			//************ FLOODWAY OVERLAY ************	
			Geometry floodwaysUnion = null;
			if (parameter.getFloodway()) {			 
				filter = ff.equals(ff.property(MapAttImpl.planCodes_group1),ff.literal("FLOODWAY OVERLAY"));
				SimpleFeatureCollection floodways = overayFilters(filter);
				if ( floodways != null ){
					floodwaysUnion = (Geometry) unionService.createUnion(floodways);
					System.out.println("floodway size==="+floodways.size());
				}
				stb.add("OL_Floodway", Boolean.class); 
			}
			//************ LAND SUBJECT TO INUNDATION OVERLAY ************
			Geometry inundationsUnion = null; //????
			if (parameter.getInundation()) {		 
				filter = ff.equals(ff.property(MapAttImpl.planCodes_group1),ff.literal("LAND SUBJECT TO INUNDATION OVERLAY"));
				SimpleFeatureCollection inundations = overayFilters(filter);
				if ( inundations != null ){
					inundationsUnion = unionService.createUnion(inundations) ; 
					System.out.println("inundations size==="+inundations.size());
				}
				stb.add("OL_Inundation", Boolean.class); 
			}
			//************ NEIGHBOURHOOD CHARACTER OVERLAY ************
			Geometry neighborhoodsUnion = null;		
			if (parameter.getNeighborhood()) {		 
				filter = ff.equals(ff.property(MapAttImpl.planCodes_group1),ff.literal("NEIGHBOURHOOD CHARACTER OVERLAY"));
				SimpleFeatureCollection neighborhoods = overayFilters(filter);
				if ( neighborhoods != null ){
					neighborhoodsUnion = (Geometry) unionService.createUnion(neighborhoods);
					System.out.println("neighborhoods size==="+neighborhoods.size());
				}
				stb.add("OL_Neighborhood", Boolean.class); 
			}

			//************ DESIGN AND DEVELOPMENT OVERLAY ************
			Geometry designDevelopmentsUnion = null;		
			if (parameter.getDesignDevelopment()) {			 
				filter = ff.equals(ff.property(MapAttImpl.planCodes_group1),ff.literal("DESIGN AND DEVELOPMENT OVERLAY"));
				SimpleFeatureCollection designDevelopments = overayFilters(filter);
				if ( designDevelopments != null ){
					designDevelopmentsUnion = (Geometry) unionService.createUnion(designDevelopments);
					System.out.println("designDevelopments size==="+designDevelopments.size());	
				}
				stb.add("OL_DesignDevelopment", Boolean.class); 
			}
			//************ DESIGN AND DEVELOPMENT OVERLAY ************
			Geometry developPlansUnion = null;		
			if (parameter.getDevelopPlan()) {		 
				filter = ff.equals(ff.property(MapAttImpl.planCodes_group1),ff.literal("DEVELOPMENT PLAN OVERLAY"));
				SimpleFeatureCollection developPlans = overayFilters(filter);
				if ( developPlans != null ){
					developPlansUnion = (Geometry) unionService.createUnion(developPlans);
					System.out.println("developPlans size==="+developPlans.size());	
				}
				stb.add("OL_DevelopmentPlan", Boolean.class); 
			}
			// ************ PARKING OVERLAY ************
			Geometry parkingsUnion = null;		
			if (parameter.getParking() ) {		 
				filter = ff.equals(ff.property(MapAttImpl.planCodes_group1),ff.literal("PARKING OVERLAY"));
				SimpleFeatureCollection parkings = overayFilters(filter);
				if ( parkings != null ){
					parkingsUnion = (Geometry) unionService.createUnion(parkings);
					System.out.println("parkings size==="+parkings.size());	
				}
				stb.add("OL_Parking", Boolean.class);
			}
			// ************ BUSHFIRE MANAGEMENT OVERLAY ************
			Geometry bushfiresUnion = null;	
			if (parameter.getBushfire() ) {	 
				filter = ff.equals(ff.property(MapAttImpl.planCodes_group1),ff.literal("BUSHFIRE MANAGEMENT OVERLAY"));
				SimpleFeatureCollection bushfires = overayFilters(filter);
				if ( bushfires != null ){
					bushfiresUnion = (Geometry) unionService.createUnion(bushfires);
					System.out.println("bushfires size==="+bushfires.size());
				}
				stb.add("OL_Bushfire", Boolean.class);
			}
			// ************ EROSION MANAGEMENT OVERLAY ************
			Geometry erosionsUnion = null;	
			if (parameter.getErosion()) {	 
				filter = ff.equals(ff.property(MapAttImpl.planCodes_group1),ff.literal("EROSION MANAGEMENT OVERLAY"));
				SimpleFeatureCollection erosions = overayFilters(filter);
				if ( erosions != null ){
					erosionsUnion = (Geometry) unionService.createUnion(erosions);
					System.out.println("erosions size==="+erosions.size());	
				}
				stb.add("OL_Erosion", Boolean.class);
			}
			// ************ VEGETATION PROTECTION OVERLAY ************
			Geometry vegprotectionsUnion = null;	
			if (parameter.getVegprotection()) {
				filter = ff.equals(ff.property(MapAttImpl.planCodes_group1),ff.literal("VEGETATION PROTECTION OVERLAY"));
				SimpleFeatureCollection vegprotections = overayFilters(filter);
				if ( vegprotections != null ){
					vegprotectionsUnion = (Geometry) unionService.createUnion(vegprotections);
					System.out.println("vegprotections size==="+vegprotections.size());	
				}
				stb.add("OL_VegProtection", Boolean.class);
			}
			// ************ SALINITY MANAGEMENT OVERLAY ************
			Geometry salinitysUnion = null;
			if (parameter.getSalinity()) {	 
				filter = ff.equals(ff.property(MapAttImpl.planCodes_group1),ff.literal("SALINITY MANAGEMENT OVERLAY"));
				SimpleFeatureCollection salinitys = overayFilters(filter);
				if ( salinitys != null ){
					salinitysUnion = (Geometry) unionService.createUnion(salinitys);
					System.out.println("salinitys size==="+salinitys.size());	
				}
				stb.add("OL_Salinity", Boolean.class);
			}
			// ************ CONTAMINATION MANAGEMENT OVERLAY ************
			Geometry contaminationsUnion = null;
			if (parameter.getContamination()) {
				filter = ff.equals(ff.property(MapAttImpl.planCodes_group1),ff.literal("POTENTIALLY CONTAMINATED LAND OVERLAY"));
				SimpleFeatureCollection contaminations = overayFilters(filter);
				if ( contaminations != null ){
					contaminationsUnion = (Geometry) unionService.createUnion(contaminations);
					System.out.println("contaminations size==="+contaminations.size());	
				}
				stb.add("OL_Contamination", Boolean.class);
			}
			// ************ ENVIRONMENTAL SIGNIFICANCE OVERLAY ************
			Geometry envSignificancesUnion = null;
			if (parameter.getEnvSignificance()) {
				filter = ff.equals(ff.property(MapAttImpl.planCodes_group1),ff.literal("ENVIRONMENTAL SIGNIFICANCE OVERLAY"));
				SimpleFeatureCollection envSignificances = overayFilters(filter);
				if ( envSignificances != null ){
					envSignificancesUnion = (Geometry) unionService.createUnion(envSignificances);
					System.out.println("envSignificances size==="+envSignificances.size());	
				}
				stb.add("OL_EnvSignificance", Boolean.class);
			}
			// ************ ENVIRONMENTAL AUDIT OVERLAY ************
			Geometry envAuditsUnion = null;
			if (parameter.getEnvAudit()) {
				filter = ff.equals(ff.property(MapAttImpl.planCodes_group1),ff.literal("ENVIRONMENTAL AUDIT OVERLAY"));//???
				SimpleFeatureCollection envAudits = overayFilters(filter);
				if ( envAudits != null ){
					envAuditsUnion = (Geometry) unionService.createUnion(envAudits);
					System.out.println("envAudits size==="+envAudits.size());	
				}
				stb.add("OL_EnvAudit", Boolean.class);
			}
			// ************ HERITAGE OVERLAY ************		
			Geometry heritageUnion = null;
			if (parameter.getHeritage()) {
				filter = ff.equals(ff.property(MapAttImpl.planCodes_group1),ff.literal("HERITAGE OVERLAY"));// ???
				SimpleFeatureCollection heritages = overayFilters(filter);
				if (heritages != null){
					heritageUnion = (Geometry) unionService.createUnion(heritages);
					System.out.println("heritages size==="+heritages.size());
				}
				stb.add("OL_Heritage", Boolean.class);
			}


			// ****************************** Create Feature Builder *****************************
			SimpleFeatureType newFeatureType = stb.buildFeatureType();
			SimpleFeatureBuilder sfb = new SimpleFeatureBuilder(newFeatureType);

			// **************** Loop through Property Layer Features ****************
			boolean dropCreateSchema = true;
			List<SimpleFeature> newList = new ArrayList<SimpleFeature>();
			SimpleFeatureIterator propertyIt = properties.features();
			try {
				int i = 0;
				while (propertyIt.hasNext()) {
					SimpleFeature sf = propertyIt.next();
					sfb.addAll(sf.getAttributes());
					Geometry propertyGeom = (Geometry) sf.getDefaultGeometry();

					// **************** Intersections ****************

					if (floodwaysUnion != null) {
						if (floodwaysUnion.intersects(propertyGeom)) {
							sfb.set("OL_Floodway", Boolean.TRUE);
							System.out.print("  ,   intersect with OL_Floodway  == ");						
						}									
					}				
					if (inundationsUnion != null) {
						System.out.print("  ,  inundationsUnion != null == ");						
						if (inundationsUnion.intersects(propertyGeom)) {
							sfb.set("OL_Inundation", Boolean.TRUE);
							System.out.print("  ,   intersect with OL_Inundation  == ");						
						}									
					}				
					if (neighborhoodsUnion != null) {
						if (neighborhoodsUnion.intersects(propertyGeom)) {
							sfb.set("OL_Neighborhood", Boolean.TRUE);
							System.out.print("  ,   intersect with OL_Neighborhood  == ");						
						}									
					}				
					if (designDevelopmentsUnion != null) {
						if (designDevelopmentsUnion.intersects(propertyGeom)) {
							sfb.set("OL_DesignDevelopment", Boolean.TRUE);
							System.out.print("  ,   intersect with OL_DesignDevelopment  == ");						
						}									
					}				
					if (developPlansUnion != null) {
						if (developPlansUnion.intersects(propertyGeom)) {
							sfb.set("OL_DevelopmentPlan", Boolean.TRUE);
							System.out.print("  ,   intersect with OL_DevelopmentPlan  == ");						
						}									
					}				
					if (parkingsUnion != null) {
						if (parkingsUnion.intersects(propertyGeom)) {
							sfb.set("OL_Parking", Boolean.TRUE);
							System.out.print("  ,   intersect with OL_Parking  == ");						
						}									
					}					
					if (bushfiresUnion != null) {
						if (bushfiresUnion.intersects(propertyGeom)) {
							sfb.set("OL_Bushfire", Boolean.TRUE);
							System.out.print("  ,   intersect with OL_Bushfire  == ");						
						}									
					}				
					if (erosionsUnion != null) {
						if (erosionsUnion.intersects(propertyGeom)) {
							sfb.set("OL_Erosion", Boolean.TRUE);
							System.out.print("  ,   intersect with OL_Erosion  == ");						
						}									
					}				
					if (vegprotectionsUnion != null) {
						if (vegprotectionsUnion.intersects(propertyGeom)) {
							sfb.set("OL_VegProtection", Boolean.TRUE);
							System.out.print("  ,   intersect with OL_VegProtection  == ");						
						}									
					}				
					if (salinitysUnion != null) {
						if (salinitysUnion.intersects(propertyGeom)) {
							sfb.set("OL_Salinity", Boolean.TRUE);
							System.out.print("  ,   intersect with OL_Salinity  == ");						
						}									
					}				
					if (contaminationsUnion != null) {
						if (contaminationsUnion.intersects(propertyGeom)) {
							sfb.set("OL_Contamination", Boolean.TRUE);
							System.out.print("  ,   intersect with OL_Contamination  == ");						
						}									
					}				
					if (envSignificancesUnion != null) {
						if (envSignificancesUnion.intersects(propertyGeom)) {
							sfb.set("OL_EnvSignificance", Boolean.TRUE);
							System.out.print("  ,   intersect with OL_EnvSignificance  == ");						
						}									
					}				
					if (envAuditsUnion != null) {
						if (envAuditsUnion.intersects(propertyGeom)) {
							sfb.set("OL_EnvAudit", Boolean.TRUE);
							System.out.print("  ,   intersect with OL_EnvAudit  == ");						
						}									
					}
					if (heritageUnion != null) {
						if (heritageUnion.intersects(propertyGeom)) {
							sfb.set("OL_Heritage", Boolean.TRUE);
							System.out.print("  ,   intersect with OL_Heritage  == ");

						}									
					}
					// **************** Intersections End ****************

					SimpleFeature newFeature = sfb.buildFeature(null);
					newList.add(newFeature);

					i++;
					if (i == 10000) {
						System.out.println("properties.size() < 10000");

						featureCollectionNew = new ListFeatureCollection(
								newFeatureType, newList);
						exportService.featuresExportToPostGis(newFeatureType,
								featureCollectionNew, dropCreateSchema,   Config.getDefaultFactory().getExportableDataStore());
						
						URL url = this.getClass().getResource("/geoJSON");
						File parentDirectory = new File(new URI(url.toString()));
						File file = new File(parentDirectory, "Housing_FinalData.geojson");
						GeoJSONUtilities.writeFeatures(featureCollectionNew, file);
						// File newFile = new
						// File("C:/Programming/Projects/Data/Final.shp");
						// exportToShpService.featuresExportToShapeFile(
						// sfb.getFeatureType(), featureCollectionNew,
						// newFile, createSchema);
						newList = new ArrayList<SimpleFeature>();
						i = 0;
						dropCreateSchema = false;
					}
				}
			} finally {
				propertyIt.close();
			}

			if (!newList.isEmpty()){
				System.out.println("properties.size() < 10000");
				featureCollectionNew = new ListFeatureCollection(
						newFeatureType, newList);
				exportService.featuresExportToPostGis(newFeatureType,
						featureCollectionNew, dropCreateSchema,  Config.getDefaultFactory().getExportableDataStore());
			}

			System.out.println(new Date());
			System.out.println("------------ featureCollectionNew.size() == "+ featureCollectionNew.size());
			
		}
		WeakCollectionCleaner.currentThread().run();
		
		//		return properties;
	}

	// **************** Land Use ****************
	private void landUseFilters(Filter filter) throws IOException {

		Query zoneCodeQuery = new Query();
		zoneCodeQuery.setPropertyNames(new String[] { MapAttImpl.zonecodes_zoneCode, MapAttImpl.zonecodes_group1 });
		zoneCodeQuery.setFilter(filter);
		SimpleFeatureCollection zoneCodes = zonecodesFc.getFeatures(zoneCodeQuery);
		SimpleFeatureIterator it = zoneCodes.features();
		List<Filter> match = new ArrayList<Filter>();
		while (it.hasNext()) {
			SimpleFeature zoneCode = it.next();
			Object value = zoneCode.getAttribute(MapAttImpl.zonecodes_zoneCode);
			filter = ff.equals(ff.property(MapAttImpl.property_zoning), ff.literal(value));
			match.add(filter);
		}
		it.close();
		Filter filterRES = ff.or(match);
		landUseFilters.add(filterRES);
	}

	// **************** Overlay ****************
	private SimpleFeatureCollection overayFilters(Filter filter) throws IOException {
		Query codeListQuery = new Query();
		codeListQuery.setPropertyNames(new String[] { MapAttImpl.planCodes_zoneCode, MapAttImpl.planCodes_group1 });
		codeListQuery.setFilter(filter);
		SimpleFeatureCollection codeList = planCodeListFc.getFeatures(codeListQuery);

		System.out.println("codeList.size()===  "+codeList.size()); 

		SimpleFeatureIterator it = codeList.features();
		List<Filter> match = new ArrayList<Filter>();
		while (it.hasNext()) {
			SimpleFeature zoneCode = it.next();
			Object value = zoneCode.getAttribute(MapAttImpl.planOverlay_zoneCode);
			filter = ff.equals(ff.property(MapAttImpl.planOverlay_zoneCode), ff.literal(value));
			match.add(filter);
		}
		it.close();

		Filter filterOverlay = ff.or(match);
		Query overlayQuery = new Query();
		overlayQuery.setFilter(filterOverlay);
		SimpleFeatureCollection overlays = planOverlayFc.getFeatures(overlayQuery);	
		
		System.out.println("codeoverlaysList.size()===  "+ overlays.size());

		return overlays;
	}

	public Parameter getParameter() {
		return parameter;
	}

	public void setParameter(Parameter parameter) {
		this.parameter = parameter;
	}

	public Geometry getBufferAllParams() {
		return bufferAllParams;
	}

	public void setBufferAllParams(Geometry bufferAllParams) {
		this.bufferAllParams = bufferAllParams;
	}




}

// for ( AttributeDescriptor at : sft.getAttributeDescriptors() ) {
// String name = at.getLocalName();
// Class type = at.getType().getBinding();
//
// if ( at instanceof GeometryDescriptor ) {
// type = Polygon.class;
// }
//
// stb.add(name,type);
// }


//for (Object val : sf.getAttributes()){
//	if (val instanceof Geometry){
//		sfb.add(sf.getDefaultGeometry());
//	}else{
//		sfb.add(val);
//	}
//}	


//boolean isMulitPolygon = Geometries.get(overlayGeom) == Geometries.MULTIPOLYGON;
