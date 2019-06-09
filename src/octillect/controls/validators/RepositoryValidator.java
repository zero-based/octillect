package octillect.controls.validators;

import com.jfoenix.validation.RegexValidator;

public class RepositoryValidator extends RegexValidator {

    public RepositoryValidator() {
        super("Invalid Repository Name.");
        setRegexPattern("^[a-zA-Z\\d](?:[a-zA-Z\\d]|-(?=[a-zA-Z\\d])){0,38}"
                + "[/](?:[a-zA-Z\\d]|-(?=[a-zA-Z\\d])){1,38}$");
    }

}
