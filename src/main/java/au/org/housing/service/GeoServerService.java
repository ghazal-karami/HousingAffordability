package au.org.housing.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

import au.org.housing.exception.HousingException;

/**
 * Interface for checking the existence of 
 * GeoServer or required workspace and styles.
 *
 * @author Gh.Karami
 * @version 1.0
 *
 */ 

public interface GeoServerService {

	public boolean getGeoServer(String username) throws HousingException, MalformedURLException, URISyntaxException;
	
	public boolean geoServerExist() throws HousingException, MalformedURLException;
	
	public boolean workSpaceExist(String workspace) throws HousingException;
	
	public boolean publishToGeoServer(String workspace , String dataStore , String layer, String style, File newFile) throws FileNotFoundException, IllegalArgumentException, MalformedURLException, HousingException, URISyntaxException;
	
	public boolean publishAssessmentStyle(String styleName) throws HousingException, URISyntaxException ;
	
	public String createPotentialRule(String oldRules, int propertyOverlaysNum);
	
	public boolean publishPotentialStyle(String sldBody, String styleName) throws HousingException, URISyntaxException;
		
}
