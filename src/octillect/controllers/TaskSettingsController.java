package octillect.controllers;

public class TaskSettingsController implements Injectable<ApplicationController> {

    // Injected Controllers
    private ApplicationController applicationController;

    @Override
    public void inject(ApplicationController applicationController) {
        this.applicationController = applicationController;
    }

}