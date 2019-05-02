package octillect.controllers;

import javafx.event.ActionEvent;

public class UserSettingsController implements Injectable<ApplicationController> {

    // Injected Controllers
    private ApplicationController applicationController;

    @Override
    public void inject(ApplicationController applicationController) {
        this.applicationController = applicationController;
    }

}
