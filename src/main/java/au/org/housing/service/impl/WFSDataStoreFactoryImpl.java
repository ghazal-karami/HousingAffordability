package au.org.housing.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PreDestroy;

import org.geotools.data.DataSourceException;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.wfs.WFSDataStore;
import org.geotools.data.wfs.WFSDataStoreFactory;
import org.geotools.data.wfs.v1_1_0.WFS_1_1_0_DataStore;
import org.springframework.beans.factory.annotation.Autowired;

import au.org.housing.config.LayersConfig;
import au.org.housing.service.DataStoreFactory;

public class WFSDataStoreFactoryImpl implements DataStoreFactory {

	@Autowired
	private LayersConfig layerMapping;
	
	private DataStore DSEDataStore = null;
	private DataStore CSDILADataStore = null;
	private DataStore NewCastleDataStore = null;
	private WFS_1_1_0_DataStore wFS_1_1_0_DataStore = null;

	@PreDestroy
	public void dipose(){
		DSEDataStore.dispose();
		CSDILADataStore.dispose();
	}
	
	@Override
	public SimpleFeatureSource getFeatureSource(String layerName) throws IOException {
		return getDataStore(layerName).getFeatureSource(layerName);
	}	

	@Override
	public DataStore getDataStore(String layername) throws IOException {
		if (layerMapping.getPlanOverlay().equals(layername) ||
				layerMapping.getTrainStation().equals(layername) ||
				layerMapping.getTrainRoute().equals(layername)||
				layerMapping.getTramRoute().equals(layername)  ) {
			return getDSEDataStore();
		} else if (layerMapping.getProperty().equals(layername)) {
			return getCSDILADataStore();
		}
		return null;
	}

	public DataStore getDSEDataStore() throws IOException {
		if (this.DSEDataStore == null) {
			Map<String, Object> dataStoreParams = new HashMap<String, Object>();
			String getCapabilities = "http://services.land.vic.gov.au/catalogue/httpproxy/sdm_geoserver/wfs?REQUEST=GetCapabilities";
			dataStoreParams.put("WFSDataStoreFactory:GET_CAPABILITIES_URL",getCapabilities);
			dataStoreParams.put("WFSDataStoreFactory:USERNAME", "anasr");
			dataStoreParams.put("WFSDataStoreFactory:PASSWORD", "xtasw123");
			DSEDataStore = DataStoreFinder.getDataStore(dataStoreParams);
//			DSEDataStore).setMaxFeatures(1);
		}
		return (WFSDataStore)this.DSEDataStore;
	}

	public DataStore getCSDILADataStore() throws IOException, DataSourceException{
		
		if (this.CSDILADataStore == null) {
			Map<String, Object> dataStoreParams = new HashMap<String, Object>();
			String getCapabilities = "http://192.43.209.39:8080/geoserver/ows?service=wfs&version=1.1.0&request=GetCapabilities";
			dataStoreParams.put("WFSDataStoreFactory:GET_CAPABILITIES_URL",getCapabilities);
			dataStoreParams.put("WFSDataStoreFactory:USERNAME", "aurin");
			dataStoreParams.put("WFSDataStoreFactory:PASSWORD" ,"aurinaccess");
			dataStoreParams.put(WFSDataStoreFactory.TIMEOUT.key, new Integer(18000000)); 			
//			dataStoreParams.put(WFSDataStoreFactory.USERNAME.key, "aurin");
//            dataStoreParams.put(WFSDataStoreFactory.PASSWORD.key ,"aurinaccess");
//			wFS_1_1_0_DataStore = (WFS_1_1_0_DataStore) DataStoreFinder.getDataStore(dataStoreParams);
			CSDILADataStore =  DataStoreFinder.getDataStore(dataStoreParams);
//			CSDILADataStore).setMaxFeatures(10000);
		}
//		return (WFS_1_1_0_DataStore)this.wFS_1_1_0_DataStore;
		return CSDILADataStore;
	}

	public DataStore getNEWCastleDataStore() throws IOException, DataSourceException {
		if (this.NewCastleDataStore == null) {			
			Map<String, Object> dataStoreParams = new HashMap<String, Object>();
			String getCapabilities = "http://e1.newcastle.edu.au/geoserver/wfs?request=GetCapabilities";
			dataStoreParams.put("WFSDataStoreFactory:GET_CAPABILITIES_URL",getCapabilities);
			NewCastleDataStore = DataStoreFinder.getDataStore(dataStoreParams);			
			((WFSDataStore)NewCastleDataStore).setMaxFeatures(1);
		}
		return this.NewCastleDataStore;
	}

	@Override
	public DataStore getExportableDataStore() throws Exception{		
		throw new Exception("Datastore is not exportable, use other datastore type like postgis datastore");
	}

	

}
