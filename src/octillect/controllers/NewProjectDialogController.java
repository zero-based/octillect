package octillect.controllers;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;
import octillect.controls.OButton;

public class NewProjectDialogController {
    @FXML private JFXTextField newProjectNameTextField;
    @FXML private JFXTextArea newProjectDescriptionTextArea;
    @FXML private OButton addProjectButton;

    public void handleAddProjectButtonAction(ActionEvent actionEvent) {
    }

    public void showNewProjectDialog(StackPane rootContainer) {
        try {
            JFXDialog newProjectDialog = FXMLLoader.load(getClass().getResource("/octillect/views/NewProjectDialogView.fxml"));
            newProjectDialog.show(rootContainer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
