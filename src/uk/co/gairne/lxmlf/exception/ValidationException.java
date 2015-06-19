package uk.co.gairne.lxmlf.exception;

public class ValidationException extends RuntimeException {
	
	private static final long serialVersionUID = -5786927717419206592L;

	public ValidationException() {
		super();
	}
	
	public ValidationException(String message) {
		super(message);
	}
	
	public ValidationException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public ValidationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
