package au.org.housing.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.geotools.ows.ServiceException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.postgresql.util.PSQLException;

import au.org.housing.exception.HousingException;

/**
 * Interface for handling Assessment Development analysis.
 *
 * @author Gh.Karami
 * @version 1.0
 *
 */ 

public interface DevelpmentAssessmentService {
	
	public  boolean analyse(String username, HttpSession session) throws IOException, NoSuchAuthorityCodeException, FactoryException, ServiceException, PSQLException, HousingException, IllegalArgumentException, URISyntaxException ;
	
	public Map<String, Object> getOutputLayer() ;
	
}
