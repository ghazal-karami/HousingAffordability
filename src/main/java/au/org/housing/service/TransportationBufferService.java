package au.org.housing.service;

import java.io.IOException;
import java.net.URISyntaxException;

import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;

import au.org.housing.model.Parameter;


public interface TransportationBufferService {
	
	public void generateTranportBuffer() throws NoSuchAuthorityCodeException, IOException, FactoryException, URISyntaxException;
	
	Parameter getParameter();

	void setParameter(Parameter parameter) ;
	

}
