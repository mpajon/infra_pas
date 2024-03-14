package es.princast.gepep.web.rest.errors;

public class FileStorageException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 3615413325595991318L;

	public FileStorageException(String message) {
        super(message);
    }

    public FileStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}