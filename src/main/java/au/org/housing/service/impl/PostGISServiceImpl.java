package au.org.housing.service.impl;

import java.io.IOException;
import java.rmi.ConnectException;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PreDestroy;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.simple.SimpleFeatureSource;
import org.postgresql.util.PSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import au.org.housing.config.PostGisConfig;
import au.org.housing.exception.Messages;
import au.org.housing.service.PostGISService;

import org.geotools.data.postgis.PostgisNGDataStoreFactory;

@Service
public class PostGISServiceImpl implements PostGISService {

	private static final Logger LOGGER = LoggerFactory.getLogger(PostGISServiceImpl.class);

	private DataStore dataStore = null;	

	@Autowired
	private PostGisConfig postGisConfig;

	@PreDestroy
	public void dipose(){
		System.out.println("Spring Container is destroy! Customer clean up");
		dataStore.dispose();
	}

	public SimpleFeatureSource getFeatureSource(String layerName) throws IOException, PSQLException {
		if (getDataStore(layerName) == null){
			return null;
		}
		return dataStore.getFeatureSource(layerName);
	}

	private DataStore getDataStore(String layername) throws IOException {
		return getPOSTGISDataStore() ;
	}	

	public DataStore getPOSTGISDataStore() throws IOException {
		try{
		if (this.dataStore == null) {	
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("dbtype", postGisConfig.getPostgis_type());
			params.put("host", postGisConfig.getPostgis_host());
			params.put("port", postGisConfig.getPostgis_port());
			params.put("schema", postGisConfig.getPostgis_schema());
			params.put("database", postGisConfig.getPostgis_database());
			params.put("user", postGisConfig.getPostgis_user());
			params.put("passwd", postGisConfig.getPostgis_passwd());
			params.put(PostgisNGDataStoreFactory.VALIDATECONN.key,"true");
			//				params.put( "max connections", 25);8
			//				params.put( "min connections", 10);
			params.put( "connection timeout", 1200);
			params.put( "validating connections", true);				
			params.put( "fetch size", 100);
			dataStore = DataStoreFinder.getDataStore(params);			
			LOGGER.info("dataStore.getTypeNames = "+ dataStore.getTypeNames());
		}
		}catch(Exception  e){
			LOGGER.error(e.getMessage(), e.getStackTrace()); 
			Messages.setMessage(Messages._CONN_POSTGIS_FAILED);
			dataStore = null;
		}
		return dataStore;
	}
	
	public DataStore getDataStore() {
		return dataStore;
	}

	public void setDataStore(DataStore dataStore) {
		this.dataStore = dataStore;
	}

}
