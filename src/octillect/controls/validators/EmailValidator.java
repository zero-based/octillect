package octillect.controls.validators;

import com.jfoenix.validation.RegexValidator;

public class EmailValidator extends RegexValidator {

    public EmailValidator() {
        super("Invalid Email.");
        setRegexPattern("([a-z0-9_\\.-]+)@[\\da-z\\.-]+[a-z\\.]{2,5}");
    }

}
