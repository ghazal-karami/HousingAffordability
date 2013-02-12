package au.org.housing.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PreDestroy;

import org.geotools.data.DataSourceException;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.simple.SimpleFeatureSource;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;

import au.org.housing.config.PostGisConfig;
import au.org.housing.exception.Messages;
import au.org.housing.service.DataStoreFactory;
import au.org.housing.service.PostGISService;


public class PostGISDataStoreFactoryImpl implements DataStoreFactory {
	private DataStore dataStore = null;
	
	
	@PreDestroy
	public void dipose(){
		dataStore = null;
		dataStore.dispose();
	}


	public SimpleFeatureSource getFeatureSource(String layerName) throws IOException, PSQLException {
		if (getDataStore(layerName) == null){
			return null;
		}
		try{
			return dataStore.getFeatureSource(layerName);
		}catch (NullPointerException e) {
			Messages.setMessage(Messages._NOT_FIND_REQUIRED_LAYER);
			return null;
		}		
	}


	public DataStore getDataStore(String layername) throws IOException, PSQLException {
		return getPOSTGISDataStore() ;
	}

	public DataStore getPOSTGISDataStore() throws IOException, PSQLException {
//		try{
//			if (this.dataStore == null) {//			
////				Map<String, Object> params = new HashMap<String, Object>();
////				params.put("dbtype", postGisConfig.getPostgis_type());
////				params.put("host", postGisConfig.getPostgis_host());
////				params.put("port", postGisConfig.getPostgis_port());
////				params.put("schema", "public");
////				params.put("database", "housing_metric");
////				params.put("user", "postgres");
////				params.put("passwd", "1q2w3e4r");
////				dataStore = DataStoreFinder.getDataStore(params);
//
//				//			params.put("dbtype", postGisConfig.getPostgis_type());
//				//			params.put("host", postGisConfig.getPostgis_host());
//				//			params.put("port", postGisConfig.getPostgis_port());
//				//			params.put("schema", postGisConfig.getPostgis_schema());
//				//			params.put("database", postGisConfig.getPostgis_database());
//				//			params.put("user", postGisConfig.getPostgis_user());
//				//			params.put("passwd", postGisConfig.getPostgis_passwd());
//				dataStore = DataStoreFinder.getDataStore(params);
//			}
//		}catch (DataSourceException e1) {
//			Messages.setMessage(Messages._CONN_POSTGIS_FAILED);			
//		}catch (Exception e2) {
//			Messages.setMessage(Messages._CONN_POSTGIS_FAILED);			
//		}
		return this.dataStore;
	}

	@Override
	public DataStore getExportableDataStore() throws Exception {
		return getPOSTGISDataStore();
	}	

}
