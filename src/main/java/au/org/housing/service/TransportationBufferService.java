package au.org.housing.service;

import java.io.IOException;
import java.net.URISyntaxException;

import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.postgresql.util.PSQLException;

import au.org.housing.exception.HousingException;

import com.vividsolutions.jts.geom.Geometry;


/**
 * Interface for generating buffer for Transportation 
 * DataSets based on the selected parameters by user.
 *
 * @author Gh.Karami
 * @version 1.0
 *
 */ 

public interface TransportationBufferService {

	public Geometry generateTranportBuffer()
			throws NoSuchAuthorityCodeException, IOException, FactoryException,
			URISyntaxException, PSQLException, HousingException;

}
