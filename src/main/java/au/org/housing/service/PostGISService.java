package au.org.housing.service;

import java.io.IOException;

import org.geotools.data.DataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.postgresql.util.PSQLException;

import au.org.housing.exception.HousingException;

/**
 * Interface for setting the connection to PostGIS and
 * also creating a dataStore to PostGIS to be used by GeoTools
 * for fetching the Input DataSets for further analysis.
 *
 * @author Gh.Karami
 * @version 1.0
 *
 */ 

public interface PostGISService {
	
	public SimpleFeatureSource getFeatureSource(String layerName) throws IOException, PSQLException, HousingException ;
	
	public DataStore getDataStore();

	public void setDataStore(DataStore dataStore);
	
	public DataStore getPOSTGISDataStore() throws IOException, HousingException;
	
	public void dipose();
}
