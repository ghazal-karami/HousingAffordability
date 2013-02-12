package au.org.housing.service;

import java.io.IOException;
import java.net.URISyntaxException;

import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.postgresql.util.PSQLException;

import com.vividsolutions.jts.geom.Geometry;

public interface TransportationBufferService {

	public Geometry generateTranportBuffer()
			throws NoSuchAuthorityCodeException, IOException, FactoryException,
			URISyntaxException, PSQLException;

}
