package octillect.controllers.settings;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import octillect.controllers.ApplicationController;
import octillect.controllers.Injectable;
import octillect.controls.CommitCell;
import octillect.models.Commit;
import octillect.models.builders.CommitBuilder;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class GitHubRepositoryController implements Injectable<ApplicationController> {

    //FXML Fields
    @FXML public ListView<Commit> commitsHistoryListView;

    // Injected Controllers
    private ApplicationController applicationController;

    @Override
    public void inject(ApplicationController applicationController) {
        this.applicationController = applicationController;
    }

    @Override
    public void init() {

    }

    @FXML
    public void initialize() {
        /* TODO: Remove this temporary commits */
        Commit commit1 = new CommitBuilder().with($_commit1 -> {
            $_commit1.subject = "This is a very long sentence where its length exceeds 50 characters";
            $_commit1.authorName = "MonicaTanios";
            $_commit1.body = "*Lorem Ipusm \n*Lorem Ipusm";
            $_commit1.date = new GregorianCalendar(2015, Calendar.JUNE, 1).getTime();
            $_commit1.url = "https://github.com/MonicaTanios/octillect/commits/";
        }).build();
        Commit commit2 = new CommitBuilder().with($_commit2 -> {
            $_commit2.subject = "Improve Sign Up View";
            $_commit2.authorName = "MichaMedhat";
            $_commit2.body = "*Lorem Ipusm \n*Lorem Ipusm";
            $_commit2.date = new GregorianCalendar(2016, Calendar.MAY, 21).getTime();
            $_commit2.url = "https://github.com/MonicaTanios/octillect/commits/";
        }).build();
        Commit commit3 = new CommitBuilder().with($_commit3 -> {
            $_commit3.subject = "Improve Sign In View";
            $_commit3.authorName = "Michael Safwat";
            $_commit3.body = "*Lorem Ipusm \n*Lorem Ipusm \n*Lorem Ipusm";
            $_commit3.date = new GregorianCalendar(2016, Calendar.MAY, 21).getTime();
            $_commit3.url = "https://github.com/MonicaTanios/octillect/commits/";
        }).build();
        Commit commit4 = new CommitBuilder().with($_commit4 -> {
            $_commit4.subject = "Sign Up View";
            $_commit4.authorName = "Monica Atef";
            $_commit4.body = "*Lorem Ipusm \n*Lorem Ipusm";
            $_commit4.date = new GregorianCalendar(2016, Calendar.MAY, 21).getTime();
            $_commit4.url = "https://github.com/MonicaTanios/octillect/commits/";
        }).build();
        Commit commit5 = new CommitBuilder().with($_commit5 -> {
            $_commit5.subject = "Improve Sign Up View";
            $_commit5.authorName = "No One";
            $_commit5.body = "*Lorem Ipusm \n*Lorem Ipusm";
            $_commit5.date = new GregorianCalendar(2016, Calendar.MAY, 21).getTime();
            $_commit5.url = "https://github.com/MonicaTanios/octillect/commits/";
        }).build();

        ObservableList<Commit> userCommits = FXCollections.observableArrayList(commit1, commit2, commit3, commit4, commit5);

        commitsHistoryListView.setItems(userCommits);
        commitsHistoryListView.setCellFactory(param -> new CommitCell() {
        });
    }

}