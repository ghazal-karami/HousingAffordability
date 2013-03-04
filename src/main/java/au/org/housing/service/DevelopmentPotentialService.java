package au.org.housing.service;

import java.util.Map;

import javax.servlet.http.HttpSession;

import com.vividsolutions.jts.geom.Geometry;

/**
 * Interface for handling Potential Development analysis.
 *
 * @author Gh.Karami
 * @version 1.0
 *
 */ 

public interface DevelopmentPotentialService {		

	public boolean analyse(String username, HttpSession session)throws Exception;

	public Geometry getBufferAllParams() ;

	public void setBufferAllParams(Geometry bufferAllParams);

	public Map<String, Object> getOutputLayer() ;

}
