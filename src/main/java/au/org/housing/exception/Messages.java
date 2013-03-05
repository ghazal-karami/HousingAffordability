package au.org.housing.exception;

/**
 * Contains any kind of message to represent to 
 * the user
 *
 * @author Gh.Karami
 * @version 1.0
 *
 */ 

public class Messages {

	public final static String _CONN_POSTGIS_FAILED = "Connetion To PostGIS Database Failed!";
	public final static String _NO_PPARS_FEATURESOURCE = "No FeatureSource To PPARS DataSet Found!";
	public final static String _NO_FEATURESOURCE_FOR = "No FeatureSource Found For ";
	public final static String _NO_PROPERTY_FEATURESOURCE = "No FeatureSource To Property DataSet Found!";


	public final static String _NOT_METRIC = "Layer Is Not Metric!";
	public final static String _NOT_POLYGON = "Layer Is Not Polygon!";
	public final static String _NOT_POINT = "Layer Is Not Point!";
	public final static String _NOT_LINE = "Layer Is Not Line!";
	public final static String _NOT_HAVE_REQUIRED_FIELDS = "Layer Does Not Contain Required Attributes!";
	public final static String _NOT_FIND_REQUIRED_Layer = "Layer Is Not found In DataStore!";
	
	public final static String _ERROR_PROPERTY_LAYER_VALIDATION = "Error Validating Property Layer!";
	public final static String _ERROR_MERCATOR_CHECK = "Error Checking Layer To Be Marcator!";
	public final static String _ERROR_PLANOVERLAY_LAYER_VALIDATION = "Error Validating PlanOverlay Layer!";
	public final static String _ERROR_PLANCODELIST_LAYER_VALIDATION = "Error Validating PlanCodeList dbf Table!";
	public final static String _ERROR_ZONECODELIST_LAYER_VALIDATION = "Error Validating ZoneCodeList dbf Table!";
	public final static String _ERROR_POLYGON_VALIDATION = "Error Checking Layer To Be Polygon!";
	public final static String _ERROR_LINE_VALIDATION = "Error Checking Layer To Be Line!";
	public final static String _ERROR_POINT_VALIDATION = "Error Checking Layer To Be Point!";
	public final static String _ERROR_FETCH_FIRST_FEATURE_OF = "Error Fetching First Feature Of";
	
	public final static String _ERROR_LGA_FILTER = "Error Creating LGA Filter!";
	public final static String _ERROR_DPI_FILTER = "Error Creating DPI Filter!";
	public final static String _ERROR_LANDUSE_FILTER = "Error Creating LandUse Filter!";
	public final static String _ERROR_TRANSPORT_FACILITY_FILTER = "Error Creating Transportaion And Facilities Filter!";
	public final static String _ERROR_OWNERSHIP_FILTER = "Error Creating Ownership Filter!";
	public final static String _ERROR_PROPERTY_FILTER = "Error Creating Property Filter!";
	public final static String _ERROR_OVERLAY_INTERSECTION = "Error Intersecting Selected Overlays With Property Layer Selected Features!";
	public final static String _ERROR_OVERLAY_FEATURELIST = "Error Fetching Overlay Feature List!";
	
	
	public final static String _ERROR_PPARS_FILTER = "Error Creating Property Filter!";
	
	
	public final static String _NOT_VALID_GEOMETRY = "Geometry Is Not Valid!";

	public final static String _GEOSERVER_NOT_RUNNING = "Geoserver Is Not Running!";
	public final static String _ERROR_CONNECT_GEOSERVER = "Error Connecting To GeoServer!";
	public final static String _PUBLISH_FAILED = "Unable To Publish The New Output Layer To Geoserver!";
	public final static String _WORKSPACE_NOT_EXIST = "Workspace housingWS Does Not Exist In GeoServer!";
	public final static String _WORKSPACE_NOT_EXIST_BUT_CREATED = "Workspace Does Not Exist In GeoServer, So Created!";

	public final static String _EXPORT_TO_SHP_UNSUCCESSFULL= "Export To Shape File Was Unsuccessfull!";
	public final static String _NO_FEATURE = "No Feature to Display On The Map!";
	public final static String _NO_PPARS_FOUND = "No Feature Found in PPARS Layer!";

	public final static String _SUCCESSFULLY_DONE = "Analysis Successfully Done";
	public final static String _SUCCESS = "Success";
	public final static String _ERROR = "Error";

	public final static String _SELECT_PARAM_TO_CONTINUE = "Please Select A Parameter To Continue!";
	public final static String _ERROR_GENERATE_BUFFER = "Error Generating Buffer!";
	public final static String _ERROR_GENERATE_UNION = "Error Generating Union!";

}
