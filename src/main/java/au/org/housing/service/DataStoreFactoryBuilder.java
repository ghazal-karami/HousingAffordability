package au.org.housing.service;

import au.org.housing.service.impl.PostGISDataStoreFactoryImpl;
import au.org.housing.service.impl.WFSDataStoreFactoryImpl;

public class DataStoreFactoryBuilder {
	
	private static DataStoreFactory _WFSDataStoreFactoryImpl = null;
	private static DataStoreFactory _PostGISDataStoreFactoryImpl = null;
	private static DataStoreFactory _GeoJSONFileFactoryImpl = null;
	private static DataStoreFactory _ShapeFileFactoryImpl = null;
	
	public static DataStoreFactory getBuilder(String buildername){
		if ("WFSDataStoreFactory".equals(buildername)){
			if (_WFSDataStoreFactoryImpl==null)	
				_WFSDataStoreFactoryImpl = new WFSDataStoreFactoryImpl();
			return _WFSDataStoreFactoryImpl;
			
		}
		if ("PostGISDataStoreFactory".equals(buildername)){
			if (_PostGISDataStoreFactoryImpl==null)	
				_PostGISDataStoreFactoryImpl = new PostGISDataStoreFactoryImpl();
			return _PostGISDataStoreFactoryImpl;
		}
		return null;
	}

}
