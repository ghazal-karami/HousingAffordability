package au.org.housing.service.impl;

import java.io.IOException;

import org.geotools.data.DataStore;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.opengis.feature.simple.SimpleFeature;
import org.springframework.beans.factory.annotation.Autowired;

import au.org.housing.config.InputLayersConfig;

import junit.framework.TestCase;

public class WFSDataStoreFactoryTest extends TestCase {

	@Autowired
	private InputLayersConfig layerMapping;
	
	public WFSDataStoreFactoryTest(String name) {
		super(name);
	}

	//	protected void setUp() throws Exception {
	//		super.setUp();
	//	}
	//
	//	protected void tearDown() throws Exception {
	//		super.tearDown();
	//	}
	//
	//	public void testDipose() {
	//		fail("Not yet implemented"); // TODO
	//	}
	//
//	public void testGetDataStore() throws IOException {
//		WFSDataStoreFactoryImpl wfsDataStoreFactoryImpl = new WFSDataStoreFactoryImpl();
//		DataStore dataStoreProperty = wfsDataStoreFactoryImpl.getDataStore(layerMapping.getProperty());
//		DataStore dataStoreCSDILA = wfsDataStoreFactoryImpl.getCSDILADataStore();
//		assertSame(dataStoreProperty, dataStoreCSDILA);
//	}
//
//	public void testGetDSEDataStore() throws IOException {
//		try{
//			WFSDataStoreFactoryImpl wfsDataStoreFactoryImpl = new WFSDataStoreFactoryImpl();
//			DataStore ds = wfsDataStoreFactoryImpl.getDSEDataStore();
//			SimpleFeatureSource featureSource = ds.getFeatureSource("sii:DPS_932_RAIL_STATIONS_VMT");
//			Query query = new Query();
//			query.setMaxFeatures(1);
//			SimpleFeatureCollection features = featureSource.getFeatures(query);
//			SimpleFeatureIterator it = features.features();
//			SimpleFeature simpleFeature = it.next(); 
//			assertNotNull(simpleFeature);
//		}catch(IOException e){
//			fail(e.getMessage());
//		}
//	}
//
//	public void testGetCSDILADataStore() throws IOException {
//		try{
//			WFSDataStoreFactoryImpl wfsDataStoreFactoryImpl = new WFSDataStoreFactoryImpl();
//			DataStore ds = wfsDataStoreFactoryImpl.getCSDILADataStore();
//			SimpleFeatureSource featureSource = ds.getFeatureSource("CoM:com_capacities_2010");
//			Query query = new Query();
//			query.setMaxFeatures(1);
//			SimpleFeatureCollection features = featureSource.getFeatures(query);
//			SimpleFeatureIterator it = features.features();
//			SimpleFeature simpleFeature = it.next(); 
//			assertNotNull(simpleFeature);
//		}catch(IOException e){
//			fail(e.getMessage());
//		}
//	}

}
