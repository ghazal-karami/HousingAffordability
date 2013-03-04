package au.org.housing.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Output layer config from its name to its attributes 
 *
 * @author Gh.Karami
 * @version 1.0
 *
 */

@Component
public class OutPutLayerConfig{
	
	@Value("${outputName}") 
	private String outputName ;	
	
	@Value("${objectid}") 
	private String objectid ;	
	
	@Value("${pfi}") 
	private String pfi ;	
	
	@Value("${lgaName}") 
	private String lgaName ;	
	
	@Value("${street_name}") 
	private String street_name ;	
	
	@Value("${street_type}") 
	private String street_type ;	
	
	@Value("${suburb}") 
	private String suburb ;	
	
	@Value("${postcode}") 
	private String postcode ;
	
	@Value("${land_area}") 
	private String land_area ;
	
	@Value("${areameasure}") 
	private String areameasure ;

	public String getOutputName() {
		return outputName;
	}

	public void setOutputName(String outputName) {
		this.outputName = outputName;
	}

	public String getObjectid() {
		return objectid;
	}

	public void setObjectid(String objectid) {
		this.objectid = objectid;
	}

	public String getPfi() {
		return pfi;
	}

	public void setPfi(String pfi) {
		this.pfi = pfi;
	}

	public String getLgaName() {
		return lgaName;
	}

	public void setLgaName(String lgaName) {
		this.lgaName = lgaName;
	}

	public String getStreet_name() {
		return street_name;
	}

	public void setStreet_name(String street_name) {
		this.street_name = street_name;
	}

	public String getStreet_type() {
		return street_type;
	}

	public void setStreet_type(String street_type) {
		this.street_type = street_type;
	}

	public String getSuburb() {
		return suburb;
	}

	public void setSuburb(String suburb) {
		this.suburb = suburb;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getLand_area() {
		return land_area;
	}

	public void setLand_area(String land_area) {
		this.land_area = land_area;
	}

	public String getAreameasure() {
		return areameasure;
	}

	public void setAreameasure(String areameasure) {
		this.areameasure = areameasure;
	}
	
		
}