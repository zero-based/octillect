package octillect.controllers;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
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
    @FXML public CheckComboBox<String> assigneesCheckComboBox;
    @FXML public CheckComboBox<String> labelsCheckComboBox;

    public ObservableList<String> selectedAssignees;
    public ObservableList<String> selectedLabels;


    // Injected Controllers
    private ApplicationController applicationController;

    @Override
    public void inject(ApplicationController applicationController) {
        this.applicationController = applicationController;
    }

    @FXML
    public void initialize() {

        ObservableList<String> assignees = FXCollections.observableArrayList();
        // To be changed by the contributors number
        for (int i = 0; i <= 10; i++) {
            assignees.add("items" + i);
        }
        assigneesCheckComboBox.getItems().addAll(assignees);

        assigneesCheckComboBox.getCheckModel()
                .getCheckedItems()
                .addListener((ListChangeListener<String>) c -> {
                    selectedAssignees = assigneesCheckComboBox.getCheckModel()
                            .getCheckedItems();
                });

        ObservableList<String> labels = FXCollections.observableArrayList();
        // To be changed by the contributors number
        for (int i = 0; i <= 10; i++) {
            labels.add("items" + i);
        }
        labelsCheckComboBox.getItems().addAll(labels);

        labelsCheckComboBox.getCheckModel()
                .getCheckedItems()
                .addListener((ListChangeListener<String>) c -> {
                    selectedLabels = labelsCheckComboBox.getCheckModel()
                            .getCheckedItems();
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
    }

    @FXML
    public void handleDeleteTaskAction(MouseEvent mouseEvent) {
    }

}