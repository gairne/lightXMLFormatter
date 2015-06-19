package uk.co.gairne.lxmlf.exception;

public class ParserException extends RuntimeException {

	private static final long serialVersionUID = 3822918398602383735L;

	public ParserException() {
		super();
	}
	
	public ParserException(String message) {
		super(message);
	}
	
	public ParserException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public ParserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
