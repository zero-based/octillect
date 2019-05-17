package octillect.exceptions;

public class InvalidFirebaseKeyException extends OCTException {

    public static String CODE        = "401";
    public static String DESCRIPTON  = "Invalid Firebase Key";

    public InvalidFirebaseKeyException() {
        super(CODE, DESCRIPTON);
    }
}
