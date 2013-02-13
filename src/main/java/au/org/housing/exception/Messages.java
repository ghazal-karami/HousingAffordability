package au.org.housing.exception;

public class Messages {
	
	public final static String _CONN_POSTGIS_FAILED = "Connetion To PostGIS Database Failed";
	public final static String _NOT_METRIC = "Layer Is Not Metric!";
	public final static String _NOT_POLYGON = "Layer Is Not Polygon!";
	public final static String _NOT_POINT = "Layer Is Not Point!";
	public final static String _NOT_LINE = "Layer Is Line!";
	public final static String _NOT_HAVE_REQUIRED_FIELDS = "Layer Does Not Contain Required Attributes!";
	public final static String _NOT_FIND_REQUIRED_Layer = "Layer Is Not found In DataStore!";
	public final static String _NOT_VALID = "Geometry Is Not Valid!";
	

	public final static String _NO_FEATURE = "No Feature to Display On The map!";
	public final static String _SUCCESS = "Analysis Successfully Done";
	public final static String _ERROR = "Error";
	

	public final static String _GEOSERVER_NOT_EXIST = "Geoserver Does Not Exist!";
	public final static String _PUBLISH_FAILED = "Unable To Publish The New Output Layer To Geoserver";
	
	private static String message = Messages._SUCCESS;

	public static String getMessage() {
		return message;
	}

	public static void setMessage(String message) {
		Messages.message = message;
	}

}
