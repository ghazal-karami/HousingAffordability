package au.org.housing.service.impl;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.geotools.data.DataStore;
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
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.util.WeakCollectionCleaner;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Divide;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import au.org.housing.model.Parameter;
import au.org.housing.service.ExportToShpService;
import au.org.housing.service.PropertyFilterService;
import au.org.housing.service.TransportationBufferService;
import au.org.housing.service.UnionService;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Polygon;

@Service
public class PropertyFilterServiceImpl implements PropertyFilterService {

	private static final Logger LOGGER = LoggerFactory.getLogger(PropertyFilterServiceImpl.class);

	SimpleFeatureSource propertyFc;
	FilterFactory2 ff;
	List<Filter> propertyFilters;
	List<Filter> landUseFilters;
	List<Filter> overlayFilters;
	List<Filter> ownershipFilters;
	DataStore dataStore;

	@Autowired
	private Parameter parameter;

	@Autowired
	private UnionService unionService;

	@Autowired
	private ExportToShpService exportToShpService;
	
	@Autowired
	private TransportationBufferService transportationBufferService;

	private Geometry bufferAllParams = null;

	@SuppressWarnings("deprecation")
	public SimpleFeatureCollection createFilters(DataStore dataStore)
			throws IOException, NoSuchAuthorityCodeException, FactoryException,
			CQLException, URISyntaxException {

		this.dataStore = dataStore;

		ff = CommonFactoryFinder.getFilterFactory2(GeoTools.getDefaultHints());
		propertyFc = dataStore.getFeatureSource("property");

		SimpleFeatureType sft = propertyFc.getSchema();
		propertyFilters = new ArrayList<Filter>();
		Filter filter = null;
		
		// **************** create a new featureType ****************
		SimpleFeatureTypeBuilder stb = new SimpleFeatureTypeBuilder();
		stb.setName("FinalData");
		for (AttributeDescriptor attDisc : sft.getAttributeDescriptors()) {
			String name = attDisc.getLocalName();
			Class type = attDisc.getType().getBinding();  
			if (attDisc instanceof GeometryDescriptor) {
				System.out.println("-------"+((GeometryDescriptor) attDisc).getCoordinateReferenceSystem());
				stb.add( name, Polygon.class );
			}else{
				stb.add(name, type);
			}
		}

		// ********************************* DPI Filter *********************************
		if (parameter.getDpi() != 0) {
			System.out.println("parameter.getDpi() != 0"); 
			Divide divide = ff.divide(ff.property("sv_current_year"),ff.property("civ_current_year"));
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
			filter = ff.equals(ff.property("group1"), ff.literal("RESIDENTIAL"));
			landUseFilters(filter);
		}
		if (parameter.getBusiness()) {
			filter = ff.equals(ff.property("group1"), ff.literal("BUSINESS"));
			landUseFilters(filter);
		}
		if (parameter.getRural()) {
			filter = ff.equals(ff.property("group1"), ff.literal("RURAL"));
			landUseFilters(filter);
		}
		if (parameter.getMixedUse()) {
			filter = ff.equals(ff.property("group1"), ff.literal("MIXED USE"));
			landUseFilters(filter);
		}
		if (parameter.getSpecialPurpose()) {
			filter = ff.equals(ff.property("group1"),ff.literal("SPECIAL PURPOSE"));
			landUseFilters(filter);
		}
		if (parameter.getUrbanGrowthBoundry()) {
			filter = ff.equals(ff.property("group1"),ff.literal("URBAN GROWTH BOUNDARY"));
			landUseFilters(filter);
		}
		Filter landUseFilter = null;
		if (!landUseFilters.isEmpty()) {
			landUseFilter = ff.or(landUseFilters);
		}

		// ********************************* Buffer All Parameters Filter *********************************
		if (bufferAllParams != null) {
			GeometryDescriptor gd = propertyFc.getSchema().getGeometryDescriptor();
			String gemoName = gd.getName().toString();
			filter = ff.within(ff.property(gemoName),ff.literal(bufferAllParams));
			propertyFilters.add(filter);
		}
		
		//************ Ownership  --  Public Acquisition ************		
		ownershipFilters= new ArrayList<Filter>();
		
		SimpleFeatureCollection publicAcquisitions = null;
		if (parameter.getPublicAcquision()) {
			System.out.println("------- PUBLIC ACQUISION true");	 
			filter = ff.equals(ff.property("group1"),ff.literal("PUBLIC ACQUISION OVERLAY"));
			publicAcquisitions = overayFilters(filter);			
			System.out.println("publicAcquisitions size==="+publicAcquisitions.size());
			SimpleFeatureIterator overlayIt = publicAcquisitions.features();
			while (overlayIt.hasNext()) {
				SimpleFeature overlayFt = overlayIt.next();	
				filter = ff.disjoint(ff.property(overlayFt.getDefaultGeometryProperty().getName()),ff.literal(overlayFt.getDefaultGeometry()));
				ownershipFilters.add(filter);			
			}
		}
		//************ Ownership  --  CommonWealth ************
		
		SimpleFeatureCollection commonWealths = null;
		if ( parameter.getCommonwealth() ) {
			System.out.println("------- Commonwealth true");	 
			filter = ff.equals(ff.property("group1"),ff.literal("COMMONWEALTH LAND"));
			commonWealths = overayFilters(filter);			
			System.out.println("commonWealths size==="+commonWealths.size());
			SimpleFeatureIterator overlayIt = commonWealths.features();
			while (overlayIt.hasNext()) {
				SimpleFeature overlayFt = overlayIt.next();	
				filter = ff.disjoint(ff.property(overlayFt.getDefaultGeometryProperty().getName()),ff.literal(overlayFt.getDefaultGeometry()));
				ownershipFilters.add(filter);			
			}
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
			Query propertyQuery = new Query();
			propertyQuery.setFilter(propertyFilter);
			properties = propertyFc.getFeatures(propertyQuery);
		} else {
			System.out.println("null null null");  
			DefaultQuery query = new DefaultQuery(propertyFc.getSchema().getTypeName());
			Query propertyQuery = new Query();
			// propertyQuery.setPropertyNames(new String[] { "zoning",
			// "lga_code",propertyFc.getSchema().getGeometryDescriptor().getName().getLocalPart()
			// });
//			propertyQuery.setMaxFeatures(12000); 
//			List<Filter> fs = new ArrayList<Filter>();
//			filter = ff.equals(ff.property("pfi"),ff.literal("4788308"));//floodway
//			fs.add(filter);
//			filter = ff.equals(ff.property("pfi"),ff.literal("1317010"));
//			fs.add(filter);
//			filter = ff.equals(ff.property("pfi"),ff.literal("4787005"));//inundation
//			fs.add(filter);
////			filter = ff.equals(ff.property("pfi"),ff.literal("218965278"));//inundation
////			fs.add(filter);
////			filter = ff.equals(ff.property("pfi"),ff.literal("2939395"));//inundation
////			fs.add(filter);
//			filter = ff.equals(ff.property("pfi"),ff.literal("3539423"));//inundation
//			fs.add(filter);
//			filter = ff.equals(ff.property("pfi"),ff.literal("1170714"));
//			fs.add(filter);
//			filter = ff.or(fs);
//			propertyQuery.setFilter(filter);
			properties = propertyFc.getFeatures(propertyQuery);
		}
		System.out.println("properties == " + properties.size());
		

		// ********************************* overlay Filter	 *********************************
		overlayFilters = new ArrayList<Filter>();
		SimpleFeatureCollection featureCollectionNew = FeatureCollections.newCollection();
		System.out.println(new Date());

		
		//************ FLOODWAY OVERLAY ************		
		SimpleFeatureCollection floodways = null;
		if (parameter.getFloodway()) {
			System.out.println("------- Floodway true");			 
			filter = ff.equals(ff.property("group1"),ff.literal("FLOODWAY OVERLAY"));
			floodways = overayFilters(filter);			
			System.out.println("floodway size==="+floodways.size());
			stb.add("OL_Floodway", Boolean.class); 
		}
		//************ LAND SUBJECT TO INUNDATION OVERLAY ************
		SimpleFeatureCollection inundations = null;
		if (parameter.getInundation()) {
			System.out.println("------- Inundation true");			 
			filter = ff.equals(ff.property("group1"),ff.literal("LAND SUBJECT TO INUNDATION OVERLAY"));
			inundations = overayFilters(filter);	
			System.out.println("floodway size==="+inundations.size());	
			stb.add("OL_Inundation", Boolean.class); 
		}
		//************ NEIGHBOURHOOD CHARACTER OVERLAY ************
		SimpleFeatureCollection neighborhoods = null;
		if (parameter.getNeighborhood()) {
			System.out.println("------- Neighborhood true");			 
			filter = ff.equals(ff.property("group1"),ff.literal("NEIGHBOURHOOD CHARACTER OVERLAY"));
			neighborhoods = overayFilters(filter);
			System.out.println("neighborhoods size==="+neighborhoods.size());	
			stb.add("OL_Neighborhood", Boolean.class); 
		}
		
		//************ DESIGN AND DEVELOPMENT OVERLAY ************
		SimpleFeatureCollection designDevelopments = null;
		if (parameter.getDesignDevelopment()) {
			System.out.println("------- DesignDevelopment true");			 
			filter = ff.equals(ff.property("group1"),ff.literal("DESIGN AND DEVELOPMENT OVERLAY"));
			designDevelopments = overayFilters(filter);		
			System.out.println("designDevelopments size==="+designDevelopments.size());	
			stb.add("OL_DesignDevelopment", Boolean.class); 
		}
		//************ DESIGN AND DEVELOPMENT OVERLAY ************
		SimpleFeatureCollection developPlans = null;
		if (parameter.getDevelopPlan()) {
			System.out.println("------- DevelopPlan true");			 
			filter = ff.equals(ff.property("group1"),ff.literal("DEVELOPMENT PLAN OVERLAY"));
			developPlans = overayFilters(filter);
			System.out.println("developPlans size==="+developPlans.size());	
			stb.add("OL_DevelopmentPlan", Boolean.class); 
		}
		// ************ PARKING OVERLAY ************
		SimpleFeatureCollection parkings = null;
		if (parameter.getParking() ) {
			System.out.println("------- getParking true");			 
			filter = ff.equals(ff.property("group1"),ff.literal("PARKING OVERLAY"));
			parkings = overayFilters(filter);
			System.out.println("parkings size==="+parkings.size());	
			stb.add("OL_Parking", Boolean.class);
		}
		// ************ BUSHFIRE MANAGEMENT OVERLAY ************
		SimpleFeatureCollection bushfires = null;
		if (parameter.getBushfire() ) {
			System.out.println("------- getBushfire true");			 
			filter = ff.equals(ff.property("group1"),ff.literal("BUSHFIRE MANAGEMENT OVERLAY"));
			bushfires = overayFilters(filter);
			System.out.println("bushfires size==="+bushfires.size());	
			stb.add("OL_Bushfire", Boolean.class);
		}
		// ************ EROSION MANAGEMENT OVERLAY ************
		SimpleFeatureCollection erosions = null;
		if (parameter.getErosion()) {
			System.out.println("------- getErosion true");			 
			filter = ff.equals(ff.property("group1"),ff.literal("EROSION MANAGEMENT OVERLAY"));
			erosions = overayFilters(filter);
			System.out.println("erosions size==="+erosions.size());	
			stb.add("OL_Erosion", Boolean.class);
		}
		// ************ VEGETATION PROTECTION OVERLAY ************
		SimpleFeatureCollection vegprotections = null;
		if (parameter.getVegprotection()) {
			System.out.println("------- getVegprotection true");
			filter = ff.equals(ff.property("group1"),ff.literal("VEGETATION PROTECTION OVERLAY"));
			vegprotections = overayFilters(filter);
			System.out.println("vegprotections size==="+vegprotections.size());	
			stb.add("OL_VegProtection", Boolean.class);
		}
		// ************ SALINITY MANAGEMENT OVERLAY ************
		SimpleFeatureCollection salinitys = null;
		if (parameter.getSalinity()) {
			System.out.println("------- getSalinity true");			 
			filter = ff.equals(ff.property("group1"),ff.literal("SALINITY MANAGEMENT OVERLAY"));
			salinitys = overayFilters(filter);
			System.out.println("salinitys size==="+salinitys.size());	
			stb.add("OL_Salinity", Boolean.class);
		}
		// ************ SALINITY MANAGEMENT OVERLAY ************
		SimpleFeatureCollection contaminations = null;
		if (parameter.getContamination()) {
			System.out.println("------- getContamination true");
			filter = ff.equals(ff.property("group1"),ff.literal("POTENTIALLY CONTAMINATED LAND OVERLAY"));
			contaminations = overayFilters(filter);
			System.out.println("contaminations size==="+contaminations.size());	
			stb.add("OL_Contamination", Boolean.class);
		}
		// ************ ENVIRONMENTAL SIGNIFICANCE OVERLAY ************
		SimpleFeatureCollection envSignificances = null;
		if (parameter.getEnvSignificance()) {
			System.out.println("------- getEnvSignificance true");
			filter = ff.equals(ff.property("group1"),ff.literal("ENVIRONMENTAL SIGNIFICANCE OVERLAY"));
			envSignificances = overayFilters(filter);
			System.out.println("envSignificances size==="+envSignificances.size());	
			stb.add("OL_EnvSignificance", Boolean.class);
		}
		// ************ ENVIRONMENTAL AUDIT OVERLAY ************
		SimpleFeatureCollection envAudits = null;
		if (parameter.getEnvAudit()) {
			System.out.println("------- getEnvAudit true");
			filter = ff.equals(ff.property("group1"),ff.literal("ENVIRONMENTAL AUDIT OVERLAY"));//???
			envAudits = overayFilters(filter);
			System.out.println("envAudits size==="+envAudits.size());	
			stb.add("OL_EnvAudit", Boolean.class);
		}
		// ************ HERITAGE OVERLAY ************
		SimpleFeatureCollection heritages = null;
		if (parameter.getHeritage()) {
			System.out.println("------- getHeritage true");
			filter = ff.equals(ff.property("group1"),ff.literal("HERITAGE OVERLAY"));// ???
			heritages = overayFilters(filter);
			System.out.println("heritages size==="+heritages.size());	
			stb.add("OL_Heritage", Boolean.class);
		}

		
		// ****************************** Create Feature Builder *****************************
		SimpleFeatureType newFeatureType = stb.buildFeatureType();
		SimpleFeatureBuilder sfb = new SimpleFeatureBuilder(newFeatureType);

		// **************** Loop through Property Layer Features ****************
		boolean createSchema = true;
		List<SimpleFeature> newList = new ArrayList<SimpleFeature>();
		SimpleFeatureIterator propertyIt = properties.features();
		try {
			int i = 0;
			while (propertyIt.hasNext()) {
				SimpleFeature sf = propertyIt.next();
				sfb.addAll(sf.getAttributes());
				Geometry propertyGeom = (Geometry) sf.getDefaultGeometry();
				// propertyGeom.setSRID(4283);
//				System.out.println("--- property pfi == "+ sf.getAttribute("pfi"));
				if (!propertyGeom.isValid()) {
					System.out.print(" ,    polygon  not valid ");
				}

				// **************** Intersections ****************
				if (floodways != null && !floodways.isEmpty()) {
					SimpleFeatureIterator overlayIt = floodways.features();
					while (overlayIt.hasNext()) {
						SimpleFeature overlayFt = overlayIt.next();
						Geometry overlayGeom = (Geometry) overlayFt.getDefaultGeometry();
						if (propertyGeom.intersects(overlayGeom)) {
							sfb.set("OL_Floodway", Boolean.TRUE);
							System.out.print("  ,   intersect with OL_Floodway == "+ overlayFt.getAttribute("ufi"));
							break;
						}
					}
					overlayIt.close();
				}				
				if (inundations != null && !inundations.isEmpty()) {
					SimpleFeatureIterator overlayIt = inundations.features();
					while (overlayIt.hasNext()) {
						SimpleFeature overlayFt = overlayIt.next();						
						Geometry overlayGeom = (Geometry) overlayFt.getDefaultGeometry();
						if (overlayGeom.intersects(propertyGeom)) {
							sfb.set("OL_Inundation", Boolean.TRUE);
							System.out.print("  ,   intersect with OL_Inundation  == "+ overlayFt.getAttribute("ufi"));
							break;
						}
					}
					overlayIt.close();
				}
				if (neighborhoods  != null && !neighborhoods.isEmpty()) {
					SimpleFeatureIterator overlayIt = neighborhoods.features();
					while (overlayIt.hasNext()) {
						SimpleFeature overlayFt = overlayIt.next();						
						Geometry overlayGeom = (Geometry) overlayFt.getDefaultGeometry();
						if (overlayGeom.intersects(propertyGeom)) {
							sfb.set("OL_Neighborhood", Boolean.TRUE);
							System.out.print("  ,   intersect with OL_Neighborhood  == "+ overlayFt.getAttribute("ufi"));
							break;
						}
					}
					overlayIt.close();
				}				
				if (designDevelopments  != null && !designDevelopments.isEmpty()) {
					SimpleFeatureIterator overlayIt = designDevelopments.features();
					while (overlayIt.hasNext()) {
						SimpleFeature overlayFt = overlayIt.next();						
						Geometry overlayGeom = (Geometry) overlayFt.getDefaultGeometry();
						if (overlayGeom.intersects(propertyGeom)) {
							sfb.set("OL_DesignDevelopment", Boolean.TRUE);
							System.out.print("  ,   intersect with OL_DesignDevelopment  == "+ overlayFt.getAttribute("ufi"));
							break;
						}
					}
					overlayIt.close();
				}				
				if (developPlans  != null && !developPlans.isEmpty()) {
					SimpleFeatureIterator overlayIt = developPlans.features();
					while (overlayIt.hasNext()) {
						SimpleFeature overlayFt = overlayIt.next();						
						Geometry overlayGeom = (Geometry) overlayFt.getDefaultGeometry();
						if (overlayGeom.intersects(propertyGeom)) {
							sfb.set("OL_DevelopmentPlan", Boolean.TRUE);
							System.out.print("  ,   intersect with OL_DevelopmentPlan  == "+ overlayFt.getAttribute("ufi"));
							break;
						}
					}
					overlayIt.close();
				}				
				if (parkings != null && !parkings.isEmpty()) {
					SimpleFeatureIterator overlayIt = parkings.features();
					while (overlayIt.hasNext()) {
						SimpleFeature overlayFt = overlayIt.next();						
						Geometry overlayGeom = (Geometry) overlayFt.getDefaultGeometry();
						if (overlayGeom.intersects(propertyGeom)) {
							sfb.set("OL_Parking", Boolean.TRUE);
							System.out.print("  ,   intersect with OL_Parking  == "+ overlayFt.getAttribute("ufi"));
							break;
						}
					}
					overlayIt.close();
				}  				
				if (bushfires != null && !bushfires.isEmpty()) {
					SimpleFeatureIterator overlayIt = bushfires.features();
					while (overlayIt.hasNext()) {
						SimpleFeature overlayFt = overlayIt.next();						
						Geometry overlayGeom = (Geometry) overlayFt.getDefaultGeometry();
						if (overlayGeom.intersects(propertyGeom)) {
							sfb.set("OL_Bushfire", Boolean.TRUE);
							System.out.print("  ,   intersect with OL_Bushfire  == "+ overlayFt.getAttribute("ufi"));
							break;
						}
					}
					overlayIt.close();
				}				
				if (erosions != null && !erosions.isEmpty()) {
					SimpleFeatureIterator overlayIt = erosions.features();
					while (overlayIt.hasNext()) {
						SimpleFeature overlayFt = overlayIt.next();						
						Geometry overlayGeom = (Geometry) overlayFt.getDefaultGeometry();
						if (overlayGeom.intersects(propertyGeom)) {
							sfb.set("OL_Erosion", Boolean.TRUE);
							System.out.print("  ,   intersect with OL_Erosion  == "+ overlayFt.getAttribute("ufi"));
							break;
						}
					}
					overlayIt.close();
				}				
				if (vegprotections != null && !vegprotections.isEmpty()) {
					SimpleFeatureIterator overlayIt = vegprotections.features();
					while (overlayIt.hasNext()) {
						SimpleFeature overlayFt = overlayIt.next();						
						Geometry overlayGeom = (Geometry) overlayFt.getDefaultGeometry();
						if (overlayGeom.intersects(propertyGeom)) {
							sfb.set("OL_VegProtection", Boolean.TRUE);
							System.out.print("  ,   intersect with OL_VegProtection  == "+ overlayFt.getAttribute("ufi"));
							break;
						}
					}
					overlayIt.close();
				}				
				if (salinitys != null && !salinitys.isEmpty()) {
					SimpleFeatureIterator overlayIt = salinitys.features();
					while (overlayIt.hasNext()) {
						SimpleFeature overlayFt = overlayIt.next();						
						Geometry overlayGeom = (Geometry) overlayFt.getDefaultGeometry();
						if (overlayGeom.intersects(propertyGeom)) {
							sfb.set("OL_Salinity", Boolean.TRUE);
							System.out.print("  ,   intersect with OL_Salinity  == "+ overlayFt.getAttribute("ufi"));
							break;
						}
					}
					overlayIt.close();
				}				
				if (contaminations != null && !contaminations.isEmpty()) {
					SimpleFeatureIterator overlayIt = contaminations.features();
					while (overlayIt.hasNext()) {
						SimpleFeature overlayFt = overlayIt.next();						
						Geometry overlayGeom = (Geometry) overlayFt.getDefaultGeometry();
						if (overlayGeom.intersects(propertyGeom)) {
							sfb.set("OL_Contamination", Boolean.TRUE);
							System.out.print("  ,   intersect with OL_Contamination  == "+ overlayFt.getAttribute("ufi"));
							break;
						}
					}
					overlayIt.close();
				}	
				
				if (envSignificances != null && !envSignificances.isEmpty()) {
					SimpleFeatureIterator overlayIt = envSignificances.features();
					while (overlayIt.hasNext()) {
						SimpleFeature overlayFt = overlayIt.next();						
						Geometry overlayGeom = (Geometry) overlayFt.getDefaultGeometry();
						if (overlayGeom.intersects(propertyGeom)) {
							sfb.set("OL_EnvSignificance", Boolean.TRUE);
							System.out.print("  ,   intersect with OL_EnvSignificance  == "+ overlayFt.getAttribute("ufi"));
							break;
						}
					}
					overlayIt.close();
				}				
				if (envAudits != null && !envAudits.isEmpty()) {
					SimpleFeatureIterator overlayIt = envAudits.features();
					while (overlayIt.hasNext()) {
						SimpleFeature overlayFt = overlayIt.next();						
						Geometry overlayGeom = (Geometry) overlayFt.getDefaultGeometry();
						if (overlayGeom.intersects(propertyGeom)) {
							sfb.set("OL_EnvAudit", Boolean.TRUE);
							System.out.print("  ,   intersect with OL_EnvAudit  == "+ overlayFt.getAttribute("ufi"));
							break;
						}
					}
					overlayIt.close();
				}				
				if (heritages != null && !heritages.isEmpty()) {
					SimpleFeatureIterator overlayIt = heritages.features();
					while (overlayIt.hasNext()) {
						SimpleFeature overlayFt = overlayIt.next();						
						Geometry overlayGeom = (Geometry) overlayFt.getDefaultGeometry();
						if (overlayGeom.intersects(propertyGeom)) {
							sfb.set("OL_Heritage", Boolean.TRUE);
							System.out.print("  ,   intersect with OL_Heritage  == "+ overlayFt.getAttribute("ufi"));
							break;
						}
					}
					overlayIt.close();
				}
				// **************** Intersections End ****************

				SimpleFeature newFeature = sfb.buildFeature(null);
				newList.add(newFeature);

				i++;
				if (i == 10000) {
					System.out.println("properties.size() < 10000");
					
					featureCollectionNew = new ListFeatureCollection(
							newFeatureType, newList);
					exportToShpService.featuresExportToPostGis(newFeatureType,
							featureCollectionNew, createSchema, dataStore);
					// File newFile = new
					// File("C:/Programming/Projects/Data/Final.shp");
					// exportToShpService.featuresExportToShapeFile(
					// sfb.getFeatureType(), featureCollectionNew,
					// newFile, createSchema);
					newList = new ArrayList<SimpleFeature>();
					i = 0;
					createSchema = false;
				}
			}
		} finally {
			propertyIt.close();
		}
		
		if (!newList.isEmpty()){
			System.out.println("properties.size() < 10000");
			featureCollectionNew = new ListFeatureCollection(
					newFeatureType, newList);
			exportToShpService.featuresExportToPostGis(newFeatureType,
					featureCollectionNew, createSchema, dataStore);
		}

		System.out.println(new Date());
		System.out.println("------------ featureCollectionNew.size() == "+ featureCollectionNew.size());
		System.out.println("------------ properties.size() == "	+ properties.size());

		WeakCollectionCleaner.currentThread().run();
		return properties;
	}

	// **************** Land Use ****************
	private void landUseFilters(Filter filter) throws IOException {
		SimpleFeatureSource zonecodesFc = dataStore.getFeatureSource("tbl_zonecodes");
		Query zoneCodeQuery = new Query();
		zoneCodeQuery.setPropertyNames(new String[] { "zone_code", "group1" });
		zoneCodeQuery.setFilter(filter);
		SimpleFeatureCollection zoneCodes = zonecodesFc.getFeatures(zoneCodeQuery);
		SimpleFeatureIterator it = zoneCodes.features();
		List<Filter> match = new ArrayList<Filter>();
		while (it.hasNext()) {
			SimpleFeature zoneCode = it.next();
			Object value = zoneCode.getAttribute("zone_code");
			filter = ff.equals(ff.property("zoning"), ff.literal(value));
			match.add(filter);
		}
		it.close();

		Filter filterRES = ff.or(match);
		landUseFilters.add(filterRES);
	}

	// **************** Overlay ****************
	private SimpleFeatureCollection overayFilters(Filter filter) throws IOException {
		SimpleFeatureSource planCodeListFc = dataStore.getFeatureSource("plan_codelist");
		SimpleFeatureSource planOverlayFc = dataStore.getFeatureSource("plan_overlay");	
		
		Query codeListQuery = new Query();
		codeListQuery.setPropertyNames(new String[] { "zone_code", "group1" });
		codeListQuery.setFilter(filter);
		SimpleFeatureCollection codeList = planCodeListFc.getFeatures(codeListQuery);
		
		System.out.println("codeList.size()===  "+codeList.size()); 
		
		SimpleFeatureIterator it = codeList.features();
		List<Filter> match = new ArrayList<Filter>();
		while (it.hasNext()) {
			SimpleFeature zoneCode = it.next();
			Object value = zoneCode.getAttribute("zone_code");
			filter = ff.equals(ff.property("zone_code"), ff.literal(value));
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
