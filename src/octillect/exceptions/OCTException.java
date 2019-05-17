package octillect.exceptions;

public class OCTException extends Exception {

    //Error Fields
    private String errorCode;
    private String errorDescription;

    public OCTException(String errorCode, String errorDescription) {
        this.errorCode        = errorCode;
        this.errorDescription = errorDescription;
    }

    public void printError() {
        System.out.println("[" + errorCode + "]: " + errorDescription);
    }
}