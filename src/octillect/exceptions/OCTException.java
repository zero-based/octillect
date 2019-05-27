package octillect.exceptions;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class OCTException extends Exception {

    // Local Fields
    private String errorCode;
    private String errorDescription;

    // FXML Fields
    @FXML private StackPane errorStackPane;
    @FXML public Label errorLabel;
    @FXML public Button goBackButton;


    public OCTException(String errorCode, String errorDescription) {
        this.errorCode        = errorCode;
        this.errorDescription = errorDescription;
    }

    public void dislpayError(StackPane root) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/octillect/exceptions/ErrorView.fxml"));
            fxmlLoader.setController(this);
            errorStackPane = fxmlLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        errorLabel.setText("[" + errorCode + "]: " + errorDescription);
        goBackButton.setOnAction(event -> {
            /*TODO: Add retry method here*/
        });
        root.getChildren().add(errorStackPane);
    }

    public void printError() {
        System.out.println("[" + errorCode + "]: " + errorDescription);
    }
}