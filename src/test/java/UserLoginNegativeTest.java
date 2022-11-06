import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserClient;
import user.UserCredentials;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;

public class UserLoginNegativeTest {

    User user;
    UserClient userClient;

    @Before
    public void setUp() {
        user = User.getRandomUser();
        userClient = new UserClient();
    }

    @Test
    @DisplayName("Login with incorrect email and password")
    @Description("Login with incorrect email and password returns status code 401, flag success: false, correct message")
    public void userWithIncorrectEmailAndPasswordCanNotLogin() {
        Response response = userClient.loginUser(UserCredentials.from(user));

        assertEquals("Status code isn't equal to 401", SC_UNAUTHORIZED, response.getStatusCode());

        assertFalse("Success value isn't false", response.getBody().path("success"));

        String expectedMessage = "email or password are incorrect";
        String actualMessage = response.getBody().path("message");
        assertEquals("Incorrect message", expectedMessage, actualMessage);
    }
}
