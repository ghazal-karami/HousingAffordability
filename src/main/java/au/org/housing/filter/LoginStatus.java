package au.org.housing.filter;

public class LoginStatus {
	private final boolean success;
	private final boolean loggedIn;
	private final String username;
	private final String errorMessage;

	public LoginStatus(boolean success, boolean loggedIn, String username,
			String errorMessage) {
		this.success = success;
		this.loggedIn = loggedIn;
		this.username = username;
		this.errorMessage = errorMessage;
	}
}
