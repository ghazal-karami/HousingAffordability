package au.org.housing.service;

import java.io.IOException;

import javax.servlet.http.HttpSession;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.ows.ServiceException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.postgresql.util.PSQLException;

import au.org.housing.config.GeoServerConfig;


public interface DevelpmentAssessment {
	
	public boolean analyse(HttpSession session) throws IOException, NoSuchAuthorityCodeException, FactoryException, ServiceException, PSQLException ;
	
	public String getLayerName() ;

	public void setLayerName(String layerName) ;
	
	public ReferencedEnvelope getEnvelope();

	public void setEnvelope(ReferencedEnvelope envelope);
	
	public GeoServerConfig getGeoServerConfig() ;
	
	public void setGeoServerConfig(GeoServerConfig geoServerConfig) ;


	
}
