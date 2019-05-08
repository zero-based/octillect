package octillect.controllers.settings;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.util.StringConverter;

import octillect.controllers.ApplicationController;
import octillect.controllers.BoardController;
import octillect.controllers.Injectable;
import octillect.controllers.RightDrawerController;
import octillect.controls.OButton;
import octillect.controls.SubTaskCell;
import octillect.database.repositories.ColumnRepository;
import octillect.database.repositories.TaskRepository;
import octillect.models.*;
import octillect.styles.Palette;

import org.controlsfx.control.CheckComboBox;

import org.kordamp.ikonli.javafx.FontIcon;

public class TaskSettingsController implements Injectable<ApplicationController> {

    // FXML Fields
    @FXML public TitledPane editNameTitledPane;
    @FXML public TitledPane editDescriptionTitledPane;
    @FXML public TitledPane subTasksTitledPane;
    @FXML public TitledPane assigneesTitledPane;
    @FXML public TitledPane tagsTitledPane;
    @FXML public TitledPane dueDateTitledPane;
    @FXML public TitledPane isCompletedTaskTitledPane;
    @FXML public TitledPane deleteTaskTitledPane;
    @FXML public Circle taskCreatorImageCircle;
    @FXML public FontIcon isCompletedTaskIcon;
    @FXML public Label taskCreatorLabel;
    @FXML public Label taskCreationDateLabel;
    @FXML public JFXTextField taskNameTextField;
    @FXML public JFXTextField newSubTaskTextField;
    @FXML public JFXTextArea taskDescriptionTextArea;
    @FXML public JFXDatePicker taskDueDatePicker;
    @FXML public JFXListView<Task> subTasksListView;
    @FXML public CheckComboBox<Contributor> assigneesCheckComboBox;
    @FXML public CheckComboBox<Tag> tagsCheckComboBox;
    @FXML public OButton addSubTaskButton;

    private Task currentTask;
    private Column parentColumn;

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

        subTasksListView.setCellFactory(param -> new SubTaskCell());

        assigneesCheckComboBox.setConverter(new AssigneesStringConverter());
        tagsCheckComboBox.setConverter(new TagStringConverter());
        taskDueDatePicker.setConverter(new LocalDateStringConverter());

        taskNameTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                TaskRepository.getInstance().updateName(currentTask.getId(), taskNameTextField.getText());
                currentTask.setName(taskNameTextField.getText());
                refreshTask();
            }
        });

        taskDescriptionTextArea.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                TaskRepository.getInstance().updateDescription(currentTask.getId(), taskDescriptionTextArea.getText());
                currentTask.setDescription(taskDescriptionTextArea.getText());
                refreshTask();
            }
        });

        assigneesCheckComboBox.getCheckModel()
                .getCheckedItems()
                .addListener((ListChangeListener<Contributor>) c -> {
                    ObservableList<Contributor> selectedAssignees = assigneesCheckComboBox.getCheckModel().getCheckedItems();

                    ArrayList<String> selectedAssigneesIds = new ArrayList<>();
                    selectedAssignees.forEach(assignee -> selectedAssigneesIds.add(assignee.getId()));
                    TaskRepository.getInstance().updateAssigneeIds(currentTask.getId(), selectedAssigneesIds);

                    currentTask.setAssignees(selectedAssignees);
                    refreshTask();
                });

        tagsCheckComboBox.getCheckModel()
                .getCheckedItems()
                .addListener((ListChangeListener<Tag>) c -> {
                    ObservableList<Tag> selectedTags = tagsCheckComboBox.getCheckModel().getCheckedItems();

                    ArrayList<String> selectedTagsIds = new ArrayList<>();
                    selectedTags.forEach(tag -> selectedTagsIds.add(tag.getId()));
                    TaskRepository.getInstance().updateTagsIds(currentTask.getId(), selectedTagsIds);

                    currentTask.setTags(selectedTags);
                    refreshTask();
                });

        taskDueDatePicker.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                Date date = Date.from(taskDueDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
                TaskRepository.getInstance().updateDueDate(currentTask.getId(), date);
                currentTask.setDueDate(date);
                refreshTask();
            }
        });

    }

    @FXML
    public void handleTitledPaneOnAction(MouseEvent mouseEvent) {
        editNameTitledPane.setExpanded(false);
        editDescriptionTitledPane.setExpanded(false);
        subTasksTitledPane.setExpanded(false);
        assigneesTitledPane.setExpanded(false);
        tagsTitledPane.setExpanded(false);
        dueDateTitledPane.setExpanded(false);
        ((TitledPane) mouseEvent.getSource()).setExpanded(true);
    }

    @FXML
    public void handleAddSubTaskButtonAction(MouseEvent mouseEvent) {
    }

    @FXML
    public void handleIsCompletedTaskAction(MouseEvent mouseEvent) {
        if (currentTask.getIsCompleted()) {
            isCompletedTaskIcon.setIconColor(Palette.PRIMARY);
            TaskRepository.getInstance().updateisCompleted(currentTask.getId(), false);
            currentTask.setIsCompleted(false);
            refreshTask();
        } else {
            isCompletedTaskIcon.setIconColor(Palette.SUCCESS);
            TaskRepository.getInstance().updateisCompleted(currentTask.getId(), true);
            currentTask.setIsCompleted(true);
            refreshTask();
        }
    }

    @FXML
    public void handleDeleteTaskAction(MouseEvent mouseEvent) {
        TaskRepository.getInstance().delete(currentTask);
        ColumnRepository.getInstance().deleteTaskId(parentColumn.getId(), currentTask.getId());
        int columnIndex = boardController.currentBoard.getChildren().indexOf(parentColumn);
        boardController.currentBoard.getChildren().get(columnIndex).getChildren().remove(currentTask);
        applicationController.drawersStack.toggle(rightDrawerController.rightDrawer);
    }

    public void loadTask(Task task, Column column) {
        parentColumn = column;
        currentTask = task;

        loadCreationInfo();
        taskNameTextField.setText(task.getName());
        taskDescriptionTextArea.setText(task.getDescription());
        loadBoardContributorsCheckComboBox();
        selectTaskAssignees();
        loadBoardTagsCheckComboBox();
        selectTaskTags();
        loadDueDateDatePicker();
        loadIsCompletedIcon();
        loadSubTasksListView();

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

    public void loadBoardContributorsCheckComboBox() {
        assigneesCheckComboBox.getItems().clear();
        for (Contributor contributor : boardController.currentBoard.getContributors()) {
            assigneesCheckComboBox.getItems().add(contributor);
        }
    }

    public void loadSubTasksListView() {
        if (currentTask.getChildren() != null) {
            for (TaskBase subTask : currentTask.getChildren()) {
                subTasksListView.getItems().add((Task) subTask);
            }
        }
    }

    private void selectTaskAssignees() {
        if (currentTask.getAssignees() != null) {
            for (Contributor assignee : currentTask.getAssignees()) {
                assigneesCheckComboBox.getCheckModel().check(getAssigneeIndex(assignee));
            }
        }
    }

    public void loadBoardTagsCheckComboBox() {
        tagsCheckComboBox.getItems().clear();
        if (boardController.currentBoard.getTags() != null) {
            for (Tag tag : boardController.currentBoard.getTags()) {
                tagsCheckComboBox.getItems().add(tag);
            }
        }
    }

    private void selectTaskTags() {
        if (currentTask.getTags() != null) {
            for (Tag tag : currentTask.getTags()) {
                tagsCheckComboBox.getCheckModel().check(gettagIndex(tag));
            }
        }
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

    private void refreshTask() {
        int columnIndex = boardController.currentBoard.getChildren().indexOf(parentColumn);
        int taskIndex = boardController.currentBoard.getChildren().get(columnIndex).getChildren().indexOf(currentTask);
        boardController.currentBoard.getChildren().get(columnIndex).getChildren().set(taskIndex, currentTask);
    }

    private int getAssigneeIndex(Contributor assignee) {
        for (int i = 0; i < assigneesCheckComboBox.getItems().size(); i++) {
            if (assigneesCheckComboBox.getItems().get(i).getId().equals(assignee.getId())) {
                return i;
            }
        }
        return -1;
    }

    private int gettagIndex(Tag tag) {
        for (int i = 0; i < tagsCheckComboBox.getItems().size(); i++) {
            if (tagsCheckComboBox.getItems().get(i).getId().equals(tag.getId())) {
                return i;
            }
        }
        return -1;
    }

    private class AssigneesStringConverter extends StringConverter<Contributor> {

        Contributor contributor = null;

        @Override
        public String toString(Contributor contributor) {
            this.contributor = contributor;
            return contributor.getEmail();
        }

        @Override
        public Contributor fromString(String string) {
            return contributor;
        }

    }

    private class TagStringConverter extends StringConverter<Tag> {

        Tag tag = null;

        @Override
        public String toString(Tag user) {
            this.tag = user;
            return tag.getName();
        }

        @Override
        public Tag fromString(String string) {
            return tag;
        }

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