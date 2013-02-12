package au.org.housing.service.impl;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collection;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


import au.org.housing.config.LayersConfig;
import au.org.housing.exception.Messages;
import au.org.housing.service.ExportService;
import au.org.housing.service.FeatureBuilder;

import com.vividsolutions.jts.geom.Geometry;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/springapp-servlet.xml"})
public class BufferServiceTest {
	
	@Autowired ExportService exportService;
	
	@Autowired FeatureBuilder featureBuilder;
	
	@Autowired
	private LayersConfig layerMapping;

	public BufferServiceTest() {
	}

	@Test
	public void testCreateFeaturesBuffer() throws IOException, NoSuchAuthorityCodeException, FactoryException, URISyntaxException {

////		String layerName = MapAttImpl.trainStation;
////		String layerName = MapAttImpl.educationFacilities;
//		String layerName = layerMapping.getRecreationFacilities();
//		double distance = 2000;
//
//		SimpleFeatureSource featureSource =  DataStoreConfig.getDefaultFactory().getDataStore(layerName).getFeatureSource(layerName);
//		assertNotNull(featureSource);
//		SimpleFeatureCollection features = featureSource.getFeatures();
//		assertNotEquals(features.size(), 0);
//
//		BufferServiceImpl bufferServiceImpl = new BufferServiceImpl();
//		Collection<Geometry> bufferCollection = bufferServiceImpl.createFeaturesBuffer(features, distance, layerName);
//		
//		assertNotEquals(Messages.getMessage(), Messages._NOT_VALID);
//		
//		assertNotSame(bufferCollection.size(), new Integer(0));
//				
//		SimpleFeatureCollection featureCollection = featureBuilder.buildFeatureCollection(bufferCollection);
//		assertNotEquals(featureCollection.size(), 0);
//		
//		File newFile = new File("C:/programming/Housing_"+ layerName +".shp");	      
//		exportService.featuresExportToShapeFile(featureSource.getSchema(), features, newFile, true);		
	}	
}
