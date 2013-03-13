package au.org.housing.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.GeoTools;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.ows.ServiceException;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.postgresql.util.PSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import au.org.housing.config.GeoServerConfig;
import au.org.housing.config.InputLayersConfig;
import au.org.housing.config.OutPutLayerConfig;
import au.org.housing.exception.HousingException;
import au.org.housing.exception.Messages;
import au.org.housing.model.ParameterDevelopAssessment;
import au.org.housing.service.DevelpmentAssessmentService;
import au.org.housing.service.ExportService;
import au.org.housing.service.FeatureBuilder;
import au.org.housing.service.GeoServerService;
import au.org.housing.service.PostGISService;
import au.org.housing.service.ValidationService;
import au.org.housing.utilities.TemporaryFileManager;

import com.vividsolutions.jts.geom.Geometry;

/**
 * Implementation for handling Assessment Development analysis.
 *
 * @author Gh.Karami
 * @version 1.0
 *
 */ 

@Service
@Scope("session")	
public class DevelpmentAssessmentServiceImpl implements DevelpmentAssessmentService {

	private static final Logger LOGGER = LoggerFactory.getLogger(DevelpmentAssessmentServiceImpl.class);

	@Autowired ParameterDevelopAssessment parameter;
	@Autowired	PostGISService postGISService;
	@Autowired	GeoServerService geoServerService;
	@Autowired GeoServerConfig geoServerConfig;
	@Autowired FeatureBuilder featureBuilder;
	@Autowired InputLayersConfig inputLayersConfig;
	@Autowired OutPutLayerConfig outPutLayerConfig;
	@Autowired ExportService exportService;
	@Autowired ValidationService validationService;

	SimpleFeatureSource propertyFc;
	Map<String, Object> outputLayer;	  	
	private FilterFactory2 ff;
	private File newFile;

	public boolean analyse(String username, HttpSession session) throws IOException, FileNotFoundException,
	ServiceException, NoSuchAuthorityCodeException, FactoryException, PSQLException, HousingException, IllegalArgumentException, URISyntaxException {

		ff = CommonFactoryFinder.getFilterFactory2(GeoTools.getDefaultHints());
		outputLayer = new HashMap<String, Object>(); 

		layersValidation();
		SimpleFeatureCollection pparsCollection = pparseFilter();
		SimpleFeatureCollection propertyCollection = propertyFilter(pparsCollection);				

		String workspace = geoServerConfig.getGsWorkspace() + "_" + username;
		//		String dataStore = geoServerConfig.getGsAssessmentDatastore();
		String dataStore = geoServerConfig.getGsDataStore();
		String layer = geoServerConfig.getGsAssessmentLayer();
		String style = geoServerConfig.getGsAssessmentStyle();
		File newDirectory =  TemporaryFileManager.getNew(session, username,  layer , "",true);
		newFile = new File(newDirectory.getAbsolutePath() +"/" + layer + ".shp");

		exportToShapeFile(propertyCollection, username, layer, session);
		geoServerService.getGeoServer(workspace);
		geoServerService.publishAssessmentStyle(style);
		geoServerService.publishToGeoServer( workspace , dataStore , layer, style, newFile );		

		outputLayer.put("layerName", layer);
		outputLayer.put("workspace", workspace);
		return true;
	}

	private void layersValidation() throws IOException, PSQLException, HousingException {
		propertyFc = postGISService.getFeatureSource(inputLayersConfig.getProperty());
		validationService.propertyValidated(propertyFc, inputLayersConfig.getProperty());
		validationService.isPolygon(propertyFc, inputLayersConfig.getProperty());
		validationService.isMetric(propertyFc, inputLayersConfig.getProperty());
	}

	private Filter createFilterBasedOnOperator(String operator, String property, Integer literal){
		Filter filter = null;
		if (operator.equals(">")){
			filter = ff.greater(ff.property(property),ff.literal(literal));
		}else if (operator.equals(">=")){
			filter = ff.greaterOrEqual(ff.property(property),ff.literal(literal));
		}
		else if (operator.equals("=")){
			filter = ff.equals(ff.property(property),ff.literal(literal));
		}
		else if (operator.equals("<")){
			filter = ff.less(ff.property(property),ff.literal(literal));
		}
		else if (operator.equals("<=")){
			filter = ff.lessOrEqual(ff.property(property),ff.literal(literal));
		}
		else if (operator.equals("!=")){
			filter = ff.notEqual(ff.property(property),ff.literal(literal));
		}
		return filter;
	}

	private SimpleFeatureCollection pparseFilter() throws PSQLException, IOException, HousingException{
		SimpleFeatureCollection pparsCollection;
		List<Filter> pparsFilters = new ArrayList<Filter>();
		SimpleFeatureSource pparsFc = postGISService.getFeatureSource(inputLayersConfig.getPpars());
		if (parameter.getDurationAssessment() != 0) {
			Filter filter = createFilterBasedOnOperator(parameter.getDurationAssessmentOperateor(), inputLayersConfig.getPparsDuration(), parameter.getDurationAssessment()); 
			pparsFilters.add(filter);
		}

		if (parameter.getNumOfObjection() != 0) {
			Filter filter = createFilterBasedOnOperator(parameter.getNumOfObjectionOperateor(), inputLayersConfig.getPparsObjections(), parameter.getNumOfObjection());
			pparsFilters.add(filter);
		}

		if (parameter.getFurtherInfo() == 2) {
			Filter filter = createFilterBasedOnOperator("=", inputLayersConfig.getPparsFurtherinfo(), parameter.getFurtherInfo());
			pparsFilters.add(filter);
		}

		if (parameter.getPublicNotice() == 2) {
			Filter filter = createFilterBasedOnOperator("=", inputLayersConfig.getPparsPublicNotice(), parameter.getPublicNotice());
			pparsFilters.add(filter);
		}

		if (parameter.getReferralIssues() == 2) {
			Filter filter = createFilterBasedOnOperator("=", inputLayersConfig.getPparsReferralIssue(), parameter.getReferralIssues());
			pparsFilters.add(filter);
		}

		if (!parameter.getSelectedCategories().isEmpty()) {
			Filter filter = null;
			StringBuilder sb = new StringBuilder();
			int index = 0;
			for (Integer categoryCode : parameter.getSelectedCategories()) {
				sb.append(categoryCode);
				if (++index != parameter.getSelectedCategories().size()) {
					sb.append(",");
				}
				filter = ff.equals(ff.property(inputLayersConfig.getPparsCategory()),
						ff.literal(sb.toString()));
			}
			pparsFilters.add(filter);
		}

		if (parameter.getNumOfDwelling() != 0) {
			Filter filter = createFilterBasedOnOperator(parameter.getNumOfDwellingOperateor(), inputLayersConfig.getPparsNumOfDwelling(), parameter.getNumOfDwelling());
			pparsFilters.add(filter);
		}

		if (parameter.getCurrentUse() == 7) {
			Filter filter = createFilterBasedOnOperator("=", inputLayersConfig.getPparsCurrentUse(), parameter.getCurrentUse());
			pparsFilters.add(filter);
		} else if (parameter.getReferralIssues() == -7) {
			Filter filter = createFilterBasedOnOperator("!=", inputLayersConfig.getPparsCurrentUse(), parameter.getCurrentUse());
			pparsFilters.add(filter);
		}

		if (parameter.getProposedUse() == 7) {
			Filter filter = createFilterBasedOnOperator("=", inputLayersConfig.getPparsProposedUse(), parameter.getProposedUse());
			pparsFilters.add(filter);
		} else if (parameter.getProposedUse() == -7) {
			Filter filter = createFilterBasedOnOperator("!=", inputLayersConfig.getPparsProposedUse(), parameter.getProposedUse());pparsFilters.add(filter);
		}

		if (parameter.getEstimatedCostOfWork() != 0) {
			Filter filter = createFilterBasedOnOperator(parameter.getEstimatedCostOfWorkOperateor(), inputLayersConfig.getPparsEstimatedCostOfWork(), parameter.getEstimatedCostOfWork());
			pparsFilters.add(filter);
		}

		if (parameter.getPreMeeting() == 2) {
			Filter filter = createFilterBasedOnOperator("=", inputLayersConfig.getPparsPreMeeting(), parameter.getPreMeeting());
			pparsFilters.add(filter);
		}

		if (parameter.getSelectedOutcome() != -1) {
			Filter filter = createFilterBasedOnOperator("<", inputLayersConfig.getPparsOutcome(), parameter.getSelectedOutcome());
			pparsFilters.add(filter);
		}

		if (pparsFilters == null || pparsFilters.isEmpty()){
			throw new HousingException(Messages._SELECT_PARAM_TO_CONTINUE);
		}
		Filter pparsFilter = ff.and(pparsFilters);
		pparsCollection = pparsFc.getFeatures(pparsFilter);
		if (pparsCollection == null || pparsCollection.isEmpty()){
			throw new HousingException(Messages._NO_FEATURE);			
		}
		System.out.println("pparsCollection.size()" + pparsCollection.size());
		return pparsCollection;
	}

	private SimpleFeatureCollection propertyFilter(SimpleFeatureCollection pparsCollection) throws PSQLException, IOException, NoSuchAuthorityCodeException, FactoryException, HousingException{
		SimpleFeatureIterator pparsIterator = null;
		SimpleFeatureCollection propertyCollection;
		try{
			propertyFc = postGISService.getFeatureSource(inputLayersConfig.getProperty());		
			List<Filter> lgaFilters = new ArrayList<Filter>();
			Filter lgaFilter = null;
			if (parameter.getSelectedLGAs2()!= null && !parameter.getSelectedLGAs2().isEmpty()){
				for (String lgaCode : parameter.getSelectedLGAs2()) {
					Filter filter = ff.equals(ff.property(inputLayersConfig.getPropertyLgaCode()),ff.literal(lgaCode));
					lgaFilters.add(filter);
				}
				lgaFilter = ff.or(lgaFilters);
			}	
			String geomName = propertyFc.getSchema().getGeometryDescriptor().getLocalName();
			List<Filter> propertyFilters = new ArrayList<Filter>();		
			pparsIterator = pparsCollection.features();
			Filter filter;
			while (pparsIterator.hasNext()) {
				SimpleFeature ppars = pparsIterator.next();
				Geometry pparsGeom = (Geometry) ppars.getDefaultGeometry();
				filter = ff.intersects(ff.property(geomName),ff.literal(pparsGeom)); 
				if (lgaFilter != null){
					filter = ff.and(lgaFilter, filter);
				}
				propertyFilters.add(filter);
			}			
			filter = null;		
			Filter propertyFilter = ff.or(propertyFilters);
			Query propertyQuery = new Query();
			String geom_name = propertyFc.getSchema().getGeometryDescriptor().getLocalName();
			String[] attributes = {geom_name, outPutLayerConfig.getObjectid(), outPutLayerConfig.getPfi(), outPutLayerConfig.getLgaName()
					, outPutLayerConfig.getStreet_name(), outPutLayerConfig.getStreet_type(), outPutLayerConfig.getSuburb()
					, outPutLayerConfig.getPostcode(), outPutLayerConfig.getLand_area(), outPutLayerConfig.getAreameasure() };
			propertyQuery.setPropertyNames(attributes);
			propertyQuery.setFilter(propertyFilter);		
			LOGGER.info("propertyFilter= "+ propertyFilter);
			propertyCollection = propertyFc.getFeatures(propertyQuery);
			LOGGER.info("propertyCollection.size= "+ propertyCollection.size());

			ReferencedEnvelope envelope = propertyCollection.getBounds();
			outputLayer.put("maxX", envelope.getMaxX());
			outputLayer.put("minX", envelope.getMinX());
			outputLayer.put("maxY", envelope.getMaxY());
			outputLayer.put("minY", envelope.getMinY());

			if (propertyCollection == null || propertyCollection.isEmpty()){
				LOGGER.info("No Properties Found!");
				throw new HousingException(Messages._NO_FEATURE);				
			}
		}finally{
			pparsIterator.close();
		}
		return propertyCollection;
	}

	private boolean exportToShapeFile(SimpleFeatureCollection propertyCollection, String username, String layer, HttpSession session) throws NoSuchAuthorityCodeException, IOException, FactoryException, HousingException {
		System.out.println(newFile.toURI());		
		SimpleFeatureTypeBuilder stb = featureBuilder.createFeatureTypeBuilder(propertyCollection.getSchema(), "OutPut");	
		SimpleFeatureType newFeatureType = stb.buildFeatureType();		
		System.out.println("propertyCollection.size 222  = "+ propertyCollection.size());
		if (!exportService.featuresExportToShapeFile(newFeatureType, propertyCollection,newFile, true)){
			throw new HousingException(Messages._EXPORT_TO_SHP_UNSUCCESSFULL);			
		}
		return true;
	}

	public Map<String, Object> getOutputLayer() {
		return outputLayer;
	}
}

