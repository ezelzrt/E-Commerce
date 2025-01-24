package ar.uba.fi.ingsoft1.exception;

public class InvalidStatusChangeException extends RuntimeException {
    public InvalidStatusChangeException(String message) {
        super(message);
    }

    public InvalidStatusChangeException(String message, Throwable cause) {
        super(message, cause);
    }
}
