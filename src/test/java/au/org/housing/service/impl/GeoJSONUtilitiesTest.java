package au.org.housing.service.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;

import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import au.org.housing.config.DataStoreConfig;
import au.org.housing.config.LayersConfig;
import au.org.housing.utilities.GeoJSONUtilities;

public class GeoJSONUtilitiesTest {

	@Autowired
	private LayersConfig layerMapping;
	
	public GeoJSONUtilitiesTest() {
	}

	@Before
	public void setUp() throws Exception {		 
	}

	@Test
	public void testWriteFeatureCollectionToJSON() throws IOException, URISyntaxException {
		String layerName = "Property";
//		SimpleFeatureSource featureSource =  Config.getDefaultFactory().getDataStore(layerName).getFeatureSource(layerName);
//		SimpleFeatureSource featureSource =  Config.getGeoJSONFileFactory().getFeatureSource(layerName);
		File file2 = new File("C:/Programming/Projects/Data/Property/"+layerName+".shp");
		FileDataStore store = FileDataStoreFinder.getDataStore(file2);
		SimpleFeatureSource featureSource = store.getFeatureSource();	
		SimpleFeatureCollection featureCollection = featureSource.getFeatures(); 
		URL url = this.getClass().getResource("/geoJSON");
		File parentDirectory = new File(new URI(url.toString()));
		File file = new File(parentDirectory, "Housing_"+ layerName +".json"); 
		GeoJSONUtilities.writeFeatures(featureCollection, file);
	}	
	
	@Test
	public void testreadFeaturesFromURL() throws IOException, URISyntaxException {	
		String layerName = layerMapping.getProperty();
		URL url = this.getClass().getResource("/geoJSON/Housing_"+layerName+".json");
//		URL url = this.getClass().getResource("C:/Programming/Projects/Data/Property/GDA94_MGA_zone_55/Housing_"+layerName+".json");
		
//		File file = new File("C:/Programming/Projects/Data/Property/GDA94_MGA_zone_55/Housing_"+layerName+".json");
//		URL url = new URL("C:/Programming/Projects/Data/Property/GDA94_MGA_zone_55/Housing_"+layerName+".json");
		System.out.println(new Date());
		SimpleFeatureCollection featureCollection = GeoJSONUtilities.readFeatures(url);
		System.out.println(new Date());
		assertNotNull(featureCollection);
		assertNotEquals(featureCollection.size(), 0);
	}	
}
