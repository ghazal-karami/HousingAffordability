package au.org.housing.service.impl;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.activation.FileDataSource;

import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.Query;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.GeoTools;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureCollections;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.reflection.Whitebox;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.vividsolutions.jts.geom.Geometry;


import au.org.housing.config.LayersConfig;
import au.org.housing.exception.Messages;
import au.org.housing.model.ParameterDevelopPotential;
import au.org.housing.service.ExportService;
import au.org.housing.service.FeatureBuilder;
import au.org.housing.service.TransportationBufferService;
import au.org.housing.service.UnionService;
import au.org.housing.service.ValidationService;
import au.org.housing.utilities.GeoJSONUtilities;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/springapp-servlet.xml"})
public class PropertyFilterServiceTest {

	@Autowired
	private ParameterDevelopPotential parameter;	

	@Autowired
	private ValidationService validationService;

	@Autowired
	private ExportService exportService;

	@Autowired
	private UnionService unionService;
	
	@Autowired 
	private FeatureBuilder featureBuilder;
	
	@Autowired
	private LayersConfig layerMapping;

	@Autowired
	private TransportationBufferService transportationBufferService;

	PropertyFilterServiceImpl propertyFilterServiceImpl;

	static SimpleFeatureSource propertyFc;
	static SimpleFeatureSource planCodeListFc;
	static SimpleFeatureSource planOverlayFc;
	static SimpleFeatureSource zonecodesFc;

	static FilterFactory2 ff;
	
	SimpleFeatureCollection overlays;


	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
//		propertyFc = Config.getGeoJSONFileFactory().getFeatureSource(MapAttImpl.property);
//		propertyFc = Config.getShapeFileFactory().getFeatureSource(MapAttImpl.property);
//		propertyFc = Config.getWFSFactory().getFeatureSource(MapAttImpl.property);
//		assertNotNull(propertyFc);
//		planCodeListFc = Config.getGeoJSONFileFactory().getFeatureSource(MapAttImpl.planCodes);
//		assertNotNull(planCodeListFc);
//		planOverlayFc = Config.getGeoJSONFileFactory().getFeatureSource(MapAttImpl.planOverlay);	
//		assertNotNull(planOverlayFc);
//		zonecodesFc =  Config.getGeoJSONFileFactory().getFeatureSource(MapAttImpl.zonecodesTbl);
//		assertNotNull(zonecodesFc);
//		ff = CommonFactoryFinder.getFilterFactory2(GeoTools.getDefaultHints());
//		assertNotNull(ff);
	}

	@Before
	public void setup(){
	}

	@Test
	public void layersValidationTest() throws IOException {
		if (!validationService.propertyValidated(propertyFc, layerMapping.getProperty())  || !validationService.isPolygon(propertyFc, layerMapping.getProperty()) || !validationService.isMetric(propertyFc, layerMapping.getProperty()) ||
				!validationService.planOverlayValidated(planOverlayFc, layerMapping.getPlanOverlay()) || !validationService.isMetric(planOverlayFc, layerMapping.getPlanOverlay()) || !validationService.isPolygon(planOverlayFc, layerMapping.getPlanOverlay()) ||
				!validationService.planCodeListValidated(planCodeListFc, layerMapping.getPlanCodes()) ||
				!validationService.zonecodesValidated(zonecodesFc, layerMapping.getZonecodesTbl()) ){
			fail(Messages.getMessage());
		}
		assertTrue(true);
	}

//	@Test
//	public void landUseFiltersTest() throws IOException {
//		List<Filter> landUseFilters = new ArrayList<Filter>();
//		Query zoneCodeQuery = new Query();
//		zoneCodeQuery.setPropertyNames(new String[] { MapAttImpl.zonecodes_zoneCode, MapAttImpl.zonecodes_group1 });
//
//		Filter filter = ff.equals(ff.property(MapAttImpl.zonecodes_group1), ff.literal("RESIDENTIAL"));
//
//		zoneCodeQuery.setFilter(filter);
//		SimpleFeatureCollection zoneCodes = zonecodesFc.getFeatures(zoneCodeQuery);
//		assertNotNull(zoneCodes);
//
//		SimpleFeatureIterator it = zoneCodes.features();
//		assertNotNull(it);
//
//		List<Filter> match = new ArrayList<Filter>();
//		while (it.hasNext()) {
//			SimpleFeature zoneCode = it.next();
//			Object value = zoneCode.getAttribute(MapAttImpl.zonecodes_zoneCode);
//			assertNotNull(value);			
//			filter = ff.equals(ff.property(MapAttImpl.property_zoning), ff.literal(value));
//			match.add(filter);
//		}
//		it.close();
//		System.out.println(match.size());
//		assertNotEquals(match.size(), 0);		
//		Filter filterRES = ff.or(match);
//		landUseFilters.add(filterRES);
//		assertNotEquals(landUseFilters.size(), 0);		
//	}

//	@Test
	private void overlayCollectionTest() throws IOException {
		Query codeListQuery = new Query();
		codeListQuery.setPropertyNames(new String[] { layerMapping.getPlanCodes_zoneCode(), layerMapping.getPlanCodes_group1() });
		Filter filter = ff.equals(ff.property(layerMapping.getPlanCodes_group1()),ff.literal("LAND SUBJECT TO INUNDATION OVERLAY"));
		codeListQuery.setFilter(filter);

		SimpleFeatureCollection codeList = planCodeListFc.getFeatures(codeListQuery);
		System.out.println("plancodeList.size()===  "+codeList.size()); 
		assertNotNull(codeList);

		SimpleFeatureIterator it = codeList.features();
		assertNotNull(it);

		List<Filter> match = new ArrayList<Filter>();
		while (it.hasNext()) {
			SimpleFeature zoneCode = it.next();
			Object value = zoneCode.getAttribute(layerMapping.getPlanOverlay_zoneCode());
			filter = ff.equals(ff.property(layerMapping.getPlanOverlay_zoneCode()), ff.literal(value));
			match.add(filter);
		}
		it.close();
		assertNotEquals(match.size(), 0);		

		Filter filterOverlay = ff.or(match);
		Query overlayQuery = new Query();
		overlayQuery.setFilter(filterOverlay);
		overlays = planOverlayFc.getFeatures(overlayQuery);	

		System.out.println("planOverlays.size()===  "+ overlays.size());	
		assertNotEquals(overlays.size(), 0);
	}

	/*@Test
	public void testCreateFilters() throws Exception {
		propertyFilterServiceImpl = new PropertyFilterServiceImpl();
		Whitebox.setInternalState( propertyFilterServiceImpl, "validationService", validationService );
		Whitebox.setInternalState( propertyFilterServiceImpl, "unionService", unionService );
		Whitebox.setInternalState( propertyFilterServiceImpl, "parameter", parameter );
		Whitebox.setInternalState( propertyFilterServiceImpl, "exportService", exportService );
		Whitebox.setInternalState( propertyFilterServiceImpl, "featureBuilder", featureBuilder );
		
//		parameter.setDpi(new Float(0.2));
		parameter.setInundation(true);
		
		propertyFilterServiceImpl.propertyAnalyse();
		if (!Messages.getMessage().equals(Messages._SUCCESS)){
			fail(Messages.getMessage());
		}
	}	*/
	
//	@Test
//	public void testPropertyCSDILA() throws Exception {
//		
//		ff = CommonFactoryFinder.getFilterFactory2(GeoTools.getDefaultHints());		
//		propertyFc = DataStoreConfig.getWFSFactory().getFeatureSource(layerMapping.getProperty());
//		List<Filter> fs = new ArrayList<Filter>();
//		Filter filter = ff.equals(ff.property("pfi"),ff.literal("3539423"));// no inundation 
//		fs.add(filter);
//		filter = ff.equals(ff.property("pfi"),ff.literal("4787005"));//  inundation 
//		fs.add(filter);
//		filter = ff.equals(ff.property("pfi"),ff.literal("1317010"));//  inundation 
//		fs.add(filter);
//		Filter propertyFilter = ff.or(fs);	
//		Query propertyQuery = new Query();
//		propertyQuery.setFilter(propertyFilter);
//		SimpleFeatureCollection properties= propertyFc.getFeatures(propertyQuery);
//		
//	System.out.println(properties.size());
//	URL url = this.getClass().getResource("/geoJSON");
//	File parentDirectory = new File(new URI(url.toString()));
//	
////	File jsonfile = new File(parentDirectory, "Housing_"+ "Property_Value_NWMRLGAnew" +".json"); 
////	GeoJSONUtilities.writeFeatures(properties, jsonfile);
////	
////
//		File newFile = new File("C:/programming/Projects/Housing_"+ "Property_Value_NWMRLGAnew" +".shp");	      
//		exportService.featuresExportToShapeFile(propertyFc.getSchema(), properties, newFile, true);	
//		
//		planOverlayFc = DataStoreConfig.getDefaultFactory().getFeatureSource(layerMapping.getPlanOverlay());
//		planCodeListFc = DataStoreConfig.getDefaultFactory().getFeatureSource(layerMapping.getPlanCodes());
//		this.overlayCollectionTest();
//		
////		File jsonfile2 = new File(parentDirectory, "Housing_"+ "planOverlaynew" +".json"); 
////		GeoJSONUtilities.writeFeatures(overlays, jsonfile2);
//		File newFile2 = new File("C:/programming/Projects/"+ "planOverlaynew" +".shp");	      
//		exportService.featuresExportToShapeFile(planOverlayFc.getSchema(), overlays, newFile2, true);	
//	}
}
