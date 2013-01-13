package test;

import java.util.HashMap;
import java.util.Map;

import org.geotools.data.DataSourceException;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.wfs.WFSDataStoreFactory;
import org.geotools.filter.Filter;
import org.geotools.filter.text.cql2.CQL;
import org.opengis.feature.simple.SimpleFeature;

public class Test {
    public static void main0(String[] args) throws Exception {
        Map<String, Object> dataStoreParams = new HashMap<String, Object>();
        dataStoreParams.put("WFSDataStoreFactory:GET_CAPABILITIES_URL","http://e1.newcastle.edu.au/geoserver/wfs?request=GetCapabilities");
        DataStore dataStore = DataStoreFinder.getDataStore(dataStoreParams);
        SimpleFeatureSource featureSource = dataStore.getFeatureSource("cfer:AusBySLA");
        Query query = new Query();
		query.setMaxFeatures(1);
        SimpleFeatureCollection features = featureSource.getFeatures(query);
		SimpleFeatureIterator it = features.features();
		SimpleFeature simpleFeature = it.next();        
    }
    public static void main1(String[] args) throws Exception {
    	String getCapabilities = "http://192.43.209.39:8080/geoserver/ows?service=wfs&version=1.0.0&request=GetCapabilities";

    	Map<String, Object> dataStoreParams = new HashMap<String, Object>();
		dataStoreParams.put("WFSDataStoreFactory:GET_CAPABILITIES_URL",getCapabilities);
//		dataStoreParams.put(WFSDataStoreFactory.USERNAME.key, "aurin");
		dataStoreParams.put(WFSDataStoreFactory.USERNAME.key, "aurin");
		dataStoreParams.put(WFSDataStoreFactory.PASSWORD.key ,"aurinaccess");
		
    	DataStore dataStore = DataStoreFinder.getDataStore(dataStoreParams);
    	
    	SimpleFeatureSource featureSource = dataStore.getFeatureSource("CoM:com_capacities_2010");
    	Query query = new Query();
    	query.setMaxFeatures(1);
    	SimpleFeatureCollection features = featureSource.getFeatures(query);
    	SimpleFeatureIterator it = features.features();
    	SimpleFeature simpleFeature = it.next();   	
    }
    
    public static void main(String[] args) throws Exception, DataSourceException {
    	Map<String, Object> dataStoreParams = new HashMap<String, Object>();
//		String getCapabilities = "http://services.land.vic.gov.au/catalogue/httpproxy/sdm_geoserver/wfs?version=1.1.0&request=GetCapabilities";
		String getCapabilities = "http://services.land.vic.gov.au/catalogue/httpproxy/sdm_geoserver/wfs?REQUEST=GetCapabilities";
		dataStoreParams.put("WFSDataStoreFactory:GET_CAPABILITIES_URL",getCapabilities);
		dataStoreParams.put("WFSDataStoreFactory:USERNAME", "anasr");
		dataStoreParams.put("WFSDataStoreFactory:PASSWORD", "xtasw123");
		dataStoreParams.put(WFSDataStoreFactory.BUFFER_SIZE.key, new Integer(100));
		dataStoreParams.put(WFSDataStoreFactory.MAXFEATURES.key, new Integer(1));
		
//		dataStoreParams.put(WFSDataStoreFactory.TIMEOUT.key, new Integer(19000000)); 
		
		DataStore dataStore = DataStoreFinder.getDataStore(dataStoreParams);
    	
    	SimpleFeatureSource featureSource = dataStore.getFeatureSource("sii:VMPLAN.PLAN_OVERLAY");
//    	Query query = new Query();
    	Filter filter = (Filter) CQL.toFilter("UFI=699898");
//    	query.setMaxFeatures(1);
    	SimpleFeatureCollection features = featureSource.getFeatures(filter);
    	System.out.println(features.size());
    	SimpleFeatureIterator it = features.features();
    	SimpleFeature simpleFeature = it.next();   	
    }
    
    
}