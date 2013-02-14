package au.org.housing.service;

import javax.servlet.http.HttpSession;

import org.geotools.geometry.jts.ReferencedEnvelope;

import au.org.housing.config.GeoServerConfig;

import com.vividsolutions.jts.geom.Geometry;


public interface PropertyFilterService {		

	public void analyse(HttpSession session )throws Exception;

	public Geometry getBufferAllParams() ;

	public void setBufferAllParams(Geometry bufferAllParams);
	
	public ReferencedEnvelope getEnvelope() ;
	
	public void setEnvelope(ReferencedEnvelope envelope) ;

	public GeoServerConfig getGeoServerConfig() ;
	
	public void setGeoServerConfig(GeoServerConfig geoServerConfig) ;

	public String getLayerName();
	
	public void setLayerName(String layerName) ;

}
