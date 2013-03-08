package au.org.housing.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Input layers configuration from layer name to its attributes
 * 
 * @author Gh.Karami
 * @version 1.0
 *
 */

@Component
public class InputLayersConfig{

	@Value("${_ppars}") 
	private String ppars ;	

	@Value("${pparsDuration}") 
	private String pparsDuration ;	

	@Value("${pparsObjections}") 
	private String pparsObjections ;	
	
	@Value("${pparsFurtherinfo}") 
	private String pparsFurtherinfo ;	
	
	@Value("${pparsPublicNotice}") 
	private String pparsPublicNotice ;	
	
	@Value("${pparsReferralIssue}") 
	private String pparsReferralIssue ;	
	
	@Value("${pparsCategory}") 
	private String pparsCategory ;	
	
	@Value("${pparsNumOfDwelling}") 
	private String pparsNumOfDwelling ;	
	
	@Value("${pparsCurrentUse}") 
	private String pparsCurrentUse ;
	
	@Value("${pparsProposedUse}") 
	private String pparsProposedUse ;
	
	@Value("${pparsEstimatedCostOfWork}") 
	private String pparsEstimatedCostOfWork ;
	
	@Value("${pparsPreMeeting}") 
	private String pparsPreMeeting ;
	
	@Value("${pparsOutcome}") 
	private String pparsOutcome ;
	
	
	
	
///////////////////////////////////////	
	@Value("${property}") 
	private String property ;

	@Value("${property_svCurrentYear}") 
	private String property_svCurrentYear ;

	@Value("${property_civCurrentYear}") 
	private String property_civCurrentYear ;

	@Value("${property_zoning}") 
	private String property_zoning ;

	@Value("${propertyLgaCode}") 
	private String propertyLgaCode ;
	
	@Value("${zonecodesTbl}") 
	private String zonecodesTbl ;

	@Value("${zonecodes_zoneCode}") 
	private String zonecodes_zoneCode ;

	@Value("${zonecodes_group1}") 
	private String zonecodes_group1;

	@Value("${planOverlay}") 
	private String planOverlay ;

	@Value("${planOverlay_zoneCode}") 
	private String planOverlay_zoneCode ;

	@Value("${planCodes}") 
	private String planCodes;

	@Value("${planCodes_zoneCode}") 
	private String planCodes_zoneCode;

	@Value("${planCodes_group1}") 
	private String planCodes_group1 ;

	@Value("${trainStation}") 
	private String trainStation ;

	@Value("${trainRoute}") 
	private String trainRoute ;

	@Value("${tramRoute}") 
	private String tramRoute ;

	@Value("${busRoute}") 
	private String busRoute ;
	
	@Value("${educationFacilities}") 
	private String educationFacilities;

	@Value("${recreationFacilities}") 
	private String recreationFacilities;

	@Value("${medicalFacilities}") 
	private String medicalFacilities ;

	@Value("${communityFacilities}") 
	private String communityFacilities;

	@Value("${utilityFacilities}") 
	private String utilityFacilities;

	@Value("${appCategory}") 
	private String appCategory ;

	@Value("${appCategory_code}") 
	private String appCategory_code ;

	@Value("${appCategory_desc}") 
	private String appCategory_desc ;

	@Value("${appOutcome}") 
	private String appOutcome ;

	@Value("${appOutcome_code}") 
	private String appOutcome_code;

	@Value("${appOutcome_desc}") 
	private String appOutcome_desc;

	public String getPpars() {
		return ppars;
	}
	public void setPpars(String ppars) {
		this.ppars = ppars;
	}

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
	public String getPparsDuration() {
		return pparsDuration;
	}
	public void setPparsDuration(String pparsDuration) {
		this.pparsDuration = pparsDuration;
	}
	public String getPparsObjections() {
		return pparsObjections;
	}
	public void setPparsObjections(String pparsObjections) {
		this.pparsObjections = pparsObjections;
	}
	public String getPparsFurtherinfo() {
		return pparsFurtherinfo;
	}
	public void setPparsFurtherinfo(String pparsFurtherinfo) {
		this.pparsFurtherinfo = pparsFurtherinfo;
	}
	public String getPparsPublicNotice() {
		return pparsPublicNotice;
	}
	public void setPparsPublicNotice(String pparsPublicNotice) {
		this.pparsPublicNotice = pparsPublicNotice;
	}
	public String getPparsReferralIssue() {
		return pparsReferralIssue;
	}
	public void setPparsReferralIssue(String pparsReferralIssue) {
		this.pparsReferralIssue = pparsReferralIssue;
	}
	public String getPparsCategory() {
		return pparsCategory;
	}
	public void setPparsCategory(String pparsCategory) {
		this.pparsCategory = pparsCategory;
	}
	public String getPparsNumOfDwelling() {
		return pparsNumOfDwelling;
	}
	public void setPparsNumOfDwelling(String pparsNumOfDwelling) {
		this.pparsNumOfDwelling = pparsNumOfDwelling;
	}
	public String getPparsCurrentUse() {
		return pparsCurrentUse;
	}
	public void setPparsCurrentUse(String pparsCurrentUse) {
		this.pparsCurrentUse = pparsCurrentUse;
	}
	public String getPparsProposedUse() {
		return pparsProposedUse;
	}
	public void setPparsProposedUse(String pparsProposedUse) {
		this.pparsProposedUse = pparsProposedUse;
	}
	public String getPparsEstimatedCostOfWork() {
		return pparsEstimatedCostOfWork;
	}
	public void setPparsEstimatedCostOfWork(String pparsEstimatedCostOfWork) {
		this.pparsEstimatedCostOfWork = pparsEstimatedCostOfWork;
	}
	public String getPparsPreMeeting() {
		return pparsPreMeeting;
	}
	public void setPparsPreMeeting(String pparsPreMeeting) {
		this.pparsPreMeeting = pparsPreMeeting;
	}
	public String getPparsOutcome() {
		return pparsOutcome;
	}
	public void setPparsOutcome(String pparsOutcome) {
		this.pparsOutcome = pparsOutcome;
	}
	public String getPropertyLgaCode() {
		return propertyLgaCode;
	}
	public void setPropertyLgaCode(String propertyLgaCode) {
		this.propertyLgaCode = propertyLgaCode;
	}
	
	public String getBusRoute() {
		return busRoute;
	}
	public void setBusRoute(String busRoute) {
		this.busRoute = busRoute;
	}
}