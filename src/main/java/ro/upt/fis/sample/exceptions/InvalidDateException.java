package ro.upt.fis.sample.exceptions;

public class InvalidDateException extends RuntimeException {

    public InvalidDateException(String message) {
        System.out.println(message);
    }
}
