import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserClient;
import user.UserCredentials;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;

public class UserLoginPositiveTest {

    User user;
    UserClient userClient;
    private String accessToken;

    @Before
    public void setUp() {
        user = User.getRandomUser();
        userClient = new UserClient();
        userClient.createUser(user).then().statusCode(SC_OK);
    }

    @After
    public void tearDown() {
        userClient.deleteUser(accessToken);
    }

    @Test
    @DisplayName("Existed user login")
    @Description("Existed user login returns status code 200, flag success: true, access and refresh tokens, correct user email and name")
    public void userCanLogin() {
        Response response = userClient.loginUser(UserCredentials.from(user));

        assertEquals(SC_OK, response.getStatusCode());

        assertTrue(response.getBody().path("success"));

        accessToken = response.getBody().path("accessToken");
        assertThat(accessToken, allOf(is(notNullValue()), startsWith("Bearer ")));

        String refreshToken = response.getBody().path("refreshToken");
        assertNotNull(refreshToken);

        String expectedUserEmail = response.getBody().path("user.email");
        String actualUserEmail = user.getEmail();
        assertEquals(expectedUserEmail, actualUserEmail);

        String expectedUserName = response.getBody().path("user.name");
        String actualUserName = user.getName();
        assertEquals(expectedUserName, actualUserName);
    }
}
