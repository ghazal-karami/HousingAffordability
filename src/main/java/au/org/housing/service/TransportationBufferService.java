package au.org.housing.service;

import java.io.IOException;
import java.net.URISyntaxException;

import org.geotools.data.DataStore;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;

import com.vividsolutions.jts.geom.Geometry;

import au.org.housing.model.Parameter;


public interface TransportationBufferService {
	
	public void exportToShp(Geometry union) throws URISyntaxException, NoSuchAuthorityCodeException, IOException, FactoryException ;
	
	public Geometry generateTranportBuffer(DataStore datastore) throws NoSuchAuthorityCodeException, IOException, FactoryException, URISyntaxException;

	public Parameter getParameter() ;

	public void setParameter(Parameter parameter);
}
