package octillect.controllers.settings;

import com.jfoenix.controls.JFXColorPicker;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

import octillect.controllers.application.ApplicationController;
import octillect.controllers.application.BoardController;
import octillect.controllers.application.LeftDrawerController;
import octillect.controllers.application.TitleBarController;
import octillect.controllers.util.Injectable;
import octillect.controllers.util.PostLoad;
import octillect.controls.cells.ContributorCell;
import octillect.controls.cells.Mode;
import octillect.controls.cells.TagCell;
import octillect.controls.OButton;
import octillect.controls.validators.CustomValidator;
import octillect.controls.validators.RequiredValidator;
import octillect.controls.validators.ValidationManager;
import octillect.database.repositories.TagRepository;
import octillect.database.repositories.BoardRepository;
import octillect.database.repositories.UserRepository;
import octillect.database.firebase.FirestoreAPI;
import octillect.models.Contributor;
import octillect.models.Tag;
import octillect.models.builders.TagBuilder;

public class BoardSettingsController implements Injectable<ApplicationController> {

    // FXML Fields
    @FXML public TitledPane deleteBoardTitledPane;
    @FXML public BorderPane newContributorBorderPane;
    @FXML public BorderPane newTagBorderPane;
    @FXML public JFXTextField editNameTextField;
    @FXML public JFXTextField newContributorTextField;
    @FXML public JFXTextField newTagTextField;
    @FXML public JFXTextArea editDescriptionTextArea;
    @FXML public JFXListView<Contributor> contributorsListView;
    @FXML public JFXListView<Tag> tagsListView;
    @FXML public JFXComboBox<Contributor.Role> rolesComboBox;
    @FXML public JFXColorPicker tagColorPicker;
    @FXML public OButton addContributorButton;
    @FXML public OButton addTagButton;

    // Validators
    private RequiredValidator requiredValidator;
    private CustomValidator emailValidator;
    private CustomValidator contributorValidator;

    // Injected Controllers
    private ApplicationController applicationController;
    private BoardController boardController;
    private LeftDrawerController leftDrawerController;
    private TitleBarController titleBarController;

    @Override
    public void inject(ApplicationController applicationController) {
        this.applicationController = applicationController;
        boardController            = applicationController.boardController;
        leftDrawerController       = applicationController.leftDrawerController;
        titleBarController         = applicationController.titleBarController;
    }

    @PostLoad
    public void initCombBoxItems() {
        rolesComboBox.setItems(FXCollections.observableArrayList(Contributor.Role.values()));
    }

    @PostLoad
    public void initCellFactories() {

        contributorsListView.setCellFactory(param -> {
            ContributorCell contributorCell = new ContributorCell(Mode.BOARD);
            contributorCell.inject(applicationController);
            return contributorCell;
        });

        tagsListView.setCellFactory(param -> {
            TagCell tagCell = new TagCell(Mode.BOARD);
            tagCell.inject(applicationController);
            return tagCell;
        });

    }

    @PostLoad
    public void initValidators() {
        requiredValidator    = new RequiredValidator();
        emailValidator       = new CustomValidator("This account doesn't exist.");
        contributorValidator = new CustomValidator("Already a Contributor.");

        ValidationManager.addValidator(false, requiredValidator, newContributorTextField, rolesComboBox, newTagTextField);
        ValidationManager.addValidator(false, emailValidator, newContributorTextField);
        ValidationManager.addValidator(false, contributorValidator, newContributorTextField);
    }

    @PostLoad
    public void initListeners() {

        editNameTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue && boardController.currentBoard != null) {
                BoardRepository.getInstance().updateName(boardController.currentBoard.getId(), editNameTextField.getText());
                boardController.currentBoard.setName(editNameTextField.getText());
                titleBarController.boardNameLabel.setText(editNameTextField.getText());
                leftDrawerController.userBoardsListView.refresh();
            }
        });

        editDescriptionTextArea.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue && boardController.currentBoard != null) {
                BoardRepository.getInstance().updateDescription(boardController.currentBoard.getId(), editDescriptionTextArea.getText());
                boardController.currentBoard.setDescription(editDescriptionTextArea.getText());
            }
        });

    }

    @FXML
    public void handleAddContributorButtonAction(MouseEvent mouseEvent) {

        newContributorTextField.validate();
        rolesComboBox.validate();
        boolean isContributor = false;

        if (!requiredValidator.getHasErrors()) {

            for (Contributor contributor : boardController.currentBoard.getContributors()) {
                if (contributor.getEmail().equals(newContributorTextField.getText())) {
                    isContributor = true;
                }
            }

            if (isContributor) {
                emailValidator.showMessage();
            } else {
                Contributor contributor = BoardRepository.getInstance().getContributor(FirestoreAPI.getInstance().encrypt(newContributorTextField.getText()));

                if (contributor == null) {
                    emailValidator.showMessage();
                } else {
                    contributor.setRole(rolesComboBox.getValue());
                    BoardRepository.getInstance().addContributor(boardController.currentBoard.getId(), contributor);
                    UserRepository.getInstance().addBoardId(contributor.getId(), boardController.currentBoard.getId());
                    boardController.currentBoard.getContributors().add(contributor);

                    newContributorTextField.setText(null);
                    rolesComboBox.getSelectionModel().clearSelection();
                }
            }
        }
    }

    @FXML
    public void handleAddTagButtonAction(MouseEvent mouseEvent) {

        newTagTextField.validate();

        if (!requiredValidator.getHasErrors()) {

            Tag tag = new TagBuilder().with($ -> {
                $.id = FirestoreAPI.getInstance().encryptWithDateTime(newTagTextField.getText() + applicationController.user.getId());
                $.name = newTagTextField.getText();
                $.color = tagColorPicker.getValue();
            }).build();

            TagRepository.getInstance().add(tag);
            BoardRepository.getInstance().addTagId(boardController.currentBoard.getId(), tag.getId());
            boardController.currentBoard.getTags().add(tag);

            newTagTextField.setText(null);
            tagColorPicker.setValue(Color.WHITE);
        }
    }

    @FXML
    public void handleDeleteBoardAction(MouseEvent mouseEvent) {
        BoardRepository.getInstance().delete(boardController.currentBoard);
        for (Contributor contributor : boardController.currentBoard.getContributors()) {
            UserRepository.getInstance().deleteBoardId(contributor.getId(), boardController.currentBoard.getId());
        }

        applicationController.user.getBoards().remove(boardController.currentBoard);
        boardController.loadFirstBoard();
    }

    public void loadBoardSettings() {
        editNameTextField.setText(boardController.currentBoard.getName());
        editDescriptionTextArea.setText(boardController.currentBoard.getDescription());
        controlRoleAccess(boardController.currentBoard.getUserRole(applicationController.user.getId()));
        loadContributors();
        loadTags();
    }

    private void loadContributors() {
        contributorsListView.setItems(boardController.currentBoard.getContributors());
    }

    private void loadTags() {
        tagsListView.setItems(boardController.currentBoard.getTags());
    }

    public void resetBoardSettings() {
        editNameTextField.setText(null);
        editDescriptionTextArea.setText(null);
        contributorsListView.getItems().clear();
        newContributorTextField.setText(null);
        rolesComboBox.getSelectionModel().clearSelection();
        tagsListView.getItems().clear();
        newTagTextField.setText(null);
        tagColorPicker.setValue(Color.WHITE);
    }

    private void controlRoleAccess(Contributor.Role role) {

        if (role.equals(Contributor.Role.owner)) {
            deleteBoardTitledPane.setDisable(false);
            deleteBoardTitledPane.setOpacity(1);
        } else {
            deleteBoardTitledPane.setDisable(true);
            deleteBoardTitledPane.setOpacity(0);
        }

        if (role.equals(Contributor.Role.viewer)) {

            newContributorTextField.setDisable(true);
            rolesComboBox.setDisable(true);
            addContributorButton.setDisable(true);
            newContributorBorderPane.setManaged(false);
            newContributorBorderPane.setOpacity(0);

            newTagTextField.setDisable(true);
            tagColorPicker.setDisable(true);
            addTagButton.setDisable(true);
            newTagBorderPane.setDisable(true);
            newTagBorderPane.setManaged(false);
            newTagBorderPane.setOpacity(0);

        } else {

            newContributorTextField.setDisable(false);
            rolesComboBox.setDisable(false);
            addContributorButton.setDisable(false);
            newContributorBorderPane.setManaged(true);
            newContributorBorderPane.setOpacity(1);

            newTagTextField.setDisable(false);
            tagColorPicker.setDisable(false);
            addTagButton.setDisable(false);
            newTagBorderPane.setDisable(false);
            newTagBorderPane.setManaged(true);
            newTagBorderPane.setOpacity(1);

        }

    }

}
