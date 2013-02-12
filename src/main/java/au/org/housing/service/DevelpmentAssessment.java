package au.org.housing.service;

import java.io.IOException;

import org.geotools.ows.ServiceException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.postgresql.util.PSQLException;


public interface DevelpmentAssessment {
	
	public boolean analyse() throws IOException, NoSuchAuthorityCodeException, FactoryException, ServiceException, PSQLException ;
	
}
