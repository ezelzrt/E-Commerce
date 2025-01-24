package ar.uba.fi.ingsoft1;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<String> handleTransactionException(TransactionSystemException ex) {
        Throwable cause = ex.getRootCause();
        
        String errorMessage = "Could not complete the operation";
        if (cause != null) {
            errorMessage = cause.getMessage();
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("Operation failed: " + errorMessage);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        // Log the exception (optional)
        ex.printStackTrace(); // or use a logging framework like Logback or SLF4J

        // Return a 500 Internal Server Error with a generic message
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("An unexpected error occurred. Please try again later.");
    }
}
