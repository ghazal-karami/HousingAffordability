package au.org.housing.exception;

public class LayerValidationException extends Exception{
	/** The Constant serialVersionUID. */
	  private static final long serialVersionUID = -1116820426271144835L;

	  /**
	   * Instantiates a new layer invalid exception.
	   */
	  public LayerValidationException() {
	  }

	  /**
	   * Instantiates a new wif invalid input exception.
	   *
	   * @param message the message
	   */
	  public LayerValidationException(String message) {
	    super(message);
	  }

	  /**
	   * Instantiates a new wif invalid input exception.
	   *
	   * @param cause the cause
	   */
	  public LayerValidationException(Throwable cause) {
	    super(cause);
	  }

	  /**
	   * Instantiates a new wif invalid input exception.
	   *
	   * @param message the message
	   * @param cause the cause
	   */
	  public LayerValidationException(String message, Throwable cause) {
	    super(message, cause);
	  }
}
