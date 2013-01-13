/**
 * 
 */
package test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import junit.framework.TestCase;

import org.geotools.data.simple.SimpleFeatureCollection;

import au.org.housing.utilities.GeoJSONUtilities;

/**
 * @author gkarami
 *
 */
public class GeoJSONUtilitiesTest extends TestCase {

	/**
	 * @param name
	 */
	public GeoJSONUtilitiesTest(String name) {
		super(name);
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Test method for {@link au.org.housing.utilities.GeoJSONUtilities#readFeatures(java.net.URL)}.
	 * @throws URISyntaxException 
	 * @throws IOException 
	 */
	public void testReadFeatures() throws URISyntaxException, IOException {
//		File file = new File(this.getClass().getClassLoader().getResource("geoJSON/train_station_GeoJSON.geojson").toURI());
//		File file = new File(this.getClass().getClassLoader().getResource("geoJSON/Train_Station_ArcGis_metric.json").toURI());
//		File file = new File(this.getClass().getClassLoader().getResource("geoJSON/hhhh.geojson").toURI());
//		GeoJSONUtilities.writeFeatures(features, file)
//		File file = new File(this.getClass().getClassLoader().getResource("geoJSON/Train_Station.geojson").toURI());
		File file = new File(this.getClass().getClassLoader().getResource("geoJSON/points.json").toURI());
		URL url = file.toURL();
		SimpleFeatureCollection collection = GeoJSONUtilities.readFeatures(url);
		assertNotNull(collection);
		
		
		
//		ConnectivityIndexOMS connectivityOMS = new ConnectivityIndexOMS();
//		
//		URL roadsUrl = getClass().getResource("/psma_cut_projected.geojson");
//	    URL regionsUrl = getClass().getResource("/networkBufferOMS.geojson");
//	    ConnectivityIndexOMS connectivityOMS = new ConnectivityIndexOMS();
//	    connectivityOMS.network = DataUtilities.source(GeoJSONUtilities
//	        .readFeatures(roadsUrl));
//	    connectivityOMS.regions = DataUtilities.source(GeoJSONUtilities
//	        .readFeatures(regionsUrl));
//	    connectivityOMS.run();
//	    GeoJSONUtilities.writeFeatures(connectivityOMS.results.getFeatures(),
//	        new File("connectivityOMSTest.geojson").toURI().toURL());
//	    SimpleFeatureSource comparisonSource = DataUtilities
//	        .source(GeoJSONUtilities.readFeatures(getClass().getResource(
//	            "/connectivityOMSTest.geojson")));
//	    GeotoolsAssert.assertEquals(connectivityOMS.results, comparisonSource);
	}

}
