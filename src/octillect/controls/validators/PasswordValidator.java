package octillect.controls.validators;

import com.jfoenix.validation.RegexValidator;

public class PasswordValidator extends RegexValidator {

    public PasswordValidator() {
        super("Use 8 or more characters with a mix of letters and numbers.");
        setRegexPattern("^(?=.*[0-9]+.*)(?=.*[a-zA-Z]+.*)[0-9a-zA-Z]{8,}$");
    }

}
