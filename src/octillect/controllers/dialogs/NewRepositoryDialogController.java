package octillect.controllers.dialogs;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import com.jfoenix.controls.events.JFXDialogEvent;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;

import octillect.controllers.ApplicationController;
import octillect.controllers.Injectable;
import octillect.controls.OButton;

public class NewRepositoryDialogController implements Injectable<ApplicationController> {

    //FXML Fields
    @FXML public JFXDialog newRepoDialog;
    @FXML public JFXTextField newRepositoryNameTextField;
    @FXML public OButton addRepositoryButton;

    // Injected Controllers
    private ApplicationController applicationController;

    @Override
    public void inject(ApplicationController applicationController) {
        this.applicationController = applicationController;
    }

    @Override
    public void init(){
    }

    @FXML
    public void handleNewRepoDialogClosed(JFXDialogEvent jfxDialogEvent) {
        newRepositoryNameTextField.setText("");
    }

    @FXML
    public void handleAddRepositoryButtonAction(ActionEvent actionEvent) {
        //Add github code

        newRepoDialog.close();
    }
}
