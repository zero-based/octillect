package octillect.controllers.dialogs;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import com.jfoenix.controls.events.JFXDialogEvent;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;

import octillect.controllers.ApplicationController;
import octillect.controllers.Injectable;
import octillect.controls.OButton;

public class EditColumnDialogController implements Injectable<ApplicationController> {

    @FXML public JFXDialog editColumnDialog;
    @FXML public JFXTextField editColumnTextField;
    @FXML public OButton editColumnButton;

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
    public void handleEditColumnDialogClosed(JFXDialogEvent jfxDialogEvent) {
    }

    @FXML
    public void handleEditColumnButtonAction(ActionEvent actionEvent) {
        editColumnDialog.close();
    }
}
