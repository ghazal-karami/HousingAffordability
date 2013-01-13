package au.org.housing.service;

public class Config {
	public static DataStoreFactory getDefaultFactory() {
		return DataStoreFactoryBuilder.getBuilder("PostGISDataStoreFactory");
	}
	
	public static DataStoreFactory getWFSFactory() {
		return DataStoreFactoryBuilder.getBuilder("WFSDataStoreFactory");
	}
	
	public static DataStoreFactory getGeoJSONFileFactory() {
		return DataStoreFactoryBuilder.getBuilder("GeoJSONFileFactory");
	}
}
