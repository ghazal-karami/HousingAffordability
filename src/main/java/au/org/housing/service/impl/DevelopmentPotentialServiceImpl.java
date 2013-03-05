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
import au.org.housing.exception.HousingException;
import au.org.housing.exception.Messages;
import au.org.housing.model.ParameterDevelopPotential;
import au.org.housing.service.DevelopmentPotentialService;
import au.org.housing.service.ExportService;
import au.org.housing.service.FeatureBuilder;
import au.org.housing.service.GeoServerService;
import au.org.housing.service.PostGISService;
import au.org.housing.service.UnionService;
import au.org.housing.service.ValidationService;
import au.org.housing.utilities.TemporaryFileManager;
import au.org.housing.utilities.Zip;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.TopologyException;

/**
 * Implementation for handling Potential Development analysis.
 *
 * @author Gh.Karami
 * @version 1.0
 *
 */ 

@Service
public class DevelopmentPotentialServiceImpl implements DevelopmentPotentialService {

	private static final Logger LOGGER = LoggerFactory.getLogger(DevelopmentPotentialServiceImpl.class);

	@Autowired	ParameterDevelopPotential parameter;
	@Autowired	 PostGISService postGISService;
	@Autowired	 GeoServerConfig geoServerConfig;
	@Autowired	private ValidationService validationService;
	@Autowired	private ExportService exportService;
	@Autowired	UnionService unionService;
	@Autowired 	FeatureBuilder featureBuilder;
	@Autowired	InputLayersConfig inputLayersConfig;
	@Autowired	OutPutLayerConfig outPutLayerConfig;
	@Autowired	GeoServerService geoServerService;

	SimpleFeatureSource propertyFc = null;
	SimpleFeatureSource planCodeListFc = null;
	SimpleFeatureSource planOverlayFc = null;
	SimpleFeatureSource zonecodesFc = null;

	FilterFactory2 ff;
	Filter propertyFilter;
	Filter lgaFilter;
	Query propertyQuery ;
	SimpleFeatureTypeBuilder stb;
	SimpleFeatureBuilder sfb;
	SimpleFeatureType newFeatureType;
//	ReferencedEnvelope envelope;
	//	String layerName;
	Filter ownershipFilter;
	Filter landUseFilter;
	Map<String, Object> overlayMap;
	List<Filter> propertyFilters;
	List<Filter> landUseFilters;
	Geometry bufferAllParams = null;
	SimpleFeatureCollection properties;
	boolean dropCreateSchema ;	
	Geometry inundationsUnion = null;
	Integer propertyOverlaysNum;	
	boolean anyOverlayChecked ;
	SimpleFeatureCollection planCodeList = null;
	SimpleFeatureCollection tblZoneCodes = null;
	File newFile;
	Map<String, Object> outputLayer;	  	
	 

	public boolean analyse(String username, HttpSession session) throws SQLException, Exception{
		layersValidation();
		propertyOverlaysNum = -1;		
		dropCreateSchema = true;
		propertyFilters = new ArrayList<Filter>();
		propertyFilter = null;	
		ownershipFilter = null;
		landUseFilter = null;
		ff = CommonFactoryFinder.getFilterFactory2(GeoTools.getDefaultHints());		

		outputLayer = new HashMap<String, Object>(); 
		String workspace = geoServerConfig.getGsWorkspace() + "_" + username;
//		String dataStore = geoServerConfig.getGsPotentialDatastore();
		String dataStore = geoServerConfig.getGsDataStore();
		String layer = geoServerConfig.getGsPotentialLayer();
		String style = geoServerConfig.getGsPotentialStyle();
		File newDirectory =  TemporaryFileManager.getNew(session, username,  layer , "",true);
		newFile = new File(newDirectory.getAbsolutePath()+"/"+ layer + ".shp");
		System.out.println(newFile.toURI());

		this.lgaFilter();
		this.dpiFilter();
		this.landUseFilter();
		this.bufferAllParamsFilter();
		this.ownershipFilter();		
		this.propertyFilter();
		if (!this.generateQuery(session)){
			return false;
		}		
		this.overlayCollection();
		if (!this.overlayIntersection(username, layer, session)){
			return false;
		}

		geoServerService.getGeoServer(workspace);
		geoServerService.publishToGeoServer( workspace , dataStore , layer, style, newFile );	

		outputLayer.put("workspace", workspace);
		outputLayer.put("layerName", layer);		

		return true;
	}

	private void layersValidation() throws IOException, PSQLException, HousingException {
		propertyFc = postGISService.getFeatureSource(inputLayersConfig.getProperty());
		planCodeListFc = postGISService.getFeatureSource(inputLayersConfig.getPlanCodes());
		planOverlayFc = postGISService.getFeatureSource(inputLayersConfig.getPlanOverlay());
		LOGGER.info(planOverlayFc.getSchema().getCoordinateReferenceSystem().toString());
		zonecodesFc =  postGISService.getFeatureSource(inputLayersConfig.getZonecodesTbl());
		validationService.propertyValidated(propertyFc, inputLayersConfig.getProperty()) ;
		validationService.isPolygon(propertyFc, inputLayersConfig.getProperty()) ;
		validationService.isMetric(propertyFc, inputLayersConfig.getProperty());
		validationService.planOverlayValidated(planOverlayFc, inputLayersConfig.getPlanOverlay()) ;
		validationService.isMetric(planOverlayFc, inputLayersConfig.getPlanOverlay()) ;
		validationService.isPolygon(planOverlayFc, inputLayersConfig.getPlanOverlay()) ;
		validationService.planCodeListValidated(planCodeListFc, inputLayersConfig.getPlanCodes()) ;
		validationService.zonecodesValidated(zonecodesFc, inputLayersConfig.getZonecodesTbl()) ;		
	}

	private void lgaFilter() throws Exception{	
		try{
			List<Filter> lgaFilters = new ArrayList<Filter>();
			for (String lga_code : parameter.getSelectedLGAs()) {
				Filter filter = ff.equals(ff.property(inputLayersConfig.getPropertyLgaCode()),ff.literal(lga_code));
				lgaFilters.add(filter);
			}
			Filter lgaFilter = ff.or(lgaFilters);
			propertyFilters.add(lgaFilter);			
		}catch(Exception e){
			e.printStackTrace();
			throw new Exception(Messages._ERROR_LGA_FILTER);			
		}
	}	

	private void dpiFilter() throws Exception{
		try{
			if (parameter.getDpi() != 0) {
				Divide divide = ff.divide(ff.property(inputLayersConfig.getProperty_svCurrentYear()),
						ff.property(inputLayersConfig.getProperty_civCurrentYear()));
				String operator = parameter.getDpiOperateorVal();
				Filter filterDPI = null;
				if (operator.equals(">")){
					filterDPI =  ff.greater( divide, ff.literal(parameter.getDpi()) );
				}else if (operator.equals(">=")){
					filterDPI =  ff.greaterOrEqual( divide, ff.literal(parameter.getDpi()) );
				}else if (operator.equals("=")){
					Filter filterGreater =  ff.greater( divide, ff.literal(parameter.getDpi()) );
					Filter filterEquals =  ff.equals( divide, ff.literal(parameter.getDpi()) );					
					Filter filterGreaterOrEqual = ff.or( filterGreater, filterEquals );
					Filter filterLess =  ff.less( divide, ff.literal(parameter.getDpi()+0.1) );
					filterDPI = ff.and( filterGreaterOrEqual, filterLess );
				}else if (operator.equals("<")){
					filterDPI =  ff.less( divide, ff.literal(parameter.getDpi()+0.1) );
				}else if (operator.equals("<=")){
					filterDPI =  ff.lessOrEqual( divide, ff.literal(parameter.getDpi()+0.1) );
				}
				propertyFilters.add(filterDPI);		

			}
		}catch(Exception e){
			e.printStackTrace();
			throw new Exception(Messages._ERROR_DPI_FILTER);			
		}
	}

	private void landUseFilter() throws Exception{
		try{
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
		}catch(Exception e){
			e.printStackTrace();
			throw new Exception(Messages._ERROR_LANDUSE_FILTER);			
		}
	}

	private void bufferAllParamsFilter() throws Exception{
		try{
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
			}
		}catch(Exception e){
			e.printStackTrace();
			throw new Exception(Messages._ERROR_TRANSPORT_FACILITY_FILTER);			
		}
	}

	private void ownershipFilter() throws Exception{
		try{
			List<Filter> ownershipFilters= new ArrayList<Filter>();
			Filter filter = null;
			Geometry publicAcquisitionsUnion;
			SimpleFeatureCollection publicAcquisitions = null;
			if (parameter.getPublicAcquision()) {
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
		}catch(Exception e){
			e.printStackTrace();
			throw new Exception(Messages._ERROR_OWNERSHIP_FILTER);			
		}
	}

	private void overlayCollection() throws IOException, HousingException{
		Filter filter = null;
		overlayMap = new HashMap<String, Object>();
		anyOverlayChecked = false;

		//************ FLOODWAY OVERLAY ************	
		Geometry floodwaysUnion = null;
		if (parameter.getFloodway()) {			 
			filter = ff.equals(ff.property(inputLayersConfig.getPlanCodes_group1()),ff.literal("FLOODWAY OVERLAY"));
			SimpleFeatureCollection floodways = overlayCollection(filter);
			if ( floodways != null ){
				floodwaysUnion = (Geometry) unionService.createUnion(floodways);
				floodwaysUnion = floodwaysUnion.buffer(0.001);
				overlayMap.put("floodways", floodwaysUnion);
				System.out.println("floodway size==="+floodways.size());
			}else{
				overlayMap.put("floodways", null);
			}
			stb.add("OL_Floodway", Boolean.class); 
			anyOverlayChecked = true;
		}
		//************ LAND SUBJECT TO INUNDATION OVERLAY ************
		//		Geometry inundationsUnion = null; //????
		if (parameter.getInundation()) {		 
			filter = ff.equals(ff.property(inputLayersConfig.getPlanCodes_group1()),ff.literal("LAND SUBJECT TO INUNDATION OVERLAY"));
			SimpleFeatureCollection inundations = overlayCollection(filter);
			if ( inundations != null ){
				inundationsUnion = unionService.createUnion(inundations) ; 
				inundationsUnion = inundationsUnion.buffer(0.001);
				overlayMap.put("inundations", inundationsUnion);
				System.out.println("inundations size==="+inundations.size());
			}else{
				overlayMap.put("inundations", null);
			}
			stb.add("OL_Inundation", Boolean.class); 
			anyOverlayChecked = true;
		}
		//************ NEIGHBOURHOOD CHARACTER OVERLAY ************
		Geometry neighborhoodsUnion = null;		
		if (parameter.getNeighborhood()) {		 
			filter = ff.equals(ff.property(inputLayersConfig.getPlanCodes_group1()),ff.literal("NEIGHBOURHOOD CHARACTER OVERLAY"));
			SimpleFeatureCollection neighborhoods = overlayCollection(filter);
			if ( neighborhoods != null && !neighborhoods.isEmpty()){
				neighborhoodsUnion = (Geometry) unionService.createUnion(neighborhoods);
				neighborhoodsUnion = neighborhoodsUnion.buffer(0.001);
				overlayMap.put("neighborhoods", neighborhoodsUnion);
				System.out.println("neighborhoods size==="+neighborhoods.size());
			}else{
				overlayMap.put("neighborhoods", null);
			}
			stb.add("OL_Neighborhood", Boolean.class);
			anyOverlayChecked = true;
		}

		//************ DESIGN AND DEVELOPMENT OVERLAY ************
		Geometry designDevelopmentsUnion = null;		
		if (parameter.getDesignDevelopment()) {			 
			filter = ff.equals(ff.property(inputLayersConfig.getPlanCodes_group1()),ff.literal("DESIGN AND DEVELOPMENT OVERLAY"));
			SimpleFeatureCollection designDevelopments = overlayCollection(filter);
			if ( designDevelopments != null && !designDevelopments.isEmpty()){
				designDevelopmentsUnion = (Geometry) unionService.createUnion(designDevelopments);
				designDevelopmentsUnion = designDevelopmentsUnion.buffer(0.001);
				overlayMap.put("designDevelopments", designDevelopmentsUnion);
				System.out.println("designDevelopments size==="+designDevelopments.size());	
			}else{
				overlayMap.put("designDevelopments", null);
			}
			stb.add("OL_DesignDevelopment", Boolean.class);
			anyOverlayChecked = true;
		}
		//************ DESIGN AND DEVELOPMENT OVERLAY ************
		Geometry developPlansUnion = null;		
		if (parameter.getDevelopPlan()) {		 
			filter = ff.equals(ff.property(inputLayersConfig.getPlanCodes_group1()),ff.literal("DEVELOPMENT PLAN OVERLAY"));
			SimpleFeatureCollection developPlans = overlayCollection(filter);
			if ( developPlans != null && !developPlans.isEmpty()){
				developPlansUnion = (Geometry) unionService.createUnion(developPlans);
				developPlansUnion = developPlansUnion.buffer(0.001);
				overlayMap.put("developPlans", developPlansUnion);
				System.out.println("developPlans size==="+developPlans.size());	
			}else{
				overlayMap.put("developPlans", null);
			}
			stb.add("OL_DevelopmentPlan", Boolean.class); 
			anyOverlayChecked = true;
		}
		// ************ PARKING OVERLAY ************
		Geometry parkingsUnion = null;		
		if (parameter.getParking() ) {		 
			filter = ff.equals(ff.property(inputLayersConfig.getPlanCodes_group1()),ff.literal("PARKING OVERLAY"));
			SimpleFeatureCollection parkings = overlayCollection(filter);
			if ( parkings != null && !parkings.isEmpty() ){
				parkingsUnion = (Geometry) unionService.createUnion(parkings);
				parkingsUnion = parkingsUnion.buffer(0.001);
				overlayMap.put("parkings", parkingsUnion);
				System.out.println("parkings size==="+parkings.size());	
			}else{
				overlayMap.put("parkings", null);
			}
			stb.add("OL_Parking", Boolean.class);
			anyOverlayChecked = true;
		}
		// ************ BUSHFIRE MANAGEMENT OVERLAY ************
		Geometry bushfiresUnion = null;	
		if (parameter.getBushfire() ) {	 
			Filter bushfilter1 = ff.equals(ff.property(inputLayersConfig.getPlanCodes_group1()),ff.literal("BUSHFIRE MANAGEMENT OVERLAY"));
			Filter bushfilter2 = ff.equals(ff.property(inputLayersConfig.getPlanCodes_group1()),ff.literal("WILDFIRE MANAGEMENT OVERLAY"));
			filter = ff.or(bushfilter1, bushfilter2);
			SimpleFeatureCollection bushfires = overlayCollection(filter);
			if ( bushfires != null && !bushfires.isEmpty() ){
				bushfiresUnion = (Geometry) unionService.createUnion(bushfires);
				bushfiresUnion = bushfiresUnion.buffer(0.001);
				overlayMap.put("bushfires", bushfiresUnion);
				System.out.println("bushfires size==="+bushfires.size());
			}else{
				overlayMap.put("bushfires", null);
			}
			stb.add("OL_Bushfire", Boolean.class);
			anyOverlayChecked = true;
		}
		// ************ EROSION MANAGEMENT OVERLAY ************
		Geometry erosionsUnion = null;	
		if (parameter.getErosion() ) {	 
			filter = ff.equals(ff.property(inputLayersConfig.getPlanCodes_group1()),ff.literal("EROSION MANAGEMENT OVERLAY"));
			SimpleFeatureCollection erosions = overlayCollection(filter);
			if ( erosions != null && !erosions.isEmpty()){
				erosionsUnion = (Geometry) unionService.createUnion(erosions);
				erosionsUnion = erosionsUnion.buffer(0.001);
				overlayMap.put("erosions", erosionsUnion);
				System.out.println("erosions size==="+erosions.size());	
			}else{
				overlayMap.put("erosions", null);
			}
			stb.add("OL_Erosion", Boolean.class);
			anyOverlayChecked = true;
		}
		// ************ VEGETATION PROTECTION OVERLAY ************
		Geometry vegprotectionsUnion = null;	
		if (parameter.getVegprotection()) {
			filter = ff.equals(ff.property(inputLayersConfig.getPlanCodes_group1()),ff.literal("VEGETATION PROTECTION OVERLAY"));
			SimpleFeatureCollection vegprotections = overlayCollection(filter);
			if ( vegprotections != null && !vegprotections.isEmpty()){
				vegprotectionsUnion = (Geometry) unionService.createUnion(vegprotections);
				vegprotectionsUnion = vegprotectionsUnion.buffer(0.001);
				overlayMap.put("vegprotections", vegprotectionsUnion);
				System.out.println("vegprotections size==="+vegprotections.size());	
			}else{
				overlayMap.put("vegprotections", null);
			}
			stb.add("OL_VegProtection", Boolean.class);
			anyOverlayChecked = true;
		}
		// ************ SALINITY MANAGEMENT OVERLAY ************
		Geometry salinitysUnion = null;
		if (parameter.getSalinity()) {	 
			filter = ff.equals(ff.property(inputLayersConfig.getPlanCodes_group1()),ff.literal("SALINITY MANAGEMENT OVERLAY"));
			SimpleFeatureCollection salinitys = overlayCollection(filter);
			if ( salinitys != null && !salinitys.isEmpty()){
				salinitysUnion = (Geometry) unionService.createUnion(salinitys);
				salinitysUnion = salinitysUnion.buffer(0.001);
				overlayMap.put("salinitys", salinitysUnion);
				System.out.println("salinitys size==="+salinitys.size());	
			}else{
				overlayMap.put("salinitys", null);
			}
			stb.add("OL_Salinity", Boolean.class);
			anyOverlayChecked = true;
		}
		// ************ CONTAMINATION MANAGEMENT OVERLAY ************
		Geometry contaminationsUnion = null;
		if (parameter.getContamination()) {
			filter = ff.equals(ff.property(inputLayersConfig.getPlanCodes_group1()),ff.literal("POTENTIALLY CONTAMINATED LAND OVERLAY"));
			SimpleFeatureCollection contaminations = overlayCollection(filter);
			if ( contaminations != null && !contaminations.isEmpty()){
				contaminationsUnion = (Geometry) unionService.createUnion(contaminations);
				contaminationsUnion = contaminationsUnion.buffer(0.001);
				overlayMap.put("contaminations", contaminationsUnion);
				System.out.println("contaminations size==="+contaminations.size());	
			}else{
				overlayMap.put("contaminations", null);
			}
			stb.add("OL_Contamination", Boolean.class);
			anyOverlayChecked = true;
		}
		// ************ ENVIRONMENTAL SIGNIFICANCE OVERLAY ************
		Geometry envSignificancesUnion = null;
		if (parameter.getEnvSignificance()) {
			filter = ff.equals(ff.property(inputLayersConfig.getPlanCodes_group1()),ff.literal("ENVIRONMENTAL SIGNIFICANCE OVERLAY"));
			SimpleFeatureCollection envSignificances = overlayCollection(filter);
			if ( envSignificances != null && !envSignificances.isEmpty()){
				envSignificancesUnion = (Geometry) unionService.createUnion(envSignificances);
				envSignificancesUnion = envSignificancesUnion.buffer(0.001);
				overlayMap.put("envSignificances", envSignificancesUnion);
				System.out.println("envSignificances size==="+envSignificances.size());	
			}else{
				overlayMap.put("envSignificances", null);
			}
			stb.add("OL_EnvSignificance", Boolean.class);
			anyOverlayChecked = true;
		}
		// ************ ENVIRONMENTAL AUDIT OVERLAY ************
		Geometry envAuditsUnion = null;
		if (parameter.getEnvAudit()) {
			filter = ff.equals(ff.property(inputLayersConfig.getPlanCodes_group1()),ff.literal("ENVIRONMENTAL AUDIT OVERLAY"));//???
			SimpleFeatureCollection envAudits = overlayCollection(filter);
			if ( envAudits != null && !envAudits.isEmpty()){
				envAuditsUnion = (Geometry) unionService.createUnion(envAudits);
				envAuditsUnion = envAuditsUnion.buffer(0.001);
				overlayMap.put("envAudits", envAuditsUnion);
				System.out.println("envAudits size==="+envAudits.size());	
			}else{
				overlayMap.put("envAudits", null);
			}
			stb.add("OL_EnvAudit", Boolean.class);
			anyOverlayChecked = true;
		}
		// ************ HERITAGE OVERLAY ************		
		Geometry heritageUnion = null;
		if (parameter.getHeritage() ) {
			filter = ff.equals(ff.property(inputLayersConfig.getPlanCodes_group1()),ff.literal("HERITAGE OVERLAY"));// ???
			SimpleFeatureCollection heritages = overlayCollection(filter);
			if (heritages != null && !heritages.isEmpty()){
				heritageUnion = (Geometry) unionService.createUnion(heritages);
				heritageUnion = heritageUnion.buffer(0.001);
				overlayMap.put("heritages", heritageUnion);
				System.out.println("heritages size==="+heritages.size());
			}else{
				overlayMap.put("heritages", null);
			}
			stb.add("OL_Heritage", Boolean.class);
			anyOverlayChecked = true;
		}
	}

	private void propertyFilter() throws IOException, HousingException{
		try{
			if (propertyFilters == null || propertyFilters.isEmpty()){
				throw new HousingException(Messages._SELECT_PARAM_TO_CONTINUE);
			}
			if (!propertyFilters.isEmpty() ) {
				if (landUseFilter != null){
					propertyFilters.add(landUseFilter);				
				}
				if (ownershipFilter != null){
					propertyFilters.add(ownershipFilter);				
				}
				propertyFilter = ff.and(propertyFilters);
			}	
		}catch(Exception e){
			e.printStackTrace();
			throw new HousingException(Messages._ERROR_PROPERTY_FILTER);
		}
	}

	private boolean generateQuery(HttpSession session) throws SQLException, Exception{
		propertyQuery = new Query();
		String geom_name = propertyFc.getSchema().getGeometryDescriptor().getLocalName();
		String[] attributes = {geom_name, outPutLayerConfig.getObjectid(), outPutLayerConfig.getPfi(), outPutLayerConfig.getLgaName()
				, outPutLayerConfig.getStreet_name(), outPutLayerConfig.getStreet_type(), outPutLayerConfig.getSuburb()
				, outPutLayerConfig.getPostcode(), outPutLayerConfig.getLand_area(), outPutLayerConfig.getAreameasure() };
		propertyQuery.setPropertyNames(attributes);
		propertyQuery.setFilter(propertyFilter);			
		properties= propertyFc.getFeatures(propertyQuery);
		ReferencedEnvelope envelope = properties.getBounds();
		outputLayer.put("maxX", envelope.getMaxX());
		outputLayer.put("minX", envelope.getMinX());
		outputLayer.put("maxY", envelope.getMaxY());
		outputLayer.put("minY", envelope.getMinY());
		
		if (properties == null || properties.isEmpty()){
			LOGGER.info("No Properties Found!");
			throw new HousingException(Messages._NO_FEATURE);
		}
		System.out.println("size= "+properties.size());			
		stb = featureBuilder.createFeatureTypeBuilder(properties.getSchema(), "OutPut");	
		stb.add("OverlaysNum", Integer.class);
		return true;
	}

	private boolean overlayIntersection(String username, String layer, HttpSession session) throws NoSuchAuthorityCodeException, IOException, FactoryException, SQLException, Exception{
		newFeatureType = stb.buildFeatureType();
		sfb = new SimpleFeatureBuilder(newFeatureType);
		List<SimpleFeature> newList = new ArrayList<SimpleFeature>();
		SimpleFeatureIterator propertyIt = null;
		propertyIt = properties.features();
		LOGGER.info("properties.size()=="+String.valueOf(properties.size()));
		/*  SimpleFeatureCollection featureCollectionNew = FeatureCollections.newCollection();			*/
		try {
			int i = 0;
			while (propertyIt.hasNext()) {
				propertyOverlaysNum = -1;
				SimpleFeature sf = propertyIt.next();
				sfb.addAll(sf.getAttributes());
				Geometry propertyGeom = (Geometry) sf.getDefaultGeometry();
				System.out.println("  prooperty layer pfi  == "+ sf.getAttribute("pfi"));			

				if (anyOverlayChecked){
					propertyOverlaysNum = 0;
					// **************** Intersections ****************
					Geometry floodwaysUnion = (Geometry) overlayMap.get("floodways");
					if (floodwaysUnion != null) {
						if (floodwaysUnion.intersects(propertyGeom)) {
							sfb.set("OL_Floodway", Boolean.TRUE);
							propertyOverlaysNum++;					
						}									
					}		
					if (inundationsUnion != null) {		
						if (inundationsUnion.intersects(propertyGeom)) {
							sfb.set("OL_Inundation", Boolean.TRUE);	
							propertyOverlaysNum++;
							System.out.println(sf.getAttribute("pfi")+"  have intersection  with OL_Inundation  == ");							
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
					SimpleFeatureCollection featureCollectionNew = new ListFeatureCollection(newFeatureType, newList);						 
					exportService.featuresExportToShapeFile(sfb.getFeatureType(), featureCollectionNew,newFile, dropCreateSchema);
					//						newList = new ArrayList<SimpleFeature>();
					newList.clear();
					i = 0;
					dropCreateSchema = false;
				}				
			}
			if (!newList.isEmpty()){
				System.out.println("properties.size() < 1000");
				SimpleFeatureCollection featureCollectionNew = new ListFeatureCollection(newFeatureType, newList);
				exportService.featuresExportToShapeFile(sfb.getFeatureType(), featureCollectionNew,newFile, dropCreateSchema);
			}	
		}catch(Exception e){
			e.printStackTrace();
			throw new HousingException(Messages._ERROR_OVERLAY_INTERSECTION);
		} finally {
			propertyIt.close();
		}
		return true;
	}


	//*************************  landUseFilters ***************************

	private void landUseFilters(Filter filter) throws IOException, HousingException {
		SimpleFeatureIterator it = null;
		try{
			Query zoneCodeQuery = new Query();
			zoneCodeQuery.setPropertyNames(new String[] { inputLayersConfig.getZonecodes_zoneCode(), inputLayersConfig.getZonecodes_group1()});
			zoneCodeQuery.setFilter(filter);
			tblZoneCodes = zonecodesFc.getFeatures(zoneCodeQuery);
			it = tblZoneCodes.features();
			List<Filter> match = new ArrayList<Filter>();
			while (it.hasNext()) {
				SimpleFeature zoneCode = it.next();
				Object value = zoneCode.getAttribute(inputLayersConfig.getZonecodes_zoneCode());
				filter = ff.equals(ff.property(inputLayersConfig.getProperty_zoning()), ff.literal(value));
				match.add(filter);
			}
			System.out.println(match.size());			
			Filter filterRES = ff.or(match);
			landUseFilters.add(filterRES);
		}catch(Exception e){
			e.printStackTrace();
			throw new HousingException(Messages._ERROR_LANDUSE_FILTER);
		}finally{
			it.close();
		}
	}

	//************************* overlayCollection  ***************************
	private SimpleFeatureCollection overlayCollection(Filter filter) throws IOException, HousingException {
		SimpleFeatureCollection overlays = null;
		SimpleFeatureIterator it = null;
		try{
			Query codeListQuery = new Query();
			codeListQuery.setPropertyNames(new String[] { inputLayersConfig.getPlanCodes_zoneCode(), inputLayersConfig.getPlanCodes_group1() });
			codeListQuery.setFilter(filter);
			planCodeList = planCodeListFc.getFeatures(codeListQuery);
			System.out.println("planCodeList.size()===  "+planCodeList.size());
			it = planCodeList.features();
			List<Filter> match = new ArrayList<Filter>();
			while (it.hasNext()) {
				SimpleFeature zoneCode = it.next();
				Object value = zoneCode.getAttribute(inputLayersConfig.getPlanOverlay_zoneCode());
				filter = ff.equals(ff.property(inputLayersConfig.getPlanOverlay_zoneCode()), ff.literal(value));
				match.add(filter);
			}
			Filter filterOverlay = ff.or(match);
			Query overlayQuery = new Query();
			overlayQuery.setFilter(filterOverlay);
			overlays = planOverlayFc.getFeatures(overlayQuery);	
			System.out.println("codeoverlaysList.size()===  "+ overlays.size());
		}catch(Exception e){
			e.printStackTrace();
			throw new HousingException(Messages._ERROR_OVERLAY_FEATURELIST);
		}finally{
			it.close();
		}
		return overlays;
	}

	//************************* Getter Setter  ***************************
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

	public Map<String, Object> getOutputLayer() {
		return outputLayer;
	}

	//	if (propertyFilter != null) {
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
	
	
//	filter = ff.equals(ff.property("pfi"),ff.literal("5026012"));//
		//			filter = ff.equals(ff.property("pfi"),ff.literal("3196232"));//public acuisition
		//			filter = ff.equals(ff.property("pfi"),ff.literal("3539423"));//inundation
//		propertyFilters.add(filter);
}

