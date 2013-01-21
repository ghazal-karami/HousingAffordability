package au.org.housing.exception;

public class Messages {
	
	public final static String _NOT_METRIC = "layer is not Metric!";
	public final static String _NOT_POLYGON = "layer is not Polygon!";
	public final static String _NOT_POINT = "layer is not Point!";
	public final static String _NOT_LINE = "layer is not Line!";
	public final static String _NOT_HAVE_REQUIRED_FIELDS = "layer does not contain required attributes!";
	public final static String _NOT_VALID = "Geometry is not valid!";
	public final static String _SUCCESS = "Analyse Done Successfully.";
	
	private static String message = Messages._SUCCESS;

	public static String getMessage() {
		return message;
	}

	public static void setMessage(String message) {
		Messages.message = message;
	}

}
