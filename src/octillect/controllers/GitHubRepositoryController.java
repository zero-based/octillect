package octillect.controllers;

public class GitHubRepositoryController implements Injectable<ApplicationController> {

    // Injected Controllers
    private ApplicationController applicationController;

    @Override
    public void inject(ApplicationController applicationController) {
        this.applicationController = applicationController;
    }

    @Override
    public void init() {
    }

}
