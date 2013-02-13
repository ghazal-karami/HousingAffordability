package au.org.housing.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collection;

import org.geotools.data.DataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.FeatureIterator;

import org.opengis.feature.simple.SimpleFeature;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.postgresql.util.PSQLException;

import com.vividsolutions.jts.geom.Geometry;

public interface PostGISService {
	
	public SimpleFeatureSource getFeatureSource(String layerName) throws IOException, PSQLException ;
	
	public DataStore getDataStore();

	public void setDataStore(DataStore dataStore);
	
	public void dipose();
}
