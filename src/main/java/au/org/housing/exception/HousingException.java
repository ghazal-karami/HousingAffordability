package au.org.housing.exception;

/**
 * Responsible for handling any kind of exception
 *
 * @author Gh.Karami
 * @version 1.0
 *
 */ 
public class HousingException extends Exception {
	/** The Constant serialVersionUID. */
	  private static final long serialVersionUID = -1116820426271144835L;

	  /**
	   * Instantiates a new layer invalid exception.
	   */
	  public HousingException() {
	  }

	  /**
	   * Instantiates a new wif invalid input exception.
	   *
	   * @param message the message
	   */
	  public HousingException(String message) {
	    super(message);
	  }

	  /**
	   * Instantiates a new wif invalid input exception.
	   *
	   * @param cause the cause
	   */
	  public HousingException(Throwable cause) {
	    super(cause);
	  }

	  /**
	   * Instantiates a new wif invalid input exception.
	   *
	   * @param message the message
	   * @param cause the cause
	   */
	  public HousingException(String message, Throwable cause) {
	    super(message, cause);
	  }
}
