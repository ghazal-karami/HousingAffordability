package au.org.housing.model;

/**
 * Represents LGA model 
 *
 * @author Gh.Karami
 * @version 1.0
 *
 */

public class LGA {
	
	public String getLgaCode() {
		return lgaCode;
	}
	public void setLgaCode(String lgaCode) {
		this.lgaCode = lgaCode;
	}
	public String getLgaName() {
		return lgaName;
	}
	public void setLgaName(String lgaName) {
		this.lgaName = lgaName;
	}
	
	public LGA(String lgaCode, String lgaName) {
        this.lgaCode = lgaCode;
        this.lgaName = lgaName;
    }
	private String lgaCode;
	private String lgaName;

}
