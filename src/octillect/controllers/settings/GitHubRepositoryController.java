package octillect.controllers.settings;

import octillect.controllers.ApplicationController;
import octillect.controllers.Injectable;

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
