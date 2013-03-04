package au.org.housing.service;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import org.geotools.data.DataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;

/**
 * Interface for exporting the collection of 
 * features to Shape File or PostGIS.
 *
 * @author Gh.Karami
 * @version 1.0
 *
 */ 

public interface ExportService {

	boolean featuresExportToShapeFile(SimpleFeatureType type,
			SimpleFeatureCollection simpleFeatureCollection, File newFile, boolean createSchema)
			throws IOException, NoSuchAuthorityCodeException, FactoryException;

	public void featuresExportToPostGis(SimpleFeatureType type,
			SimpleFeatureCollection simpleFeatureCollection, boolean createSchema, DataStore dataStore)
			throws IOException, NoSuchAuthorityCodeException, FactoryException, SQLException;
	
}
