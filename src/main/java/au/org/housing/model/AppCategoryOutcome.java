package au.org.housing.model;

/**
 * Contains the fields for displaying the categories and
 * outcomes from related tables in PostGIS to the user.
 *
 * @author Gh.Karami
 * @version 1.0
 *
 */ 

public class AppCategoryOutcome {
	public AppCategoryOutcome(int code, String desc) {
		super();
		this.code = code;
		this.desc = desc;
	}
	private int code; 
	
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	private String desc; 
}
