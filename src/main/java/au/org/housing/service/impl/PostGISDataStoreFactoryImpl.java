package au.org.housing.service.impl;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PreDestroy;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;

import au.org.housing.service.DataStoreFactory;
import au.org.housing.utilities.GeoJSONUtilities;

public class PostGISDataStoreFactoryImpl implements DataStoreFactory {
	private DataStore dataStore = null;
	
	@PreDestroy
	public void dipose(){
		dataStore.dispose();
	}

	@Override
	public SimpleFeatureSource getFeatureSource(String layerName) throws IOException {
		return getDataStore(layerName).getFeatureSource(layerName);
	}

	@Override
	public DataStore getDataStore(String layername) throws IOException {
			return getPOSTGISDataStore();
	}
	
	public DataStore getPOSTGISDataStore() throws IOException {
		if (this.dataStore == null) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("dbtype", "postgis");
			params.put("host", "localhost");
			params.put("port", 5432);
			params.put("schema", "public");
//			 params.put("database", "housing_4283");
			params.put("database", "housingmetric");
			params.put("user", "postgres");
			params.put("passwd", "1q2w3e4r");
			dataStore = DataStoreFinder.getDataStore(params);
		}
		return this.dataStore;
	}

	@Override
	public DataStore getExportableDataStore() throws Exception {
		return getPOSTGISDataStore();
	}	

}
