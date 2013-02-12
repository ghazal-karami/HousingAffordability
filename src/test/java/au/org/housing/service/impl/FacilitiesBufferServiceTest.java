package au.org.housing.service.impl;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureCollections;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.reflection.Whitebox;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import au.org.housing.exception.LayerValidationException;
import au.org.housing.model.ParameterDevelopPotential;
import au.org.housing.service.BufferService;
import au.org.housing.service.ExportService;
import au.org.housing.service.FeatureBuilder;
import au.org.housing.service.UnionService;
import au.org.housing.service.ValidationService;
import au.org.housing.utilities.GeoJSONUtilities;

import com.vividsolutions.jts.geom.Geometry;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/springapp-servlet.xml"})
public class FacilitiesBufferServiceTest {

	@Autowired ParameterDevelopPotential parameter;

	@Autowired FeatureBuilder featureBuilder;

	@Autowired ExportService exportService;

	@Autowired ValidationService validationService;

	@Autowired BufferService bufferService;

	@Autowired UnionService unionService;

	FacilitiesBufferServiceImpl facilitiesBufferServiceImpl;

	Geometry intersected = null;	

	public FacilitiesBufferServiceTest() {
	}

	@Before
	public void setup(){
		facilitiesBufferServiceImpl = new FacilitiesBufferServiceImpl();
		Whitebox.setInternalState( facilitiesBufferServiceImpl, "validationService", validationService );
		Whitebox.setInternalState( facilitiesBufferServiceImpl, "bufferService", bufferService );
		Whitebox.setInternalState( facilitiesBufferServiceImpl, "unionService", unionService );
		Whitebox.setInternalState( facilitiesBufferServiceImpl, "parameter", parameter );
		//		ReflectionTestUtils.setField(facilitiesBufferServiceImpl, "validationService", validationService);
	}

	@Test
	public void testGenerateFacilityBuffer() throws NoSuchAuthorityCodeException, IOException, FactoryException, URISyntaxException, LayerValidationException {
//		parameter.setEducation_BufferDistance(2000);
//		parameter.setRecreation_BufferDistance(2000);
//		
//		intersected = facilitiesBufferServiceImpl.generateFacilityBuffer();
//		assertNotNull(intersected);
//		SimpleFeature feature = featureBuilder.buildFeature(intersected);
//		
//		URL url = this.getClass().getResource("/geoJSON");
//		File parentDirectory = new File(new URI(url.toString()));
//		
//		File jsonfile = new File(parentDirectory, "Housing_facilityBuffer.json"); 
//		GeoJSONUtilities.writeFeature(feature, jsonfile);
//		
//		DefaultFeatureCollection featureCollection = (DefaultFeatureCollection) FeatureCollections.newCollection();
//		featureCollection.add(feature);
//		File shpFile = new File(parentDirectory, "Housing_facilityBuffer.shp");	      
//		exportService.featuresExportToShapeFile(featureBuilder.getType(), featureCollection, shpFile, true);	
	}
}
