package au.org.housing.service.impl;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureCollections;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.reflection.Whitebox;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.springframework.beans.factory.annotation.Autowired;

import com.vividsolutions.jts.geom.Geometry;

import au.org.housing.model.Parameter;
import au.org.housing.service.BufferService;
import au.org.housing.service.ExportService;
import au.org.housing.service.FeatureBuilder;
import au.org.housing.service.UnionService;
import au.org.housing.service.ValidationService;

public class TransportationBufferServiceTest {

	@Autowired Parameter parameter;

	@Autowired FeatureBuilder featureBuilder;

	@Autowired ExportService exportService;

	@Autowired ValidationService validationService;

	@Autowired BufferService bufferService;

	@Autowired UnionService unionService;
	
	TransportationBufferServiceImpl transportationBufferServiceImpl;

	public TransportationBufferServiceTest() {
	}

	@Before
	public void setUp() throws Exception {
		transportationBufferServiceImpl = new TransportationBufferServiceImpl();
		Whitebox.setInternalState( transportationBufferServiceImpl, "validationService", validationService );
		Whitebox.setInternalState( transportationBufferServiceImpl, "bufferService", bufferService );
		Whitebox.setInternalState( transportationBufferServiceImpl, "unionService", unionService );
		Whitebox.setInternalState( transportationBufferServiceImpl, "parameter", parameter );
	}

	@Test
	public void testGenerateTranportBuffer() throws NoSuchAuthorityCodeException, IOException, FactoryException, URISyntaxException {
		Geometry union = null;	
		
		parameter.setTrain_St_BufferDistance(2000);
		parameter.setTrain_Rt_BufferDistance(0);
		parameter.setTram_Rt_BufferDistance(0);	

		union =	transportationBufferServiceImpl.generateTranportBuffer();
		assertNotNull(union);

		DefaultFeatureCollection featureCollection = (DefaultFeatureCollection) FeatureCollections.newCollection();
		featureCollection.add(featureBuilder.buildFeature(union));
		File newFile = new File("C:/programming/Housing_facilityBuffer.shp");	      
		exportService.featuresExportToShapeFile(featureBuilder.getType(), featureCollection, newFile, true);
		
	}
}
