package au.org.housing.model;

import org.springframework.stereotype.Component;

@Component
public class LayerMapping{
	
	private String ppars = "ppars";	
	
	public String getPpars() {
		return ppars;
	}
	public void setPpars(String ppars) {
		this.ppars = ppars;
	}
	private String property = "property";
	private String property_svCurrentYear = "sv_current_year";
	private String property_civCurrentYear = "civ_current_year";
	private String property_zoning = "zoning";

	private String zonecodesTbl = "tbl_zonecodes";
	private String zonecodes_zoneCode = "zone_code";
	private String zonecodes_group1 = "group1";

	private String planOverlay = "Plan_Overlay";
	private String planOverlay_zoneCode = "zone_code";

	private String planCodes = "plan_codelist";
	private String planCodes_zoneCode = "zone_code";
	private String planCodes_group1 = "group1";


	private String trainStation = "train_station_arcgis_metric";
	private String trainRoute = "train_route_arcgis_metric";
	private String tramRoute = "tram_route_arcgis_metric";

	private String educationFacilities = "education_facilities";
	private String recreationFacilities = "recreation_facilities";
	private String medicalFacilities = "medical_facilities";
	private String communityFacilities = "communty_facilities";
	private String utilityFacilities = "utility_facilities";
	
	
	private String appCategory = "tbl_appcategory";
	private String appCategory_code = "cat_code";
	private String appCategory_desc = "cat_desc";

	private String appOutcome = "tbl_outcome";
	private String appOutcome_code = "outcome_co";
	private String appOutcome_desc = "outcome_de";

	
	public String getAppOutcome() {
		return appOutcome;
	}
	public void setAppOutcome(String appOutcome) {
		this.appOutcome = appOutcome;
	}
	public String getAppOutcome_code() {
		return appOutcome_code;
	}
	public void setAppOutcome_code(String appOutcome_code) {
		this.appOutcome_code = appOutcome_code;
	}
	public String getAppOutcome_desc() {
		return appOutcome_desc;
	}
	public void setAppOutcome_desc(String appOutcome_desc) {
		this.appOutcome_desc = appOutcome_desc;
	}

	public String getAppCategory_code() {
		return appCategory_code;
	}
	public void setAppCategory_code(String appCategory_code) {
		this.appCategory_code = appCategory_code;
	}
	public String getAppCategory_desc() {
		return appCategory_desc;
	}
	public void setAppCategory_desc(String appCategory_desc) {
		this.appCategory_desc = appCategory_desc;
	}
	
	public String getAppCategory() {
		return appCategory;
	}
	public void setAppCategory(String appCategory) {
		this.appCategory = appCategory;
	}
	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}
	public String getProperty_svCurrentYear() {
		return property_svCurrentYear;
	}
	public void setProperty_svCurrentYear(String property_svCurrentYear) {
		this.property_svCurrentYear = property_svCurrentYear;
	}
	public String getProperty_civCurrentYear() {
		return property_civCurrentYear;
	}
	public void setProperty_civCurrentYear(String property_civCurrentYear) {
		this.property_civCurrentYear = property_civCurrentYear;
	}
	public String getProperty_zoning() {
		return property_zoning;
	}
	public void setProperty_zoning(String property_zoning) {
		this.property_zoning = property_zoning;
	}
	public String getZonecodesTbl() {
		return zonecodesTbl;
	}
	public void setZonecodesTbl(String zonecodesTbl) {
		this.zonecodesTbl = zonecodesTbl;
	}
	public String getZonecodes_zoneCode() {
		return zonecodes_zoneCode;
	}
	public void setZonecodes_zoneCode(String zonecodes_zoneCode) {
		this.zonecodes_zoneCode = zonecodes_zoneCode;
	}
	public String getZonecodes_group1() {
		return zonecodes_group1;
	}
	public void setZonecodes_group1(String zonecodes_group1) {
		this.zonecodes_group1 = zonecodes_group1;
	}
	public String getPlanOverlay() {
		return planOverlay;
	}
	public void setPlanOverlay(String planOverlay) {
		this.planOverlay = planOverlay;
	}
	public String getPlanOverlay_zoneCode() {
		return planOverlay_zoneCode;
	}
	public void setPlanOverlay_zoneCode(String planOverlay_zoneCode) {
		this.planOverlay_zoneCode = planOverlay_zoneCode;
	}
	public String getPlanCodes() {
		return planCodes;
	}
	public void setPlanCodes(String planCodes) {
		this.planCodes = planCodes;
	}
	public String getPlanCodes_zoneCode() {
		return planCodes_zoneCode;
	}
	public void setPlanCodes_zoneCode(String planCodes_zoneCode) {
		this.planCodes_zoneCode = planCodes_zoneCode;
	}
	public String getPlanCodes_group1() {
		return planCodes_group1;
	}
	public void setPlanCodes_group1(String planCodes_group1) {
		this.planCodes_group1 = planCodes_group1;
	}
	public String getTrainStation() {
		return trainStation;
	}
	public void setTrainStation(String trainStation) {
		this.trainStation = trainStation;
	}
	public String getTrainRoute() {
		return trainRoute;
	}
	public void setTrainRoute(String trainRoute) {
		this.trainRoute = trainRoute;
	}
	public String getTramRoute() {
		return tramRoute;
	}
	public void setTramRoute(String tramRoute) {
		this.tramRoute = tramRoute;
	}
	public String getEducationFacilities() {
		return educationFacilities;
	}
	public void setEducationFacilities(String educationFacilities) {
		this.educationFacilities = educationFacilities;
	}
	public String getRecreationFacilities() {
		return recreationFacilities;
	}
	public void setRecreationFacilities(String recreationFacilities) {
		this.recreationFacilities = recreationFacilities;
	}
	public String getMedicalFacilities() {
		return medicalFacilities;
	}
	public void setMedicalFacilities(String medicalFacilities) {
		this.medicalFacilities = medicalFacilities;
	}
	public String getCommunityFacilities() {
		return communityFacilities;
	}
	public void setCommunityFacilities(String communityFacilities) {
		this.communityFacilities = communityFacilities;
	}
	public String getUtilityFacilities() {
		return utilityFacilities;
	}
	public void setUtilityFacilities(String utilityFacilities) {
		this.utilityFacilities = utilityFacilities;
	}
}