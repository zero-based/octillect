package octillect.controls.validators;

import com.jfoenix.validation.RegexValidator;

public class RepositoryValidtor extends RegexValidator {

    public RepositoryValidtor() {
        super("Invalid Repository Name.");
        setRegexPattern("^[a-zA-Z\\d](?:[a-zA-Z\\d]|-(?=[a-zA-Z\\d])){0,38}"
                + "[/](?:[a-zA-Z\\d]|-(?=[a-zA-Z\\d])){1,38}$");
    }

}
