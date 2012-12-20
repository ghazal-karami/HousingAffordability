package au.org.housing.service;

import java.io.IOException;
import java.net.URISyntaxException;

import org.geotools.data.DataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.filter.text.cql2.CQLException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;

import au.org.housing.model.Parameter;

import com.vividsolutions.jts.geom.Geometry;


public interface PropertyFilterService {	
	
	SimpleFeatureCollection createFilters(DataStore dataStore) throws IOException, NoSuchAuthorityCodeException, FactoryException, CQLException, URISyntaxException;
	
	public Geometry getBufferAllParams() ;

	public void setBufferAllParams(Geometry bufferAllParams);
	
	public Parameter getParameter() ;

	public void setParameter(Parameter parameter) ;

}
