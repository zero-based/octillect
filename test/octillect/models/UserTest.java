package octillect.models;

import octillect.models.builders.UserBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

public class UserTest {

    String id       = "OCT101";
    String name     = "Test User";
    String email    = "TestUser@octillect.com";
    String password = "strong_password";

    User user = new UserBuilder().with($ -> {
        $.id       = "OCT101";
        $.name     = "Test User";
        $.email    = "TestUser@octillect.com";
        $.password = "strong_password";
    }).build();

    @Test
    public void getId() {
        assertEquals(id, user.getId());
    }

    @Test
    public void getName() {
        assertEquals(name, user.getName());
    }

    @Test
    public void getEmail() {
        assertEquals(email, user.getEmail());
    }

    @Test
    public void getPassword() {
        assertEquals(password, user.getPassword());
    }

}