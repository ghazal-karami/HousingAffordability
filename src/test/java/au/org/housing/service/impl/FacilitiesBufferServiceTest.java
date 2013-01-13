package au.org.housing.service.impl;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotEquals;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureCollections;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.reflection.Whitebox;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import au.org.housing.exception.LayerValidationException;
import au.org.housing.exception.Messages;
import au.org.housing.model.Parameter;
import au.org.housing.service.BufferService;
import au.org.housing.service.ExportService;
import au.org.housing.service.FeatureBuilder;
import au.org.housing.service.UnionService;
import au.org.housing.service.ValidationService;

import com.vividsolutions.jts.geom.Geometry;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/springapp-servlet.xml"})
public class FacilitiesBufferServiceTest {

	@Autowired Parameter parameter;

	@Autowired FeatureBuilder featureBuilder;

	@Autowired ExportService exportService;

	@Autowired ValidationService validationService;

	@Autowired BufferService bufferService;

	@Autowired UnionService unionService;
	
	FacilitiesBufferServiceImpl facilitiesBufferServiceImpl;

	public FacilitiesBufferServiceTest() {
	}
	
	@Before
	public void setup(){
		facilitiesBufferServiceImpl = new FacilitiesBufferServiceImpl();
//		Whitebox.setInternalState( facilitiesBufferServiceImpl, "validationService", validationService );
		Whitebox.setInternalState( facilitiesBufferServiceImpl, "bufferService", bufferService );
		Whitebox.setInternalState( facilitiesBufferServiceImpl, "unionService", unionService );
		Whitebox.setInternalState( facilitiesBufferServiceImpl, "parameter", parameter );
		
		ReflectionTestUtils.setField(facilitiesBufferServiceImpl, "validationService", validationService);
	}

	@Test
	public void testGenerateFacilityBuffer() throws NoSuchAuthorityCodeException, IOException, FactoryException, URISyntaxException, LayerValidationException {

		Geometry intersected = null;	
		
		parameter.setEducation_BufferDistance(2000);
		parameter.setRecreation_BufferDistance(2000);
		parameter.setMedical_BufferDistance(0);
		parameter.setCommunity_BufferDistance(0);
		parameter.setUtility_BufferDistance(0);		

		intersected =	facilitiesBufferServiceImpl.generateFacilityBuffer();
		assertNotNull(intersected);

		DefaultFeatureCollection featureCollection = (DefaultFeatureCollection) FeatureCollections.newCollection();
		featureCollection.add(featureBuilder.buildFeature(intersected));
		File newFile = new File("C:/programming/Housing_facilityBuffer.shp");	      
		exportService.featuresExportToShapeFile(featureBuilder.getType(), featureCollection, newFile, true);	
	}
}
