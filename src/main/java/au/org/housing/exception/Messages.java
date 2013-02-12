package au.org.housing.exception;

public class Messages {
	
	public final static String _NOT_METRIC = "layer is not Metric!";
	public final static String _NOT_POLYGON = "layer is not Polygon!";
	public final static String _NOT_POINT = "layer is not Point!";
	public final static String _NOT_LINE = "layer is not Line!";
	public final static String _NOT_HAVE_REQUIRED_FIELDS = "layer does not contain required attributes!";
	public final static String _NOT_FIND_REQUIRED_LAYER = "layer is not found in datastrore!";
	public final static String _NOT_VALID = "Geometry is not valid!";
	public final static String _SUCCESS = "Analysis Successfully Done";
	public final static String _NO_FEATURE = "No feature to display on the map!";
	public final static String _GEOSERVER_NOT_EXIST = "Geoserver does not exist!";
	public final static String _ERROR = "Error";
	public final static String _CONN_POSTGIS_FAILED = "Connetion to postgis database failed";
	
	private static String message = Messages._SUCCESS;

	public static String getMessage() {
		return message;
	}

	public static void setMessage(String message) {
		Messages.message = message;
	}

}
