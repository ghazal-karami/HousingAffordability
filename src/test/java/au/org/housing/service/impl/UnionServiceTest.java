package au.org.housing.service.impl;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureCollections;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


import au.org.housing.config.InputLayersConfig;
import au.org.housing.exception.Messages;
import au.org.housing.service.ExportService;
import au.org.housing.service.FeatureBuilder;

import com.vividsolutions.jts.geom.Geometry;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/springapp-servlet.xml"})
public class UnionServiceTest {

	@Autowired ExportService exportService;

	@Autowired FeatureBuilder featureBuilder;
	
	@Autowired
	private InputLayersConfig layerMapping;


	public UnionServiceTest() {
	}

	@Before
	public void setUp() throws Exception {
	}

//	@Test
//	public void testCreateUnionGeometryCollection() throws IOException, NoSuchAuthorityCodeException, FactoryException {
//
//		Collection<Geometry> bufferCollection = new ArrayList<Geometry>();
//		String layerName = layerMapping.getEducationFacilities();
//		SimpleFeatureSource featureSource =  DataStoreConfig.getDefaultFactory().getDataStore(layerName).getFeatureSource(layerName);
//		assertNotNull(featureSource);
//		SimpleFeatureCollection features = featureSource.getFeatures();
//		SimpleFeatureIterator it = features.features();
//		try {
//			while (it.hasNext()) {
//				SimpleFeature simpleFeature = it.next(); 
//				Geometry featureGeometry = (Geometry) simpleFeature.getDefaultGeometryProperty().getValue();
//				if (!featureGeometry.isValid()){
//					fail();
//				}
//				bufferCollection.add(featureGeometry);				
//			}
//		} finally {
//			it.close(); 
//		}		
//		assertNotNull(bufferCollection);
//		UnionServiceImpl unionServiceImpl = new UnionServiceImpl();
//		Geometry geometry = unionServiceImpl.createUnion(bufferCollection);
//		assertNotEquals(Messages.getMessage(), Messages._NOT_VALID);
//		DefaultFeatureCollection featureCollection = (DefaultFeatureCollection) FeatureCollections.newCollection();
//		featureCollection.add(featureBuilder.buildFeature(geometry));
//		assertNotEquals(featureCollection.size(), 0);
//		File newFile = new File("C:/programming/Housing_"+ layerName +"_Union.shp");	      
//		exportService.featuresExportToShapeFile(featureBuilder.getType(), featureCollection, newFile, true);		
//
//	}

}
