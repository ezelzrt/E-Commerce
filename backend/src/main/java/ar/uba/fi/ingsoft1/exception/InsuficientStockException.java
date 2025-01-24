package ar.uba.fi.ingsoft1.exception;

public class InsuficientStockException extends RuntimeException {
    public InsuficientStockException(String message) {
        super(message);
    }

    public InsuficientStockException(String message, Throwable cause) {
        super(message, cause);
    }
}
