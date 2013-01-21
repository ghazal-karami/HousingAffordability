package au.org.housing.service;

import com.vividsolutions.jts.geom.Geometry;


public interface PropertyFilterService {	
	

	public void propertyAnalyse( )throws Exception;
//	
	public Geometry getBufferAllParams() ;

	public void setBufferAllParams(Geometry bufferAllParams);
	
//	public Parameter getParameter() ;
//
//	public void setParameter(Parameter parameter) ;

}
