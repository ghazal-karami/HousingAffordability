package au.org.housing.service.impl;

import it.geosolutions.geoserver.rest.GeoServerRESTPublisher;
import it.geosolutions.geoserver.rest.GeoServerRESTReader;
import it.geosolutions.geoserver.rest.encoder.GSLayerEncoder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

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
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Divide;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.postgresql.util.PSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import au.org.housing.config.GeoServerConfig;
import au.org.housing.config.InputLayersConfig;
import au.org.housing.config.OutPutLayerConfig;
import au.org.housing.exception.Messages;
import au.org.housing.model.LayerRepository;
import au.org.housing.model.ParameterDevelopPotential;
import au.org.housing.service.ExportService;
import au.org.housing.service.FeatureBuilder;
import au.org.housing.service.PostGISService;
import au.org.housing.service.PropertyFilterService;
import au.org.housing.service.UnionService;
import au.org.housing.service.ValidationService;
import au.org.housing.utilities.TemporaryFileManager;
import au.org.housing.utilities.Zip;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.TopologyException;

@Service
public class PropertyFilterServiceImpl implements PropertyFilterService {

	private static final Logger LOGGER = LoggerFactory.getLogger(PropertyFilterServiceImpl.class);

	@Autowired
	private ParameterDevelopPotential parameter;
	
	@Autowired
	private PostGISService postGISService;
	
	@Autowired
	private GeoServerConfig geoServerConfig;
	
	private ReferencedEnvelope envelope;
	private String layerName;

	@Autowired
	private ValidationService validationService;

	@Autowired
	private ExportService exportService;

	@Autowired
	private UnionService unionService;

	@Autowired 
	private FeatureBuilder featureBuilder;

	SimpleFeatureSource propertyFc;
	SimpleFeatureSource planCodeListFc;
	SimpleFeatureSource planOverlayFc;
	SimpleFeatureSource zonecodesFc;

	FilterFactory2 ff;
	Filter propertyFilter;
	Filter lgaFilter;
	Query propertyQuery ;
	SimpleFeatureTypeBuilder stb;
	SimpleFeatureBuilder sfb;
	SimpleFeatureType newFeatureType;

	Filter ownershipFilter;
	Filter landUseFilter;
	Map<String, Object> overlayMap;

	List<Filter> propertyFilters;
	List<Filter> landUseFilters;
	
	private Geometry bufferAllParams = null;
	SimpleFeatureCollection properties;
	boolean dropCreateSchema ;	
	Geometry inundationsUnion = null;
	
	Integer propertyOverlaysNum;

	@Autowired LayerRepository layerRepo;

	@Autowired
	private InputLayersConfig inputLayersConfig;
	
	@Autowired
	private OutPutLayerConfig outPutLayerConfig;
	

	//************************* Check if the layer is Metric  ***************************

	public void analyse(HttpSession session) throws SQLException, Exception{
		if (!layersValidation() ){
			LOGGER.error(Messages.getMessage());
		}
		propertyOverlaysNum = -1;		
//		SimpleFeatureType sft = propertyFc.getSchema();
//		stb = featureBuilder.createFeatureTypeBuilder(sft, "OutPutData");	
		dropCreateSchema = true;
		propertyFilters = new ArrayList<Filter>();
		propertyFilter = null;	
		ownershipFilter = null;
		landUseFilter = null;
		ff = CommonFactoryFinder.getFilterFactory2(GeoTools.getDefaultHints());		
		this.lgaFilter();
		this.dpiFilter();
		this.landUseFilter();
		this.bufferAllParamsFilter();
		this.ownershipFilter();		
		this.propertyFilter();
		this.overlayCollection();
		this.generateQuery(session);	
		
	}
	
	private boolean layersValidation() throws IOException, PSQLException {
		propertyFc = postGISService.getFeatureSource(inputLayersConfig.getProperty());
		LOGGER.info(propertyFc.getSchema().getCoordinateReferenceSystem().toString());
		planCodeListFc = postGISService.getFeatureSource(inputLayersConfig.getPlanCodes());
		planOverlayFc = postGISService.getFeatureSource(inputLayersConfig.getPlanOverlay());
		LOGGER.info(planOverlayFc.getSchema().getCoordinateReferenceSystem().toString());
		zonecodesFc =  postGISService.getFeatureSource(inputLayersConfig.getZonecodesTbl());
		if (    !validationService.propertyValidated(propertyFc, inputLayersConfig.getProperty())  || !validationService.isPolygon(propertyFc, inputLayersConfig.getProperty()) || !validationService.isMetric(propertyFc, inputLayersConfig.getProperty())||
				!validationService.planOverlayValidated(planOverlayFc, inputLayersConfig.getPlanOverlay()) || !validationService.isMetric(planOverlayFc, inputLayersConfig.getPlanOverlay()) || !validationService.isPolygon(planOverlayFc, inputLayersConfig.getPlanOverlay()) ||
				!validationService.planCodeListValidated(planCodeListFc, inputLayersConfig.getPlanCodes()) ||
				!validationService.zonecodesValidated(zonecodesFc, inputLayersConfig.getZonecodesTbl()) ){
			return false;
		}
		return true;
	}
	
	private void lgaFilter() throws IOException{		
		List<Filter> lgaFilters = new ArrayList<Filter>();
		for (String lga_code : parameter.getSelectedLGAs()) {
			Filter filter = ff.equals(ff.property("lga_code"),ff.literal(lga_code));
			lgaFilters.add(filter);
		}
		Filter lgaFilter = ff.or(lgaFilters);
		propertyFilters.add(lgaFilter);
		System.out.println("lgaFilter=" + lgaFilter);
	}
	

	private void dpiFilter() throws CQLException{
		if (parameter.getDpi() != 0) {
			Divide divide = ff.divide(ff.property(inputLayersConfig.getProperty_svCurrentYear()),
					ff.property(inputLayersConfig.getProperty_civCurrentYear()));
			String operator = parameter.getDpiOperateorVal();
			Filter filterDPI = null;
			switch (operator.charAt(0)) {
			case '>':
				filterDPI =  ff.greater( divide, ff.literal(parameter.getDpi()) );
				break;
			case '=':
				Filter filterGreater =  ff.greater( divide, ff.literal(parameter.getDpi()) );
				Filter filterEquals =  ff.equals( divide, ff.literal(parameter.getDpi()) );					
				Filter filterGreaterOrEqual = ff.or( filterGreater, filterEquals );
				Filter filterLess =  ff.less( divide, ff.literal(parameter.getDpi()+0.1) );
				filterDPI = ff.and( filterGreaterOrEqual, filterLess );
				break;
			case '<':
				filterDPI =  ff.less( divide, ff.literal(parameter.getDpi()+0.1) );
				break;
			}
			propertyFilters.add(filterDPI);			
		}
	}

	private void landUseFilter() throws IOException{
		landUseFilters = new ArrayList<Filter>();
		Filter filter = null;
		if (parameter.getResidential()) {
			filter = ff.equals(ff.property(inputLayersConfig.getZonecodes_group1()), ff.literal("RESIDENTIAL"));
			landUseFilters(filter);
		}
		if (parameter.getBusiness()) {
			filter = ff.equals(ff.property(inputLayersConfig.getZonecodes_group1()), ff.literal("BUSINESS"));
			landUseFilters(filter);
		}
		if (parameter.getRural()) {
			filter = ff.equals(ff.property(inputLayersConfig.getZonecodes_group1()), ff.literal("RURAL"));
			landUseFilters(filter);
		}
		if (parameter.getMixedUse()) {
			filter = ff.equals(ff.property(inputLayersConfig.getZonecodes_group1()), ff.literal("MIXED USE"));
			landUseFilters(filter);
		}
		if (parameter.getSpecialPurpose()) {
			filter = ff.equals(ff.property(inputLayersConfig.getZonecodes_group1()),ff.literal("SPECIAL PURPOSE"));
			landUseFilters(filter);
		}
		if (parameter.getUrbanGrowthBoundry()) {
			filter = ff.equals(ff.property(inputLayersConfig.getZonecodes_group1()),ff.literal("URBAN GROWTH BOUNDARY"));
			landUseFilters(filter);
		}		
		if (!landUseFilters.isEmpty()) {
			landUseFilter = ff.or(landUseFilters);
		}
	}

	private void bufferAllParamsFilter(){
		if (bufferAllParams != null) {
			Filter filter = null;
			LOGGER.info("bufferAllParams is not null");
			if (bufferAllParams.isValid()){
				LOGGER.info("bufferAllParams is Valid");
			}
			GeometryDescriptor gd = propertyFc.getSchema().getGeometryDescriptor();
			String gemoName = gd.getName().toString();
			filter = ff.within(ff.property(gemoName),ff.literal(bufferAllParams));
			propertyFilters.add(filter);
			//			filter = ff.equals(ff.property("pfi"),ff.literal("5026012"));//
			//			filter = ff.equals(ff.property("pfi"),ff.literal("3196232"));//public acuisition
			//			filter = ff.equals(ff.property("pfi"),ff.literal("3539423"));//inundation
			propertyFilters.add(filter);
		}
	}

	private void ownershipFilter() throws IOException{
		List<Filter> ownershipFilters= new ArrayList<Filter>();
		Filter filter = null;
		Geometry publicAcquisitionsUnion;
		SimpleFeatureCollection publicAcquisitions = null;
		if (parameter.getPublicAcquision()) {
			System.out.println("------- PUBLIC ACQUISION true");	 
			filter = ff.equals(ff.property(inputLayersConfig.getPlanCodes_group1()),ff.literal("PUBLIC ACQUISION OVERLAY"));
			publicAcquisitions = overlayCollection(filter);			
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
			filter = ff.equals(ff.property(inputLayersConfig.getPlanCodes_group1()),ff.literal("COMMONWEALTH LAND"));
			commonWealths = overlayCollection(filter);			
			System.out.println("commonWealths size==="+commonWealths.size());
			commonWealthsUnion = (Geometry) unionService.createUnion(commonWealths);
			GeometryDescriptor gd = propertyFc.getSchema().getGeometryDescriptor();
			String gemoName = gd.getName().toString();
			filter = ff.not(ff.intersects(ff.property(gemoName),ff.literal(commonWealthsUnion)));	
		}		
		if (!ownershipFilters.isEmpty()) {
			ownershipFilter = ff.and(ownershipFilters);
		}
	}

	private void overlayCollection() throws IOException{
		Filter filter = null;
		overlayMap = new HashMap<String, Object>();

		//************ FLOODWAY OVERLAY ************	
		Geometry floodwaysUnion = null;
		if (parameter.getFloodway()) {			 
			filter = ff.equals(ff.property(inputLayersConfig.getPlanCodes_group1()),ff.literal("FLOODWAY OVERLAY"));
			SimpleFeatureCollection floodways = overlayCollection(filter);
			if ( floodways != null ){
				floodwaysUnion = (Geometry) unionService.createUnion(floodways);
				overlayMap.put("floodways", floodwaysUnion);
				System.out.println("floodway size==="+floodways.size());
			}else{
				overlayMap.put("floodways", null);
			}
			stb.add("OL_Floodway", Boolean.class); 
			propertyOverlaysNum = 0;
		}
		//************ LAND SUBJECT TO INUNDATION OVERLAY ************
		//		Geometry inundationsUnion = null; //????
		if (parameter.getInundation()) {		 
			filter = ff.equals(ff.property(inputLayersConfig.getPlanCodes_group1()),ff.literal("LAND SUBJECT TO INUNDATION OVERLAY"));
			SimpleFeatureCollection inundations = overlayCollection(filter);
			if ( inundations != null ){
				inundationsUnion = unionService.createUnion(inundations) ; 
				overlayMap.put("inundations", inundationsUnion);
				System.out.println("inundations size==="+inundations.size());
			}else{
				overlayMap.put("inundations", null);
			}
			stb.add("OL_Inundation", Boolean.class); 
			propertyOverlaysNum = 0;
		}
		//************ NEIGHBOURHOOD CHARACTER OVERLAY ************
		Geometry neighborhoodsUnion = null;		
		if (parameter.getNeighborhood()) {		 
			filter = ff.equals(ff.property(inputLayersConfig.getPlanCodes_group1()),ff.literal("NEIGHBOURHOOD CHARACTER OVERLAY"));
			SimpleFeatureCollection neighborhoods = overlayCollection(filter);
			if ( neighborhoods != null ){
				neighborhoodsUnion = (Geometry) unionService.createUnion(neighborhoods);

				overlayMap.put("neighborhoods", neighborhoodsUnion);
				System.out.println("neighborhoods size==="+neighborhoods.size());
			}else{
				overlayMap.put("neighborhoods", null);
			}
			stb.add("OL_Neighborhood", Boolean.class); 
			propertyOverlaysNum = 0;
		}

		//************ DESIGN AND DEVELOPMENT OVERLAY ************
		Geometry designDevelopmentsUnion = null;		
		if (parameter.getDesignDevelopment()) {			 
			filter = ff.equals(ff.property(inputLayersConfig.getPlanCodes_group1()),ff.literal("DESIGN AND DEVELOPMENT OVERLAY"));
			SimpleFeatureCollection designDevelopments = overlayCollection(filter);
			if ( designDevelopments != null ){
				designDevelopmentsUnion = (Geometry) unionService.createUnion(designDevelopments);
				overlayMap.put("designDevelopments", designDevelopmentsUnion);
				System.out.println("designDevelopments size==="+designDevelopments.size());	
			}else{
				overlayMap.put("designDevelopments", null);
			}
			stb.add("OL_DesignDevelopment", Boolean.class);
			propertyOverlaysNum = 0;
		}
		//************ DESIGN AND DEVELOPMENT OVERLAY ************
		Geometry developPlansUnion = null;		
		if (parameter.getDevelopPlan()) {		 
			filter = ff.equals(ff.property(inputLayersConfig.getPlanCodes_group1()),ff.literal("DEVELOPMENT PLAN OVERLAY"));
			SimpleFeatureCollection developPlans = overlayCollection(filter);
			if ( developPlans != null ){
				developPlansUnion = (Geometry) unionService.createUnion(developPlans);
				overlayMap.put("developPlans", developPlansUnion);
				System.out.println("developPlans size==="+developPlans.size());	
			}else{
				overlayMap.put("developPlans", null);
			}
			stb.add("OL_DevelopmentPlan", Boolean.class); 
			propertyOverlaysNum = 0;
		}
		// ************ PARKING OVERLAY ************
		Geometry parkingsUnion = null;		
		if (parameter.getParking() ) {		 
			filter = ff.equals(ff.property(inputLayersConfig.getPlanCodes_group1()),ff.literal("PARKING OVERLAY"));
			SimpleFeatureCollection parkings = overlayCollection(filter);
			if ( parkings != null ){
				parkingsUnion = (Geometry) unionService.createUnion(parkings);
				overlayMap.put("parkings", parkingsUnion);
				System.out.println("parkings size==="+parkings.size());	
			}else{
				overlayMap.put("parkings", null);
			}
			stb.add("OL_Parking", Boolean.class);
			propertyOverlaysNum = 0;
		}
		// ************ BUSHFIRE MANAGEMENT OVERLAY ************
		Geometry bushfiresUnion = null;	
		if (parameter.getBushfire() ) {	 
			filter = ff.equals(ff.property(inputLayersConfig.getPlanCodes_group1()),ff.literal("BUSHFIRE MANAGEMENT OVERLAY"));
			SimpleFeatureCollection bushfires = overlayCollection(filter);
			if ( bushfires != null ){
				bushfiresUnion = (Geometry) unionService.createUnion(bushfires);
				overlayMap.put("bushfires", bushfiresUnion);
				System.out.println("bushfires size==="+bushfires.size());
			}else{
				overlayMap.put("bushfires", null);
			}
			stb.add("OL_Bushfire", Boolean.class);
			propertyOverlaysNum = 0;
		}
		// ************ EROSION MANAGEMENT OVERLAY ************
		Geometry erosionsUnion = null;	
		if (parameter.getErosion()) {	 
			filter = ff.equals(ff.property(inputLayersConfig.getPlanCodes_group1()),ff.literal("EROSION MANAGEMENT OVERLAY"));
			SimpleFeatureCollection erosions = overlayCollection(filter);
			if ( erosions != null ){
				erosionsUnion = (Geometry) unionService.createUnion(erosions);
				overlayMap.put("erosions", bushfiresUnion);
				System.out.println("erosions size==="+erosions.size());	
			}else{
				overlayMap.put("erosions", null);
			}
			stb.add("OL_Erosion", Boolean.class);
			propertyOverlaysNum = 0;
		}
		// ************ VEGETATION PROTECTION OVERLAY ************
		Geometry vegprotectionsUnion = null;	
		if (parameter.getVegprotection()) {
			filter = ff.equals(ff.property(inputLayersConfig.getPlanCodes_group1()),ff.literal("VEGETATION PROTECTION OVERLAY"));
			SimpleFeatureCollection vegprotections = overlayCollection(filter);
			if ( vegprotections != null ){
				vegprotectionsUnion = (Geometry) unionService.createUnion(vegprotections);
				overlayMap.put("vegprotections", vegprotectionsUnion);
				System.out.println("vegprotections size==="+vegprotections.size());	
			}else{
				overlayMap.put("vegprotections", null);
			}
			stb.add("OL_VegProtection", Boolean.class);
			propertyOverlaysNum = 0;
		}
		// ************ SALINITY MANAGEMENT OVERLAY ************
		Geometry salinitysUnion = null;
		if (parameter.getSalinity()) {	 
			filter = ff.equals(ff.property(inputLayersConfig.getPlanCodes_group1()),ff.literal("SALINITY MANAGEMENT OVERLAY"));
			SimpleFeatureCollection salinitys = overlayCollection(filter);
			if ( salinitys != null ){
				salinitysUnion = (Geometry) unionService.createUnion(salinitys);
				overlayMap.put("salinitys", salinitysUnion);
				System.out.println("salinitys size==="+salinitys.size());	
			}else{
				overlayMap.put("salinitys", null);
			}
			stb.add("OL_Salinity", Boolean.class);
			propertyOverlaysNum = 0;
		}
		// ************ CONTAMINATION MANAGEMENT OVERLAY ************
		Geometry contaminationsUnion = null;
		if (parameter.getContamination()) {
			filter = ff.equals(ff.property(inputLayersConfig.getPlanCodes_group1()),ff.literal("POTENTIALLY CONTAMINATED LAND OVERLAY"));
			SimpleFeatureCollection contaminations = overlayCollection(filter);
			if ( contaminations != null ){
				contaminationsUnion = (Geometry) unionService.createUnion(contaminations);
				overlayMap.put("contaminations", contaminationsUnion);
				System.out.println("contaminations size==="+contaminations.size());	
			}else{
				overlayMap.put("contaminations", null);
			}
			stb.add("OL_Contamination", Boolean.class);
			propertyOverlaysNum = 0;
		}
		// ************ ENVIRONMENTAL SIGNIFICANCE OVERLAY ************
		Geometry envSignificancesUnion = null;
		if (parameter.getEnvSignificance()) {
			filter = ff.equals(ff.property(inputLayersConfig.getPlanCodes_group1()),ff.literal("ENVIRONMENTAL SIGNIFICANCE OVERLAY"));
			SimpleFeatureCollection envSignificances = overlayCollection(filter);
			if ( envSignificances != null ){
				envSignificancesUnion = (Geometry) unionService.createUnion(envSignificances);
				overlayMap.put("envSignificances", envSignificancesUnion);
				System.out.println("envSignificances size==="+envSignificances.size());	
			}else{
				overlayMap.put("envSignificances", null);
			}
			stb.add("OL_EnvSignificance", Boolean.class);
			propertyOverlaysNum = 0;
		}
		// ************ ENVIRONMENTAL AUDIT OVERLAY ************
		Geometry envAuditsUnion = null;
		if (parameter.getEnvAudit()) {
			filter = ff.equals(ff.property(inputLayersConfig.getPlanCodes_group1()),ff.literal("ENVIRONMENTAL AUDIT OVERLAY"));//???
			SimpleFeatureCollection envAudits = overlayCollection(filter);
			if ( envAudits != null ){
				envAuditsUnion = (Geometry) unionService.createUnion(envAudits);
				overlayMap.put("envAudits", envAuditsUnion);
				System.out.println("envAudits size==="+envAudits.size());	
			}else{
				overlayMap.put("envAudits", null);
			}
			stb.add("OL_EnvAudit", Boolean.class);
			propertyOverlaysNum = 0;
		}
		// ************ HERITAGE OVERLAY ************		
		Geometry heritageUnion = null;
		if (parameter.getHeritage()) {
			filter = ff.equals(ff.property(inputLayersConfig.getPlanCodes_group1()),ff.literal("HERITAGE OVERLAY"));// ???
			SimpleFeatureCollection heritages = overlayCollection(filter);
			if (heritages != null){
				heritageUnion = (Geometry) unionService.createUnion(heritages);
				overlayMap.put("heritages", heritageUnion);
				System.out.println("heritages size==="+heritages.size());
			}else{
				overlayMap.put("heritages", null);
			}
			stb.add("OL_Heritage", Boolean.class);
			propertyOverlaysNum = 0;
		}


	}

	private void propertyFilter() throws IOException{	
		if (!propertyFilters.isEmpty() ) {
			if (landUseFilter != null){
				propertyFilters.add(landUseFilter);				
			}
			if (ownershipFilter != null){
				propertyFilters.add(ownershipFilter);				
			}
			propertyFilter = ff.and(propertyFilters);
		}		
	}

	private void generateQuery(HttpSession session) throws SQLException, Exception{
		propertyQuery = new Query();
		String geom_name = propertyFc.getSchema().getGeometryDescriptor().getLocalName();
		String[] attributes = {geom_name, outPutLayerConfig.getObjectid(), outPutLayerConfig.getPfi(), outPutLayerConfig.getLgaName()
				, outPutLayerConfig.getStreet_name(), outPutLayerConfig.getStreet_type(), outPutLayerConfig.getSuburb()
				, outPutLayerConfig.getPostcode(), outPutLayerConfig.getLand_area(), outPutLayerConfig.getAreameasure() };
		propertyQuery.setPropertyNames(attributes);
		propertyQuery.setFilter(propertyFilter);		
		
		
//		if (propertyFilter != null) {
//			propertyQuery.setFilter(propertyFilter);
//		} else {
//			List<Filter> fs = new ArrayList<Filter>();
//			Filter filter = ff.equals(ff.property("pfi"),ff.literal("3539423"));// no inundation 
//			fs.add(filter);
//			filter = ff.equals(ff.property("pfi"),ff.literal("4787005"));//  inundation 
//			fs.add(filter);
//			filter = ff.equals(ff.property("pfi"),ff.literal("1317010"));//  inundation 
//			fs.add(filter);
//			filter = ff.equals(ff.property("lga_code"),ff.literal("1317010"));//  inundation 
//			fs.add(filter);
//			propertyFilter = ff.or(fs);	
//			propertyQuery.setFilter(propertyFilter);
//			System.out.println("if (propertyFilter ===== null) {");
//		}
		
		properties= propertyFc.getFeatures(propertyQuery);
		envelope = properties.getBounds();
		if (properties != null && !properties.isEmpty()) {
			System.out.println("size= "+properties.size());			
			stb = featureBuilder.createFeatureTypeBuilder(properties.getSchema(), "OutPut");	
			stb.add("OverlaysNum", Integer.class); 
			newFeatureType = stb.buildFeatureType();		 
			
//			newFeatureType = stb.buildFeatureType();
			sfb = new SimpleFeatureBuilder(newFeatureType);
			this.overlayFilter(session);
		}
		
	}

	private void overlayFilter(HttpSession session) throws NoSuchAuthorityCodeException, IOException, FactoryException, SQLException, Exception{
		File newDirectory =  TemporaryFileManager.getNew(session, geoServerConfig.getGsPotentialLayer() , "",true);
		File newFile = new File(newDirectory.getAbsolutePath()+"/"+geoServerConfig.getGsPotentialLayer()+"_"+session.getId()+ ".shp");
		System.out.println(newFile.toURI());
				
		List<SimpleFeature> newList = new ArrayList<SimpleFeature>();
		SimpleFeatureIterator propertyIt = null;
		propertyIt = properties.features();
		LOGGER.info(String.valueOf(properties.size()));
		SimpleFeatureCollection featureCollectionNew = FeatureCollections.newCollection();			
		try {
			int i = 0;
			int it = 0;
			LOGGER.info("1111"+new Date());
			while (propertyIt.hasNext()) {
				SimpleFeature sf = propertyIt.next();
				sfb.addAll(sf.getAttributes());
				Geometry propertyGeom = (Geometry) sf.getDefaultGeometry();

				try{					
					System.out.print(sf.getAttribute("objectid")+" ,   ");

					if (propertyOverlaysNum != -1){
					// **************** Intersections ****************
					Geometry floodwaysUnion = (Geometry) overlayMap.get("floodways");
					if (floodwaysUnion != null) {
						if (floodwaysUnion.intersects(propertyGeom)) {
							sfb.set("OL_Floodway", Boolean.TRUE);
							propertyOverlaysNum++;
							//System.out.print("  ,   intersect with OL_Floodway  == ");						
						}									
					}		
					if (inundationsUnion != null) {								
						if (inundationsUnion.intersects(propertyGeom)) {
							sfb.set("OL_Inundation", Boolean.TRUE);	
							propertyOverlaysNum++;
							System.out.println("    ,   intersect with OL_Inundation  == "+sf.getAttribute("objectid"));							
						}									
					}				
					Geometry neighborhoodsUnion = (Geometry) overlayMap.get("neighborhoods");
					if (neighborhoodsUnion != null) {
						if (neighborhoodsUnion.intersects(propertyGeom)) {
							sfb.set("OL_Neighborhood", Boolean.TRUE);
							propertyOverlaysNum++;
							System.out.print("  ,   intersect with OL_Neighborhood  == ");						
						}									
					}				
					Geometry designDevelopmentsUnion = (Geometry) overlayMap.get("designDevelopments");
					if (designDevelopmentsUnion != null) {
						if (designDevelopmentsUnion.intersects(propertyGeom)) {
							sfb.set("OL_DesignDevelopment", Boolean.TRUE);
							propertyOverlaysNum++;
							System.out.print("  ,   intersect with OL_DesignDevelopment  == ");						
						}									
					}				
					Geometry developPlansUnion = (Geometry) overlayMap.get("developPlans");
					if (developPlansUnion != null) {
						if (developPlansUnion.intersects(propertyGeom)) {
							sfb.set("OL_DevelopmentPlan", Boolean.TRUE);
							propertyOverlaysNum++;
							System.out.print("  ,   intersect with OL_DevelopmentPlan  == ");						
						}									
					}				
					Geometry parkingsUnion = (Geometry) overlayMap.get("parkings");
					if (parkingsUnion != null) {
						if (parkingsUnion.intersects(propertyGeom)) {
							sfb.set("OL_Parking", Boolean.TRUE);
							propertyOverlaysNum++;
							System.out.print("  ,   intersect with OL_Parking  == ");						
						}									
					}					
					Geometry bushfiresUnion = (Geometry) overlayMap.get("bushfires");
					if (bushfiresUnion != null) {
						if (bushfiresUnion.intersects(propertyGeom)) {
							sfb.set("OL_Bushfire", Boolean.TRUE);
							propertyOverlaysNum++;
							System.out.print("  ,   intersect with OL_Bushfire  == ");						
						}									
					}				
					Geometry erosionsUnion = (Geometry) overlayMap.get("erosions");
					if (erosionsUnion != null) {
						if (erosionsUnion.intersects(propertyGeom)) {
							sfb.set("OL_Erosion", Boolean.TRUE);
							propertyOverlaysNum++;
							System.out.print("  ,   intersect with OL_Erosion  == ");						
						}									
					}				
					Geometry vegprotectionsUnion = (Geometry) overlayMap.get("vegprotections");
					if (vegprotectionsUnion != null) {
						if (vegprotectionsUnion.intersects(propertyGeom)) {
							sfb.set("OL_VegProtection", Boolean.TRUE);
							propertyOverlaysNum++;
							System.out.print("  ,   intersect with OL_VegProtection  == ");						
						}									
					}				
					Geometry salinitysUnion = (Geometry) overlayMap.get("salinitys");
					if (salinitysUnion != null) {
						if (salinitysUnion.intersects(propertyGeom)) {
							sfb.set("OL_Salinity", Boolean.TRUE);
							propertyOverlaysNum++;
							System.out.print("  ,   intersect with OL_Salinity  == ");						
						}									
					}				
					Geometry contaminationsUnion = (Geometry) overlayMap.get("contaminations");
					if (contaminationsUnion != null) {
						if (contaminationsUnion.intersects(propertyGeom)) {
							sfb.set("OL_Contamination", Boolean.TRUE);
							propertyOverlaysNum++;
							System.out.print("  ,   intersect with OL_Contamination  == ");						
						}									
					}				
					Geometry envSignificancesUnion = (Geometry) overlayMap.get("envSignificances");
					if (envSignificancesUnion != null) {
						if (envSignificancesUnion.intersects(propertyGeom)) {
							sfb.set("OL_EnvSignificance", Boolean.TRUE);
							propertyOverlaysNum++;
							System.out.print("  ,   intersect with OL_EnvSignificance  == ");						
						}									
					}				
					Geometry envAuditsUnion = (Geometry) overlayMap.get("envAudits");
					if (envAuditsUnion != null) {
						if (envAuditsUnion.intersects(propertyGeom)) {
							sfb.set("OL_EnvAudit", Boolean.TRUE);
							propertyOverlaysNum++;
							System.out.print("  ,   intersect with OL_EnvAudit  == ");						
						}									
					}
					Geometry heritageUnion = (Geometry) overlayMap.get("heritages");
					if (heritageUnion != null) {
						if (heritageUnion.intersects(propertyGeom)) {
							sfb.set("OL_Heritage", Boolean.TRUE);
							propertyOverlaysNum++;
							System.out.print("  ,   intersect with OL_Heritage  == ");

						}									
					}
					}
					// **************** Intersections End ****************
					sfb.set("OverlaysNum", propertyOverlaysNum);
					SimpleFeature newFeature = sfb.buildFeature(null);
					newList.add(newFeature);

					i++;
					if (i == 1000) {
						System.out.println("properties.size() == 10000");
						featureCollectionNew = new ListFeatureCollection(newFeatureType, newList);
						//						exportService.featuresExportToPostGis(newFeatureType,
						//								featureCollectionNew, dropCreateSchema,   Config.getDefaultFactory().getExportableDataStore());						 
						exportService.featuresExportToShapeFile(sfb.getFeatureType(), featureCollectionNew,
								newFile, dropCreateSchema);
						newList = new ArrayList<SimpleFeature>();
						i = 0;
						dropCreateSchema = false;
					}
				}catch(TopologyException e){
					System.out.print(sf.getAttribute("objectid")+" , errrrrrrrrrrrrrrrrrror");
					LOGGER.error(e.getMessage());
				}
			}
		} finally {
			propertyIt.close();
		}

		if (!newList.isEmpty()){
			System.out.println("properties.size() < 1000");
			featureCollectionNew = new ListFeatureCollection(
					newFeatureType, newList);
			exportService.featuresExportToShapeFile(sfb.getFeatureType(), featureCollectionNew,
					newFile, dropCreateSchema);
		}	
		this.publishToGeoserver(newFile, session);
	}
	//*************************   ***************************
	private boolean publishToGeoserver(File newFile, HttpSession session) throws FileNotFoundException, IllegalArgumentException, MalformedURLException{
		layerName = geoServerConfig.getGsPotentialLayer()+"_"+session.getId();
		GeoServerRESTReader reader = 
				new GeoServerRESTReader(geoServerConfig.getRESTURL(), geoServerConfig.getRESTUSER(),geoServerConfig.getRESTPW());
		GeoServerRESTPublisher publisher = new GeoServerRESTPublisher(geoServerConfig.getRESTURL(), geoServerConfig.getRESTUSER(),geoServerConfig.getRESTPW());
		
		TemporaryFileManager.setReader(reader);
		TemporaryFileManager.setPublisher(publisher);
		TemporaryFileManager.setGeoServerConfig(geoServerConfig);
		
		
		if (!reader.existGeoserver()){
			Messages.setMessage(Messages._GEOSERVER_NOT_EXIST);
			return false;
		}

		if (reader.getLayer(geoServerConfig.getGsPotentialLayer()+"_"+session.getId())!=null){			
			boolean ftRemoved = publisher.unpublishFeatureType(geoServerConfig.getGsWorkspace(), geoServerConfig.getGsPotentialDatastore()+"_"+session.getId(),geoServerConfig.getGsPotentialLayer()+"_"+session.getId());
			publisher.removeLayer(geoServerConfig.getGsWorkspace(), geoServerConfig.getGsPotentialLayer()+"_"+session.getId());
			boolean dsRemoved = publisher.removeDatastore(geoServerConfig.getGsWorkspace(), geoServerConfig.getGsPotentialDatastore()+"_"+session.getId(), true);
		}

		String zipfileName = newFile.getAbsolutePath().substring(0, newFile.getAbsolutePath().lastIndexOf("."))+".zip";
		Zip zip = new Zip(zipfileName,newFile.getParentFile().getAbsolutePath());			
		zip.createZip();
		
		File zipFile = new File(zipfileName);
		boolean published = publisher.publishShp(geoServerConfig.getGsWorkspace(),
				geoServerConfig.getGsPotentialDatastore()+"_"+session.getId(),
				geoServerConfig.getGsPotentialLayer()+"_"+session.getId(), zipFile, "EPSG:28355",geoServerConfig.getGsPotentialStyle());
		if (!published){
			Messages.setMessage(Messages._PUBLISH_FAILED);
			return false;
		}
		GSLayerEncoder le = new GSLayerEncoder();
		le.setDefaultStyle(geoServerConfig.getGsPotentialStyle());
		publisher.configureLayer(geoServerConfig.getGsWorkspace(), geoServerConfig.getGsPotentialLayer()+"_"+session.getId(), le);
		System.out.println("Publishsed Success");

		return true;
	}
	


	//*************************   ***************************

	private void landUseFilters(Filter filter) throws IOException {
		Query zoneCodeQuery = new Query();
		zoneCodeQuery.setPropertyNames(new String[] { inputLayersConfig.getZonecodes_zoneCode(), inputLayersConfig.getZonecodes_group1()});
		zoneCodeQuery.setFilter(filter);
		SimpleFeatureCollection zoneCodes = zonecodesFc.getFeatures(zoneCodeQuery);
		SimpleFeatureIterator it = zoneCodes.features();
		List<Filter> match = new ArrayList<Filter>();
		while (it.hasNext()) {
			SimpleFeature zoneCode = it.next();
			Object value = zoneCode.getAttribute(inputLayersConfig.getZonecodes_zoneCode());
			filter = ff.equals(ff.property(inputLayersConfig.getProperty_zoning()), ff.literal(value));
			match.add(filter);
		}
		System.out.println(match.size());
		System.out.println(ff);
		it.close();
		Filter filterRES = ff.or(match);
		landUseFilters.add(filterRES);
	}

	private SimpleFeatureCollection overlayCollection(Filter filter) throws IOException {
		Query codeListQuery = new Query();
		codeListQuery.setPropertyNames(new String[] { inputLayersConfig.getPlanCodes_zoneCode(), inputLayersConfig.getPlanCodes_group1() });
		codeListQuery.setFilter(filter);
		SimpleFeatureCollection codeList = planCodeListFc.getFeatures(codeListQuery);

		System.out.println("codeList.size()===  "+codeList.size()); 

		SimpleFeatureIterator it = codeList.features();
		List<Filter> match = new ArrayList<Filter>();
		while (it.hasNext()) {
			SimpleFeature zoneCode = it.next();
			Object value = zoneCode.getAttribute(inputLayersConfig.getPlanOverlay_zoneCode());
			filter = ff.equals(ff.property(inputLayersConfig.getPlanOverlay_zoneCode()), ff.literal(value));
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

	public ParameterDevelopPotential getParameter() {
		return parameter;
	}

	public void setParameter(ParameterDevelopPotential parameter) {
		this.parameter = parameter;
	}

	public Geometry getBufferAllParams() {
		return bufferAllParams;
	}

	public void setBufferAllParams(Geometry bufferAllParams) {
		this.bufferAllParams = bufferAllParams;
	}
	
	public ReferencedEnvelope getEnvelope() {
		return envelope;
	}

	public void setEnvelope(ReferencedEnvelope envelope) {
		this.envelope = envelope;
	}

	public GeoServerConfig getGeoServerConfig() {
		return geoServerConfig;
	}
	public void setGeoServerConfig(GeoServerConfig geoServerConfig) {
		this.geoServerConfig = geoServerConfig;
	}

	public String getLayerName() {
		return layerName;
	}
	public void setLayerName(String layerName) {
		this.layerName = layerName;
	}

	
}




///////////////////// 
//		int startIndex =1;
//		int maxFeatures = 10000;	
//		boolean continuetoget=true;
//		QueryCapabilities caps = propertyFc.getQueryCapabilities();
//		if (caps.isOffsetSupported()){
//			LOGGER.info("Supportes");
//		}
//		do{
//			properties = (DefaultFeatureCollection) FeatureCollections.newCollection();
//			SimpleFeatureCollection returned_simpleFeatureCollection = null;
//			propertyQuery.setStartIndex(startIndex);
//			propertyQuery.setMaxFeatures(maxFeatures); 
//			
////			propertyQuery.seto
//			
//			propertyFc.getQueryCapabilities();
//			
////			propertyQuery.setCoordinateSystem( CRS.decode("EPSG:4283") ); // FROM
////			propertyQuery.setCoordinateSystemReproject( CRS.decode("EPSG:28355") ); // TO
//			returned_simpleFeatureCollection= propertyFc.getFeatures(propertyQuery);
//			LOGGER.info("size= "+returned_simpleFeatureCollection.size());
//			if (returned_simpleFeatureCollection == null
//					|| returned_simpleFeatureCollection.size() == 0)
//				continuetoget = false;
//			else {
//				properties.addAll(returned_simpleFeatureCollection);
//				LOGGER.info("size= "+properties.size());
//				if (properties != null && !properties.isEmpty()) {
//					this.overlayFilter();
//				}
//
//			}
//			startIndex = startIndex + maxFeatures;
//		}while (continuetoget);
///////////////////// 
//		int propertyNumber = propertyFc.getCount(propertyQuery);
//		LOGGER.info("propertyNumber.toString()"+propertyNumber); 
//		while (startIndex < propertyNumber){
//			properties = (DefaultFeatureCollection) FeatureCollections.newCollection();
//			propertyQuery.setStartIndex(startIndex);
//			propertyQuery.setMaxFeatures(maxFeatures); 
////			propertyQuery.setCoordinateSystem( CRS.decode("EPSG:4283") ); // FROM
////			propertyQuery.setCoordinateSystemReproject( CRS.decode("EPSG:28355") ); // TO				
//			properties.addAll(propertyFc.getFeatures(propertyQuery));
//			if (properties != null && !properties.isEmpty()){
//				
//				
//				this.overlayFilter();
//			}
//			startIndex = startIndex + maxFeatures;
//		}



//propertyQuery.setPropertyNames(new String[]{geomName, "objectid", "pfi", "lga_name", "lga_code", "propertyname", "suburb",
//"postcode", "zoning", "sv_current_year", "civ_current_year"});
