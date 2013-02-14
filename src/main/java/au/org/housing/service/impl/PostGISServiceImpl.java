package au.org.housing.service.impl;

import java.io.IOException;
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
import org.springframework.stereotype.Service;

import au.org.housing.config.PostGisConfig;
import au.org.housing.service.PostGISService;

@Service
public class PostGISServiceImpl implements PostGISService {

	private static final Logger LOGGER = LoggerFactory.getLogger(PostGISServiceImpl.class);

	private DataStore dataStore = null;
	
	public DataStore getDataStore() {
		return dataStore;
	}

	public void setDataStore(DataStore dataStore) {
		this.dataStore = dataStore;
	}

	@Autowired
	private PostGisConfig postGisConfig;

	@PreDestroy
	public void dipose(){
		System.out.println("Spring Container is destroy! Customer clean up");
		dataStore = null;
		dataStore.dispose();
	}
	
	public SimpleFeatureSource getFeatureSource(String layerName) throws IOException, PSQLException {
		if (getDataStore(layerName) == null){
			return null;
		}
		return dataStore.getFeatureSource(layerName);
	}

	private DataStore getDataStore(String layername) throws IOException, PSQLException {
		return getPOSTGISDataStore() ;
	}	
	
	private DataStore getPOSTGISDataStore() throws IOException {
			if (this.dataStore == null) {//			
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("dbtype", postGisConfig.getPostgis_type());
				params.put("host", postGisConfig.getPostgis_host());
				params.put("port", postGisConfig.getPostgis_port());
				params.put("schema", postGisConfig.getPostgis_schema());
				params.put("database", postGisConfig.getPostgis_database());
				params.put("user", postGisConfig.getPostgis_user());
				params.put("passwd", postGisConfig.getPostgis_passwd());
				dataStore = DataStoreFinder.getDataStore(params);
				
			}
		return this.dataStore;
	}

	
}
