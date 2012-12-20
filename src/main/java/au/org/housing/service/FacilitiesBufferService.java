package au.org.housing.service;

import java.io.IOException;
import java.net.URISyntaxException;
import org.geotools.data.DataStore;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;

import com.vividsolutions.jts.geom.Geometry;

import au.org.housing.model.Parameter;

public interface FacilitiesBufferService {
	
	public Geometry generateFacilityBuffer(DataStore datastore) throws NoSuchAuthorityCodeException, IOException, FactoryException, URISyntaxException;
	
	Parameter getParameter();

	void setParameter(Parameter parameter) ;
	

}
