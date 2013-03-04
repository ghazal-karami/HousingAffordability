package au.org.housing.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * GeoServer Connection Parameters and configuration for 
 * publishing and visualization of the output layer
 *
 * @author Gh.Karami
 * @version 1.0
 *
 */

@Component
public class GeoServerConfig{
	
	@Value("${geoserverRESTURL}") 
	private String RESTURL; 
	
	@Value("${geoserverRESTUSER}") 
	private String RESTUSER; 
	
	@Value("${geoserverRESTPW}") 
	private String RESTPW; 

	@Value("${gsWorkspace}") 
	private String gsWorkspace; 

	@Value("${gsAssessmentDatastore}")
	private String gsAssessmentDatastore; 
		
	@Value("${gsAssessmentLayer}") 
	private String gsAssessmentLayer; 
	
	@Value("${gsAssessmentStyle}") 
	private String gsAssessmentStyle; 
	
	@Value("${gsPotentialDatastore}")
	private String gsPotentialDatastore; 		
	
	@Value("${gsPotentialLayer}") 
	private String gsPotentialLayer; 
	
	@Value("${gsPotentialStyle}") 
	private String gsPotentialStyle; 	
	
	public String getGsWorkspace() {
		return gsWorkspace;
	}
	
	public String getGsPotentialDatastore() {
		return gsPotentialDatastore;
	}

	public void setGsPotentialDatastore(String gsPotentialDatastore) {
		this.gsPotentialDatastore = gsPotentialDatastore;
	}

	public String getGsPotentialLayer() {
		return gsPotentialLayer;
	}

	public void setGsPotentialLayer(String gsPotentialLayer) {
		this.gsPotentialLayer = gsPotentialLayer;
	}

	public String getGsPotentialStyle() {
		return gsPotentialStyle;
	}

	public void setGsPotentialStyle(String gsPotentialStyle) {
		this.gsPotentialStyle = gsPotentialStyle;
	}

	public void setGsWorkspace(String gsWorkspace) {
		this.gsWorkspace = gsWorkspace;
	}

	public String getGsAssessmentDatastore() {
		return gsAssessmentDatastore;
	}

	public void setGsAssessmentDatastore(String gsAssessmentDatastore) {
		this.gsAssessmentDatastore = gsAssessmentDatastore;
	}

	public String getGsAssessmentLayer() {
		return gsAssessmentLayer;
	}
	
	public void setGsAssessmentLayer(String gsAssessmentLayer) {
		this.gsAssessmentLayer = gsAssessmentLayer;
	}

	public String getGsAssessmentStyle() {
		return gsAssessmentStyle;
	}

	public void setGsAssessmentStyle(String gsAssessmentStyle) {
		this.gsAssessmentStyle = gsAssessmentStyle;
	}
	public String getRESTURL() {
		return RESTURL;
	}

	public void setRESTURL(String rESTURL) {
		RESTURL = rESTURL;
	}

	public String getRESTUSER() {
		return RESTUSER;
	}

	public void setRESTUSER(String rESTUSER) {
		RESTUSER = rESTUSER;
	}

	public String getRESTPW() {
		return RESTPW;
	}

	public void setRESTPW(String rESTPW) {
		RESTPW = rESTPW;
	}

	
}
