package ro.pagepo.sokoban.levels.exception;

public class InvalidXMLLevelPackException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3649687878418996422L;

	public InvalidXMLLevelPackException(String detailMessage,
			Throwable throwable) {
		super(detailMessage, throwable);
	}

	public InvalidXMLLevelPackException(String detailMessage) {
		super(detailMessage);
	}


	
}
