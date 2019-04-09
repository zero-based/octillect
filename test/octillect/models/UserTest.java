package octillect.models;

import org.junit.Test;
import static org.junit.Assert.*;

public class UserTest {

    String id = "OCT101";
    String name = "Test User";
    String email = "TestUser@octillect.com";
    String password = "strong_password";

    User user = new User (id, name, email, password, null);

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