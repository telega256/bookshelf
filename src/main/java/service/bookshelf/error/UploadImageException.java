package service.bookshelf.error;

public class UploadImageException extends RuntimeException {
    public UploadImageException() {
        super();
    }

    public UploadImageException(String message) {
        super(message);
    }

    public UploadImageException(String message, Throwable cause) {
        super(message, cause);
    }
}
