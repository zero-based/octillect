package octillect.controllers.dialogs;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.events.JFXDialogEvent;

import java.util.Calendar;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import octillect.controllers.application.ApplicationController;
import octillect.controllers.application.BoardController;
import octillect.controllers.util.Injectable;
import octillect.controllers.util.PostLoad;
import octillect.controls.OButton;
import octillect.controls.validators.RequiredValidator;
import octillect.controls.validators.ValidationManager;
import octillect.database.repositories.ColumnRepository;
import octillect.database.repositories.TaskRepository;
import octillect.database.firebase.FirestoreAPI;
import octillect.models.Column;
import octillect.models.Collaborator;
import octillect.models.Tag;
import octillect.models.Task;
import octillect.models.builders.CollaboratorBuilder;
import octillect.models.builders.TaskBuilder;

import org.apache.commons.lang3.StringUtils;

public class NewTaskDialogController implements Injectable<ApplicationController> {

    // Local Fields
    public Column currentColumn;

    // FXML Fields
    @FXML public JFXDialog newTaskDialog;
    @FXML public JFXTextField newTaskNameTextField;
    @FXML public JFXTextArea newTaskDescriptionTextArea;
    @FXML public OButton addTaskButton;

    // Validators
    private RequiredValidator requiredValidator;

    // Injected Controllers
    private ApplicationController applicationController;
    private BoardController boardController;

    @Override
    public void inject(ApplicationController applicationController) {
        this.applicationController = applicationController;
        boardController            = applicationController.boardController;
    }

    @PostLoad
    public void initValidators() {
        requiredValidator = new RequiredValidator();
        ValidationManager.addValidator(true, requiredValidator, newTaskNameTextField);
    }

    @FXML
    public void handleAddTaskButtonAction(ActionEvent actionEvent) {

        newTaskNameTextField.validate();

        if (!requiredValidator.getHasErrors()) {

            Collaborator creator = new CollaboratorBuilder().with($ -> {
                $.id    = applicationController.user.getId();
                $.name  = applicationController.user.getName();
                $.email = applicationController.user.getEmail();
                $.image = applicationController.user.getImage();
                $.role  = Collaborator.Role.owner;
            }).build();

            Task newTask = new TaskBuilder().with($ -> {

                $.tags = getMentionedTags();
                $.assignees = getMentionedAssignees();
                removeExtraSpaces();

                $.id = FirestoreAPI.getInstance().encryptWithDateTime(newTaskNameTextField.getText() + applicationController.user.getId());
                $.name = newTaskNameTextField.getText();
                $.description = newTaskDescriptionTextArea.getText();
                $.creationDate = Calendar.getInstance().getTime();
                $.creator = creator;
            }).build();

            currentColumn.<Task>getChildren().add(newTask);

            ColumnRepository.getInstance().addTask(currentColumn.getId(), newTask.getId());
            TaskRepository.getInstance().add(newTask);

            newTaskDialog.close();
        }
    }

    @FXML
    public void handleNewTaskDialogClosed(JFXDialogEvent jfxDialogEvent) {
        newTaskNameTextField.resetValidation();
        newTaskNameTextField.setText(null);
        newTaskDescriptionTextArea.setText(null);
    }

    private ObservableList<Tag> getMentionedTags() {

        int nTagsMentioned = StringUtils.countMatches(newTaskNameTextField.getText(), "#");
        ObservableList<Tag> mentionedTags = FXCollections.observableArrayList();

        if (boardController.currentBoard.getTags().isEmpty()) {
            return mentionedTags;
        }

        for (Tag tag : boardController.currentBoard.getTags()) {
            if (nTagsMentioned == 0) {
                break;
            }

            if (newTaskNameTextField.getText().contains("#" + tag.getName())) {
                mentionedTags.add(tag);
                nTagsMentioned--;
                newTaskNameTextField.setText(newTaskNameTextField.getText().replace(("#" + tag.getName()), ""));
            }
        }

        return mentionedTags;
    }

    private ObservableList<Collaborator> getMentionedAssignees() {

        int nAssigneesMentioned = StringUtils.countMatches(newTaskNameTextField.getText(), "@");
        ObservableList<Collaborator> mentionedAssignees = FXCollections.observableArrayList();

        if (boardController.currentBoard.getCollaborators().isEmpty()) {
            return mentionedAssignees;
        }

        for (Collaborator collaborator : boardController.currentBoard.getCollaborators()) {
            if (nAssigneesMentioned == 0) {
                break;
            }

            if (newTaskNameTextField.getText().contains("@" + collaborator.getName())) {
                mentionedAssignees.add(collaborator);
                nAssigneesMentioned--;
                newTaskNameTextField.setText(newTaskNameTextField.getText().replace(("@" + collaborator.getName()), ""));
            }
        }

        return mentionedAssignees;
    }

    private void removeExtraSpaces() {
        // Replace Extra Spaces with one space.
        String regex = "(\\s+)";
        newTaskNameTextField.setText(newTaskNameTextField.getText().replaceAll(regex, " "));
        
        // Remove Leading and Trailing whitespaces from Task's Name.
        newTaskNameTextField.setText(newTaskNameTextField.getText().trim());
    }
}
