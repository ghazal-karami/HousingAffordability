package au.org.housing.service;

import java.io.IOException;

import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;

import au.org.housing.model.Parameter;


public interface FacilitiesBufferService {
	
	public void generateFacilityBuffer() throws NoSuchAuthorityCodeException, IOException, FactoryException;
	
	Parameter getParameter();

	void setParameter(Parameter parameter) ;
	

}
