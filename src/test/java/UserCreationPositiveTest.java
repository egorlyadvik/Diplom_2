import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserClient;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;

public class UserCreationPositiveTest {

    User user;
    UserClient userClient;
    private String accessToken;

    @Before
    public void setUp() {
        user = User.getRandomUser();
        userClient = new UserClient();
    }


    @After
    public void tearDown() {
        userClient.deleteUser(accessToken);
    }

    @Test
    @DisplayName("Create an unique user")
    @Description("Create an unique user returns status code 200, flag success: true, correct user email and name, access and refresh tokens")
    public void userCanBeCreated() {
        Response response = userClient.createUser(user);

        assertEquals("Status code isn't equal to 200", SC_OK, response.getStatusCode());

        assertTrue("Success value isn't true", response.getBody().path("success"));

        String expectedUserEmail = response.getBody().path("user.email");
        String actualUserEmail = user.getEmail();
        assertEquals("Incorrect user email", expectedUserEmail, actualUserEmail);

        String expectedUserName = response.getBody().path("user.name");
        String actualUserName = user.getName();
        assertEquals("Incorrect user name", expectedUserName, actualUserName);

        accessToken = response.getBody().path("accessToken");
        assertThat("Incorrect access token", accessToken, allOf(is(notNullValue()), startsWith("Bearer ")));

        String refreshToken = response.getBody().path("refreshToken");
        assertNotNull("Refresh token is null", refreshToken);
    }
}
