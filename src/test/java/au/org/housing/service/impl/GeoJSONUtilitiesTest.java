package au.org.housing.service.impl;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.junit.Before;
import org.junit.Test;

import au.org.housing.service.Config;
import au.org.housing.utilities.GeoJSONUtilities;

public class GeoJSONUtilitiesTest {

	public GeoJSONUtilitiesTest() {
	}

	@Before
	public void setUp() throws Exception {
		
	}

	@Test
	public void testWriteFeatureCollectionToJSON() throws IOException, URISyntaxException {
		String layerName = MapAttImpl.property;
		SimpleFeatureSource featureSource =  Config.getDefaultFactory().getDataStore(layerName).getFeatureSource(layerName);
		SimpleFeatureCollection featureCollection = featureSource.getFeatures();
//		File newFile = new File("C:/programming/Housing_"+ layerName +".json");	 
		URL url = this.getClass().getResource("/geoJSON");
		File parentDirectory = new File(new URI(url.toString()));
		File file = new File(parentDirectory, "Housing_"+ layerName +".geojson");
		GeoJSONUtilities.writeFeatures(featureCollection, file);
	}
		

	@Test
	public void testWriteFeaturesSimpleFeatureCollectionURL() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testWriteFeature() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testReadFeature() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testGetFeatureIterator() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testReadFeatures() {
		fail("Not yet implemented"); // TODO
	}

}
