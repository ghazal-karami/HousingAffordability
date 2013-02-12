package au.org.housing.service;

import java.io.IOException;

import org.geotools.data.DataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.postgresql.util.PSQLException;

public interface DataStoreFactory {
	
	public DataStore getDataStore(String layername)  throws IOException, PSQLException;

	public DataStore getExportableDataStore () throws Exception;
	
	public SimpleFeatureSource getFeatureSource(String layername) throws IOException, PSQLException;

}
