package ro.upt.fis.sample.exceptions;

public class EmptyInputException extends RuntimeException {

    public EmptyInputException(String message) {
        super(message);
    }
}
