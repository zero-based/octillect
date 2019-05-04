package octillect.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import org.controlsfx.control.CheckComboBox;

public class TaskSettingsController implements Injectable<ApplicationController> {

    // FXML Fields
    @FXML public CheckComboBox<String> addFeaturesCheckComboBox;
    @FXML public CheckComboBox<String> addLabelsCheckComboBox;

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
        addFeaturesCheckComboBox.getItems().addAll(assignees);

        addFeaturesCheckComboBox.getCheckModel()
                .getCheckedItems()
                .addListener((ListChangeListener<String>) c -> {
                    selectedAssignees = addFeaturesCheckComboBox.getCheckModel()
                            .getCheckedItems();
                });

        ObservableList<String> labels = FXCollections.observableArrayList();
        // To be changed by the contributors number
        for (int i = 0; i <= 10; i++) {
            labels.add("items" + i);
        }
        addLabelsCheckComboBox.getItems().addAll(labels);

        addLabelsCheckComboBox.getCheckModel()
                .getCheckedItems()
                .addListener((ListChangeListener<String>) c -> {
                    selectedLabels = addLabelsCheckComboBox.getCheckModel()
                            .getCheckedItems();
                });
    }
}