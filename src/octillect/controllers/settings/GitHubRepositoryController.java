package octillect.controllers.settings;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;

import octillect.controllers.ApplicationController;
import octillect.controllers.BoardController;
import octillect.controllers.dialogs.RepositoryNameDialogController;
import octillect.controllers.util.Injectable;
import octillect.controllers.util.PostLoad;
import octillect.controls.cells.CommitCell;
import octillect.models.Commit;
import octillect.models.builders.CommitBuilder;

import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

public class GitHubRepositoryController implements Injectable<ApplicationController> {

    // FXML Fields
    @FXML public VBox repositoryVBox;
    @FXML public ListView<Commit> commitsListView;
    @FXML public Label repositoryNameLabel;
    @FXML public Label invalidRepositoryLabel;
    @FXML public Label noGitHubFileFoundLabel;

    // Injected Controllers
    private ApplicationController applicationController;
    private BoardController boardController;
    private RepositoryNameDialogController repositoryNameDialogController;

    @Override
    public void inject(ApplicationController applicationController) {
        this.applicationController     = applicationController;
        boardController                = applicationController.boardController;
        repositoryNameDialogController = applicationController.repositoryNameDialogController;
    }

    @PostLoad
    public void initCellFactories() {
        commitsListView.setCellFactory(param -> new CommitCell());
    }

    public void loadGitHubRepository() {

        try {

            commitsListView.getItems().clear();
            repositoryNameLabel.setText(boardController.currentBoard.getRepositoryName());

            repositoryVBox.setOpacity(1);
            commitsListView.setOpacity(1);
            invalidRepositoryLabel.setOpacity(0);
            noGitHubFileFoundLabel.setOpacity(0);

            GitHub github = GitHub.connect();

            try {

                GHRepository repository = github.getRepository(boardController.currentBoard.getRepositoryName());

                int commitsLimit = 15;
                List<GHCommit> commits = repository.listCommits()._iterator(commitsLimit).nextPage();

                HashMap<String, ImagePattern> avatars = new HashMap<>();

                for (GHCommit commit : commits) {

                    URI url   = commit.getHtmlUrl().toURI();
                    Date date = commit.getCommitDate();

                    String message = commit.getCommitShortInfo().getMessage();
                    int index      = message.indexOf('\n');
                    String subject = index == -1 ? message : message.substring(0, index);
                    String body    = index == -1 ? null : message.substring(index + 2);

                    String authorUsername  = commit.getAuthor().getLogin();
                    String authorAvatarUrl = commit.getAuthor().getAvatarUrl().replace("s=460", "s=128");

                    ImagePattern authorAvatar;
                    if (avatars.containsKey(authorAvatarUrl)) {
                        authorAvatar = avatars.get(authorAvatarUrl);
                    } else {
                        authorAvatar = new ImagePattern(new Image(authorAvatarUrl));
                        avatars.put(authorAvatarUrl, authorAvatar);
                    }

                    Commit commitModel = new CommitBuilder().with($ -> {
                        $.url            = url;
                        $.subject        = subject;
                        $.body           = body;
                        $.authorUsername = authorUsername;
                        $.authorAvatar   = authorAvatar;
                        $.date           = date;
                    }).build();

                    commitsListView.getItems().add(commitModel);
                }

            } catch (IOException e) {
                // If the given Repository name is invalid
                commitsListView.setOpacity(0);
                invalidRepositoryLabel.setOpacity(1);
                noGitHubFileFoundLabel.setOpacity(0);
            } catch (URISyntaxException e) {
                e.getReason();
            }

        } catch (IOException e) {
            // If ~/.github is not found
            repositoryVBox.setOpacity(0);
            invalidRepositoryLabel.setOpacity(0);
            noGitHubFileFoundLabel.setOpacity(1);
        }

    }

    public void handleEditRepositoryNameMouseClicked(MouseEvent mouseEvent) {
        repositoryNameDialogController.repositoryNameTextField.setText(boardController.currentBoard.getRepositoryName());
        repositoryNameDialogController.repositoryNameDialog.show(applicationController.rootStackPane);
    }

}