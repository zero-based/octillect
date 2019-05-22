package octillect.controls.validators;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.validation.RegexValidator;

import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;

public class MatchPasswordValidator extends RegexValidator {

    private JFXPasswordField passwordField;

    public MatchPasswordValidator(JFXPasswordField passwordField) {
        super("Passwords don't match. Try again.");
        this.passwordField = passwordField;
    }

    @Override
    protected void eval() {
        TextInputControl textField = (TextInputControl) this.srcControl.get();
        this.hasErrors.set(!textField.getText().equals(passwordField.getText()));
    }

}
