package octillect.exceptions;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class OCTException extends Exception {

    // Local Fields
    private String errorCode;
    private String errorDescription;

    // FXML Fields
    @FXML private VBox errorRootVBox;
    @FXML private Label errorLabel;
    @FXML private Button goBackButton;

    public OCTException(String errorCode, String errorDescription) {
        this.errorCode        = errorCode;
        this.errorDescription = errorDescription;
    }

    public void displayErrorView(StackPane root, IRetryStrategy retryStrategy) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/octillect/exceptions/ErrorView.fxml"));
            fxmlLoader.setController(this);
            errorRootVBox = fxmlLoader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
        errorLabel.setText("[" + errorCode + "]: " + errorDescription);
        goBackButton.setOnAction(event -> {
            if (retryStrategy.retry()) {
                root.getChildren().remove(errorRootVBox);
            }
        });
        root.getChildren().add(errorRootVBox);
    }

    public void printError() {
        System.err.println("[" + errorCode + "]: " + errorDescription);
    }
}