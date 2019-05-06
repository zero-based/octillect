package octillect.controllers;

import com.jfoenix.controls.JFXDatePicker;
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
import javafx.util.Pair;
import javafx.util.StringConverter;

import octillect.database.accessors.ColumnRepository;
import octillect.database.accessors.TaskRepository;
import octillect.models.Column;
import octillect.models.Project;
import octillect.models.Task;
import octillect.models.User;
import octillect.styles.Palette;

import org.controlsfx.control.CheckComboBox;

import org.kordamp.ikonli.javafx.FontIcon;

public class TaskSettingsController implements Injectable<ApplicationController> {

    // FXML Fields
    @FXML public TitledPane editNameTitledPane;
    @FXML public TitledPane editDescriptionTitledPane;
    @FXML public TitledPane assigneesTitledPane;
    @FXML public TitledPane labelsTitledPane;
    @FXML public TitledPane dueDateTitledPane;
    @FXML public TitledPane isCompletedTaskTitledPane;
    @FXML public TitledPane deleteTaskTitledPane;
    @FXML public Circle taskCreatorImageCircle;
    @FXML public FontIcon isCompletedTaskIcon;
    @FXML public Label taskCreatorLabel;
    @FXML public Label taskCreationDateLabel;
    @FXML public JFXTextField taskNameTextField;
    @FXML public JFXTextArea taskDescriptionTextArea;
    @FXML public JFXDatePicker taskDueDatePicker;
    @FXML public CheckComboBox<User> assigneesCheckComboBox;
    @FXML public CheckComboBox<octillect.models.Label> labelsCheckComboBox;

    private Task currentTask;
    private Column parentColumn;

    // Injected Controllers
    private ApplicationController applicationController;
    private ProjectController projectController;
    private RightDrawerController rightDrawerController;

    @Override
    public void inject(ApplicationController applicationController) {
        this.applicationController = applicationController;
        projectController          = applicationController.projectController;
        rightDrawerController      = applicationController.rightDrawerController;
    }

    @Override
    public void init() {

        assigneesCheckComboBox.setConverter(new AssigneesStringConverter());
        labelsCheckComboBox.setConverter(new LabelStringConverter());
        taskDueDatePicker.setConverter(new LocalDateStringConverter());

        taskNameTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                TaskRepository.updateName(currentTask.getId(), taskNameTextField.getText());
                currentTask.setName(taskNameTextField.getText());
                refreshTask();
            }
        });

        taskDescriptionTextArea.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                TaskRepository.updateDescription(currentTask.getId(), taskDescriptionTextArea.getText());
                currentTask.setDescription(taskDescriptionTextArea.getText());
                refreshTask();
            }
        });

        assigneesCheckComboBox.getCheckModel()
                .getCheckedItems()
                .addListener((ListChangeListener<User>) c -> {
                    ObservableList<User> selectedAssignees = assigneesCheckComboBox.getCheckModel().getCheckedItems();

                    ArrayList<String> selectedAssigneesIds = new ArrayList<>();
                    selectedAssignees.forEach(assignee -> selectedAssigneesIds.add(assignee.getId()));
                    TaskRepository.updateAssigneeIds(currentTask.getId(), selectedAssigneesIds);

                    currentTask.setAssignees(selectedAssignees);
                    refreshTask();
                });

        labelsCheckComboBox.getCheckModel()
                .getCheckedItems()
                .addListener((ListChangeListener<octillect.models.Label>) c -> {
                    ObservableList<octillect.models.Label> selectedLabels = labelsCheckComboBox.getCheckModel().getCheckedItems();

                    ArrayList<String> selectedLabelsIds = new ArrayList<>();
                    selectedLabels.forEach(label -> selectedLabelsIds.add(label.getId()));
                    TaskRepository.updateLabelsIds(currentTask.getId(), selectedLabelsIds);

                    currentTask.setLabels(selectedLabels);
                    refreshTask();
                });

        taskDueDatePicker.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                Date date = Date.from(taskDueDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
                TaskRepository.updateDueDate(currentTask.getId(), date);
                currentTask.setDueDate(date);
                refreshTask();
            }
        });

    }

    @FXML
    public void handleTitledPaneOnAction(MouseEvent mouseEvent) {
        editNameTitledPane.setExpanded(false);
        editDescriptionTitledPane.setExpanded(false);
        assigneesTitledPane.setExpanded(false);
        labelsTitledPane.setExpanded(false);
        dueDateTitledPane.setExpanded(false);
        ((TitledPane) mouseEvent.getSource()).setExpanded(true);
    }

    @FXML
    public void handleIsCompletedTaskAction(MouseEvent mouseEvent) {
        if (currentTask.getIsCompleted()) {
            isCompletedTaskIcon.setIconColor(Palette.PRIMARY);
            TaskRepository.updateisCompleted(currentTask.getId(), false);
            currentTask.setIsCompleted(false);
            refreshTask();
        } else {
            isCompletedTaskIcon.setIconColor(Palette.SUCCESS);
            TaskRepository.updateisCompleted(currentTask.getId(), true);
            currentTask.setIsCompleted(true);
            refreshTask();
        }
    }

    @FXML
    public void handleDeleteTaskAction(MouseEvent mouseEvent) {
        TaskRepository.delete(currentTask.getId());
        ColumnRepository.deleteTaskId(parentColumn.getId(), currentTask.getId());
        int columnIndex = projectController.currentProject.getColumns().indexOf(parentColumn);
        projectController.currentProject.getColumns().get(columnIndex).getTasks().remove(currentTask);
        applicationController.drawersStack.toggle(rightDrawerController.rightDrawer);
    }

    public void loadTask(Task task, Column column) {
        parentColumn = column;
        currentTask = task;

        loadCreationInfo();
        taskNameTextField.setText(task.getName());
        taskDescriptionTextArea.setText(task.getDescription());
        loadProjectContributorsCheckComboBox();
        selectTaskAssignees();
        loadProjectLabelsCheckComboBox();
        selectTaskLabels();
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

    public void loadProjectContributorsCheckComboBox() {
        assigneesCheckComboBox.getItems().clear();
        for (Pair<User, Project.Role> contributor : projectController.currentProject.getContributors()) {
            assigneesCheckComboBox.getItems().add(contributor.getKey());
        }
    }

    private void selectTaskAssignees() {
        if (currentTask.getAssignees() != null) {
            for (User user : currentTask.getAssignees()) {
                assigneesCheckComboBox.getCheckModel().check(getAssigneeIndex(user));
            }
        }
    }

    public void loadProjectLabelsCheckComboBox() {
        labelsCheckComboBox.getItems().clear();
        if (projectController.currentProject.getLabels() != null) {
            for (octillect.models.Label label : projectController.currentProject.getLabels()) {
                labelsCheckComboBox.getItems().add(label);
            }
        }
    }

    private void selectTaskLabels() {
        if (currentTask.getLabels() != null) {
            for (octillect.models.Label label : currentTask.getLabels()) {
                labelsCheckComboBox.getCheckModel().check(getlabelIndex(label));
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
        int columnIndex = projectController.currentProject.getColumns().indexOf(parentColumn);
        int taskIndex = projectController.currentProject.getColumns().get(columnIndex).getTasks().indexOf(currentTask);
        projectController.currentProject.getColumns().get(columnIndex).getTasks().set(taskIndex, currentTask);
    }

    private int getAssigneeIndex(User user) {
        for (int i = 0; i < assigneesCheckComboBox.getItems().size(); i++) {
            if (assigneesCheckComboBox.getItems().get(i).getId().equals(user.getId())) {
                return i;
            }
        }
        return -1;
    }

    private int getlabelIndex(octillect.models.Label label) {
        for (int i = 0; i < labelsCheckComboBox.getItems().size(); i++) {
            if (labelsCheckComboBox.getItems().get(i).getId().equals(label.getId())) {
                return i;
            }
        }
        return -1;
    }

    private class AssigneesStringConverter extends StringConverter<User> {

        User user = null;

        @Override
        public String toString(User user) {
            this.user = user;
            return user.getEmail();
        }

        @Override
        public User fromString(String string) {
            return user;
        }

    }

    private class LabelStringConverter extends StringConverter<octillect.models.Label> {

        octillect.models.Label label = null;

        @Override
        public String toString(octillect.models.Label user) {
            this.label = user;
            return label.getName();
        }

        @Override
        public octillect.models.Label fromString(String string) {
            return label;
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