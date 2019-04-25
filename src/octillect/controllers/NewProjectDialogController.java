package octillect.controllers;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import octillect.controls.OButton;

public class NewProjectDialogController implements Injectable<ApplicationController> {

    // FXML Fields
    @FXML public JFXDialog newProjectDialog;
    @FXML public JFXTextField newProjectNameTextField;
    @FXML public JFXTextArea newProjectDescriptionTextArea;
    @FXML public OButton addProjectButton;

    // Injected Controllers
    private ApplicationController applicationController;
    private ProjectController projectController;

    @Override
    public void inject(ApplicationController applicationController) {
        this.applicationController = applicationController;
        projectController          = applicationController.projectController;
    }

    public void handleAddProjectButtonAction(ActionEvent actionEvent) {
    }

}
