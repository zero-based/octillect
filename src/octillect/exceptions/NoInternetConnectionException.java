package octillect.exceptions;

public class NoInternetConnectionException extends OCTException {

    public static String CODE       = "OCT404:";
    public static String DESCRIPTON = "Connection lost! Please check your internet connection";

    public NoInternetConnectionException() {
        super(CODE, DESCRIPTON);
    }

}