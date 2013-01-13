package au.org.housing.service;

import java.io.IOException;

import org.geotools.data.DataStore;

public interface DataStoreFactory {
	
	public DataStore getDataStore(String layername)  throws IOException;

	public DataStore getExportableDataStore () throws Exception;

}
