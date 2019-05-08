package octillect.controllers.settings;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import octillect.controllers.ApplicationController;
import octillect.controllers.BoardController;
import octillect.controllers.Injectable;
import octillect.controls.CommitCell;
import octillect.models.Commit;
import octillect.models.builders.CommitBuilder;
import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public class GitHubRepositoryController implements Injectable<ApplicationController> {

    //FXML Fields
    @FXML public ListView<Commit> commitsHistoryListView;
    @FXML public Label repositoryNameLabel;
    @FXML public Label noGitHubFileFoundLabel;

    // Injected Controllers
    private ApplicationController applicationController;
    private BoardController boardController;


    @Override
    public void inject(ApplicationController applicationController) {
        this.applicationController = applicationController;
        boardController            = applicationController.boardController;
    }

    @Override
    public void init() {
        commitsHistoryListView.setCellFactory(param -> new CommitCell());
    }

    public void loadGithubRepository() {

        try {

            GitHub github = GitHub.connect();
            GHRepository repo = github.getRepository(boardController.currentBoard.getRepositoryName());

            int commitsCounter = 0;
            ObservableList<Commit> commits = FXCollections.observableArrayList();

            for (GHCommit commit : repo.listCommits()) {

                if (commitsCounter > 15) {
                    break;
                }

                String message = commit.getCommitShortInfo().getMessage();
                int index = message.indexOf('\n');
                String subject = index == -1 ? message : message.substring(0, index);
                String body = index == -1 ? null : message.substring(index + 2);

                String authorName = commit.getAuthor().getName();
                Date date = commit.getCommitDate();
                String url = commit.getHtmlUrl().toString();

                Commit commitModel = new CommitBuilder().with($ -> {
                    $.subject = subject;
                    $.body = body;
                    $.authorName = authorName;
                    $.date = date;
                    $.url = url;
                }).build();

                commits.add(commitModel);
                commitsCounter++;
            }

            noGitHubFileFoundLabel.setOpacity(0);
            commitsHistoryListView.setItems(commits);
            repositoryNameLabel.setText(boardController.currentBoard.getRepositoryName());

        } catch (IOException e) {
            noGitHubFileFoundLabel.setOpacity(1);
        }

    }

}