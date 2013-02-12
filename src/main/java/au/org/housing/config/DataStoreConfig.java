package au.org.housing.config;

import au.org.housing.service.DataStoreFactory;
import au.org.housing.service.DataStoreFactoryBuilder;

public class DataStoreConfig {
	public static DataStoreFactory getDefaultFactory() {
		return DataStoreFactoryBuilder.getBuilder("PostGISDataStoreFactory");
	}
	
	public static DataStoreFactory getWFSFactory() {
		return DataStoreFactoryBuilder.getBuilder("WFSDataStoreFactory");
	}
	
	public static DataStoreFactory getGeoJSONFileFactory() {
		return DataStoreFactoryBuilder.getBuilder("GeoJSONFileFactory");
	}
	
	public static DataStoreFactory getShapeFileFactory() {
		return DataStoreFactoryBuilder.getBuilder("ShapeFileFactory");
	}
}
