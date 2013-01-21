package au.org.housing.service.impl;

import static org.junit.Assert.*;

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

import com.vividsolutions.jts.geom.Geometry;

import au.org.housing.model.Parameter;
import au.org.housing.service.BufferService;
import au.org.housing.service.ExportService;
import au.org.housing.service.FeatureBuilder;
import au.org.housing.service.UnionService;
import au.org.housing.service.ValidationService;
import au.org.housing.utilities.GeoJSONUtilities;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/springapp-servlet.xml"})
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
		parameter.setTrain_Rt_BufferDistance(2000);
		parameter.setTram_Rt_BufferDistance(0);	

		union =	transportationBufferServiceImpl.generateTranportBuffer();
		assertNotNull(union);
		SimpleFeature feature = featureBuilder.buildFeature(union);
		
		URL url = this.getClass().getResource("/geoJSON");
		File parentDirectory = new File(new URI(url.toString()));		
		File jsonfile = new File(parentDirectory, "Housing_transportBuffer.json"); 

		GeoJSONUtilities.writeFeature(feature, jsonfile);
		
		DefaultFeatureCollection featureCollection = (DefaultFeatureCollection) FeatureCollections.newCollection();
		featureCollection.add(feature);
//		File newFile = new File("C:/programming/Housing_facilityBuffer.shp");	      
		File shpFile = new File(parentDirectory, "Housing_transportBuffer.shp");	     
		exportService.featuresExportToShapeFile(featureBuilder.getType(), featureCollection, shpFile, true);		
	}
}
