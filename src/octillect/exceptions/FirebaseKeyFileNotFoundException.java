package octillect.exceptions;

public class FirebaseKeyFileNotFoundException extends OCTException {

    public static String CODE        = "606";
    public static String DESCRIPTON  = "Couldn't find firebase-key.json";

    public FirebaseKeyFileNotFoundException() {
        super(CODE, DESCRIPTON);
    }
}
