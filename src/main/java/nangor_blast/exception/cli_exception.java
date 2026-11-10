package nangor_blast.exception;

public class cli_exception extends Exception {

    public cli_exception(String message) {
        super(message);
    }

    public cli_exception(String message, Throwable cause) {
        super(message, cause);
    }
}