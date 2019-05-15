package octillect.controllers.settings;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.util.StringConverter;

import octillect.controllers.ApplicationController;
import octillect.controllers.BoardController;
import octillect.controllers.Injectable;
import octillect.controllers.RightDrawerController;
import octillect.controls.ContributorCell;
import octillect.controls.Mode;
import octillect.controls.SubTaskCell;
import octillect.controls.TagCell;
import octillect.database.firebase.FirestoreAPI;
import octillect.database.repositories.ColumnRepository;
import octillect.database.repositories.TaskRepository;
import octillect.models.Board;
import octillect.models.Column;
import octillect.models.Contributor;
import octillect.models.Tag;
import octillect.models.Task;
import octillect.models.TaskBase;
import octillect.models.builders.TaskBuilder;
import octillect.styles.Palette;

import org.kordamp.ikonli.javafx.FontIcon;

public class TaskSettingsController implements Injectable<ApplicationController> {

    // Local Fields
    private Board currentBoard;
    public Column parentColumn;
    public Task currentTask;

    // FXML Fields
    @FXML public Circle taskCreatorImageCircle;
    @FXML public FontIcon isCompletedTaskIcon;
    @FXML public Label taskCreatorLabel;
    @FXML public Label taskCreationDateLabel;
    @FXML public JFXTextField taskNameTextField;
    @FXML public JFXTextField newSubTaskTextField;
    @FXML public JFXTextArea taskDescriptionTextArea;
    @FXML public JFXDatePicker taskDueDatePicker;
    @FXML public JFXListView<Task> subTasksListView;
    @FXML public JFXListView<Tag> taskTagsListView;
    @FXML public JFXListView<Contributor> taskAssigneesListView;
    @FXML public JFXComboBox<Tag> boardTagsComboBox;
    @FXML public JFXComboBox<Contributor> boardContributorsComboBox;

    // Injected Controllers
    private ApplicationController applicationController;
    private BoardController boardController;
    private RightDrawerController rightDrawerController;

    @Override
    public void inject(ApplicationController applicationController) {
        this.applicationController = applicationController;
        boardController            = applicationController.boardController;
        rightDrawerController      = applicationController.rightDrawerController;
    }

    @Override
    public void init() {

        // Cell Factories

        subTasksListView.setCellFactory(param -> {
            SubTaskCell subTaskCell = new SubTaskCell();
            subTaskCell.inject(applicationController);
            return subTaskCell;
        });

        taskAssigneesListView.setCellFactory(param -> {
            ContributorCell contributorCell = new ContributorCell(Mode.TASK);
            contributorCell.inject(applicationController);
            return contributorCell;
        });

        taskTagsListView.setCellFactory(param -> {
            TagCell tagCell = new TagCell(Mode.TASK);
            tagCell.inject(applicationController);
            return tagCell;
        });

        boardContributorsComboBox.setCellFactory(param -> {
            ContributorCell contributorCell = new ContributorCell(Mode.VIEW_ONLY);
            contributorCell.inject(applicationController);
            return contributorCell;
        });

        boardTagsComboBox.setCellFactory(param -> {
            TagCell tagCell = new TagCell(Mode.VIEW_ONLY);
            tagCell.inject(applicationController);
            return tagCell;
        });

        taskDueDatePicker.setConverter(new LocalDateStringConverter());


        // Listeners

        taskNameTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                TaskRepository.getInstance().updateName(currentTask.getId(), taskNameTextField.getText());
                currentTask.setName(taskNameTextField.getText());
                boardController.boardListView.refresh();
            }
        });

        taskDescriptionTextArea.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                TaskRepository.getInstance().updateDescription(currentTask.getId(), taskDescriptionTextArea.getText());
                currentTask.setDescription(taskDescriptionTextArea.getText());
                boardController.boardListView.refresh();
            }
        });

        taskDueDatePicker.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                Date date = Date.from(taskDueDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
                TaskRepository.getInstance().updateDueDate(currentTask.getId(), date);
                currentTask.setDueDate(date);
                boardController.boardListView.refresh();
            }
        });

    }

    @FXML
    public void handleAddSubTaskButtonAction(MouseEvent mouseEvent) {

        Task subTask = new TaskBuilder().with($ -> {
            $.id = FirestoreAPI.getInstance()
                    .encryptWithDateTime(newSubTaskTextField.getText() + applicationController.user.getName());
            $.name = newSubTaskTextField.getText();
            $.isCompleted = false;
        }).build();

        TaskRepository.getInstance().addSubTask(currentTask.getId(), subTask);

        subTasksListView.getItems().add(subTask);
        currentTask.getChildren().add(subTask);
        boardController.boardListView.refresh();
    }

    @FXML
    public void handleContributorsComboBoxAction(ActionEvent actionEvent) {
        if (boardContributorsComboBox.getSelectionModel().getSelectedIndex() != -1) {
            Contributor selected = boardContributorsComboBox.getSelectionModel().getSelectedItem();
            Platform.runLater(() -> {
                TaskRepository.getInstance().addAssigneeId(currentTask.getId(), selected.getId());
                boardContributorsComboBox.getSelectionModel().clearSelection();
                boardContributorsComboBox.getItems().remove(selected);
                currentTask.getAssignees().add(selected);
                boardController.boardListView.refresh();
            });
        }
    }

    @FXML
    public void handleTagsComboBoxAction(ActionEvent actionEvent) {
        if (boardTagsComboBox.getSelectionModel().getSelectedIndex() != -1) {
            Tag selected = boardTagsComboBox.getSelectionModel().getSelectedItem();
            Platform.runLater(() -> {
                TaskRepository.getInstance().addTagId(currentTask.getId(), selected.getId());
                boardTagsComboBox.getSelectionModel().clearSelection();
                boardTagsComboBox.getItems().remove(selected);
                currentTask.getTags().add(selected);
                boardController.boardListView.refresh();
            });
        }
    }

    @FXML
    public void handleIsCompletedTaskAction(MouseEvent mouseEvent) {
        if (currentTask.getIsCompleted()) {
            isCompletedTaskIcon.setIconColor(Palette.PRIMARY);
            TaskRepository.getInstance().updateIsCompleted(currentTask.getId(), false);
            currentTask.setIsCompleted(false);
            boardController.boardListView.refresh();
        } else {
            isCompletedTaskIcon.setIconColor(Palette.SUCCESS);
            TaskRepository.getInstance().updateIsCompleted(currentTask.getId(), true);
            currentTask.setIsCompleted(true);
            boardController.boardListView.refresh();
        }
    }

    @FXML
    public void handleDeleteTaskAction(MouseEvent mouseEvent) {
        TaskRepository.getInstance().delete(currentTask);
        ColumnRepository.getInstance().deleteTaskId(parentColumn.getId(), currentTask.getId());
        int columnIndex = currentBoard.getChildren().indexOf(parentColumn);
        currentBoard.getChildren().get(columnIndex).getChildren().remove(currentTask);
        applicationController.drawersStack.toggle(rightDrawerController.rightDrawer);
    }

    public void loadTask(Task task, Column column) {

        currentBoard = boardController.currentBoard;
        parentColumn = column;
        currentTask  = task;

        taskNameTextField.setText(task.getName());
        taskDescriptionTextArea.setText(task.getDescription());
        loadCreationInfo();
        loadSubTasksListView();
        loadAssignees();
        loadTags();
        loadDueDateDatePicker();
        loadIsCompletedIcon();

        rightDrawerController.show(rightDrawerController.taskSettings);
        applicationController.drawersStack.toggle(rightDrawerController.rightDrawer);

    }

    private void loadCreationInfo() {
        taskCreatorImageCircle.setFill(new ImagePattern(currentTask.getCreator().getImage()));
        taskCreatorLabel.setText(currentTask.getCreator().getName());

        SimpleDateFormat sdf = new SimpleDateFormat("MMMM, dd, hh:mm a");
        String date = sdf.format(currentTask.getCreationDate().getTime());
        taskCreationDateLabel.setText(date);
    }

    private void loadSubTasksListView() {
        subTasksListView.getItems().clear();
        for (TaskBase subTask : currentTask.getChildren()) {
            subTasksListView.getItems().add((Task) subTask);
        }
    }

    private void loadAssignees() {
        taskAssigneesListView.setItems(currentTask.getAssignees());
        boardContributorsComboBox.setItems(getUnusedItems(currentBoard.getContributors(), currentTask.getAssignees()));
    }

    private void loadTags() {
        taskTagsListView.setItems(currentTask.getTags());
        boardTagsComboBox.setItems(getUnusedItems(currentBoard.getTags(), currentTask.getTags()));
    }

    private void loadDueDateDatePicker() {
        if (currentTask.getDueDate() != null) {
            taskDueDatePicker.setValue(currentTask.getDueDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        } else {
            taskDueDatePicker.setPromptText("Choose Due Date");
            taskDueDatePicker.setValue(null);
        }
    }

    private void loadIsCompletedIcon() {
        if (currentTask.getIsCompleted()) {
            isCompletedTaskIcon.setIconColor(Palette.SUCCESS);
        } else {
            isCompletedTaskIcon.setIconColor(Palette.PRIMARY);
        }
    }

    /**
     * Get items the exist in a collection but not in the sub collection.
     *
     * @param collection the collection that contains all the items.
     * @param subCollection the subCollection that contains a portion of the items.
     * @param <T> The type of both the sub-collection & the collection.
     * @return An Observable list of the items that exist in the collection but not in the sub-collection,
     *         return the collection if the sub-collection is empty.
     */
    private <T> ObservableList<T> getUnusedItems(ObservableList<T> collection, ObservableList<T> subCollection) {
        ObservableList<T> unusedItems = FXCollections.observableArrayList();
        if (subCollection.isEmpty()) {
            return FXCollections.observableArrayList(collection);
        }
        for (T collectionItem : collection) {
            for (T subCollectionItem : subCollection) {
                if (collectionItem != subCollectionItem) {
                    unusedItems.add(collectionItem);
                }
            }
        }
        return unusedItems;
    }

    private class LocalDateStringConverter extends StringConverter<LocalDate> {

        String pattern = "dd-MM-yyyy";
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

        {
            taskDueDatePicker.setPromptText(pattern.toLowerCase());
        }

        @Override
        public String toString(LocalDate date) {
            if (date != null) {
                return dateFormatter.format(date);
            } else {
                return "";
            }
        }

        @Override
        public LocalDate fromString(String string) {
            if (string != null && !string.isEmpty()) {
                return LocalDate.parse(string, dateFormatter);
            } else {
                return null;
            }
        }

    }
}