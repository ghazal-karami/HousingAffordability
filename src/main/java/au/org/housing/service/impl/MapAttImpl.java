package au.org.housing.service.impl;

public class MapAttImpl {

	// *************** Property Layer ***************

	//------------------  CSDILA  -------------------

//	static String property = "Victoria:Property_Value_5LGA"; //ok , csdila

	//	static String property = "PTV:stopinformation_victoria";//for test
	//	static String property = "sii:BUILDINGREG.BUSHFIRE_PRONE_AREA";//for test
	//	static String property = "CoM:com_capacities_2010";//for test
	//	static String property = "cfer:AusBySLA";//for test


	//------------------  DSE  -------------------

	//	static String planOverlay = "sii:VMPLAN.PLAN_OVERLAY";//ok , dse

	//	static String trainStation = "sii:DPS_932_RAIL_STATIONS_VMT";//ok , dse

	//	static String trainRoute = "sii:DPS_1202_TRAIN_CORR_CENTL";//ok , dse

	//	static String tramRoute = "sii:DPS_969_TRAM_ROUTES_VMT";//ok , dse

	//	static String featuresofInterest_layername_inDSE_WFS= "sii:VMFEAT.FOI_POLYGON";//ok , dse


	//------------------  PostGis  -------------------

//		static String property = "Property";
//		static String property = "Property_Metric";
		static String property = "Victoria:Property_Value_NWMRLGA"; // 14 lga
//		static String property = "CoM:com_capacities_2010"; // 14 lga
//		static String property = "PTV:stopinformation_victoria"; // 14 lga
//		static String property = "Victoria:Property_Value_5LGA"; // 14 lga
		
	static String property_svCurrentYear = "sv_current_year";
//	static String property_svCurrentYear = "objectid";
	static String property_civCurrentYear = "civ_current_year";
//	static String property_civCurrentYear = "block_id";
	static String property_zoning = "zoning";

	static String zonecodesTbl = "tbl_zonecodes";
	static String zonecodes_zoneCode = "zone_code";
	static String zonecodes_group1 = "group1";

//	static String planOverlay = "plan_overlay";
//	static String planOverlay = "plan_overlay_shp";
	
	static String planOverlay = "Plan_Overlay";
//	static String planOverlay = "plan_overlay";
//	static String planOverlay = "plan_overlay_arcgis_metric";
	static String planOverlay_zoneCode = "zone_code";

	static String planCodes = "plan_codelist";
	static String planCodes_zoneCode = "zone_code";
	static String planCodes_group1 = "group1";
//
//	static String trainStation = "train_station";
//	static String trainRoute = "train_route";
//	static String tramRoute = "tram_route";
	static String trainStation = "train_station_arcgis_metric";
	static String trainRoute = "train_route_arcgis_metric";
	static String tramRoute = "tram_route_arcgis_metric";

	static String educationFacilities = "education_facilities";
	static String recreationFacilities = "recreation_facilities";
	static String medicalFacilities = "medical_facilities";
	static String communityFacilities = "community_facilities";
	static String utilityFacilities = "utility_facilities";
//	static String educationFacilities = "education_facilities_arcgis_metric";
//	static String recreationFacilities = "recreation_facilities_arcgis_metric";
//	static String medicalFacilities = "medical_facilities_arcgis_metric";
//	static String communityFacilities = "community_facilities_arcgis_metric";
//	static String utilityFacilities = "utility_facilities_arcgis_metric";


	//------------------  Shapefile  -------------------	
	//	static String property_svCurrentYear = "SV_current";

	//static String zonecodesTbl = "tbl_zonecodes";
	//static String zonecodes_zoneCode = "ZONE_CODE_";
	//static String zonecodes_group1 = "GROUP1";

	//	static String planOverlay = "plan_overlay_shp";

	//	static String planCodes = "plan_codelist";
	//	static String planCodes_zoneCode = "ZONE_CODE";
	//	static String planCodes_group1 = "GROUP1";
}
