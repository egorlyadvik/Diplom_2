import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import user.User;
import user.UserClient;
import user.UserProfileData;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class UserChangeProfilePositiveTest {

    User user;
    static User staticUser = User.getRandomUser();
    UserClient userClient = new UserClient();
    static UserClient staticUserClient = new UserClient();
    private int expectedStatusCode;
    private static String accessToken;

    public UserChangeProfilePositiveTest(User user, int expectedStatusCode) {
        this.user = user;
        this.expectedStatusCode = expectedStatusCode;
    }

    @Parameterized.Parameters(name = "Пользователь = {index}, статус код = {1}")
    public static Object[][] getProfileData() {
        Response creationResponse = staticUserClient.createUser(staticUser);
        assertEquals("Status code isn't equal to 200", SC_OK, creationResponse.getStatusCode());
        accessToken = creationResponse.getBody().path("accessToken");

        return new Object[][]{
                {User.getRandomUser(), SC_OK},
                {User.getRandomEmailForUser(staticUser), SC_OK},
                {User.getRandomPasswordForUser(staticUser), SC_OK},
                {User.getRandomNameForUser(staticUser), SC_OK}
        };
    }

    @AfterClass
    public static void tearDown() {
        staticUserClient.deleteUser(accessToken);
    }

    @Test
    @DisplayName("Change profile data")
    @Description("Change profile data returns status code 200, flag success: true, updated user email and name")
    public void userCanChangeProfile() {
        Response changingResponse = userClient.changeUserProfile(UserProfileData.from(user), accessToken);

        assertEquals("Incorrect status code", expectedStatusCode, changingResponse.getStatusCode());

        assertTrue("Success value isn't true", changingResponse.getBody().path("success"));

        String expectedUserEmail = user.getEmail();
        String actualUserEmail = changingResponse.getBody().path("user.email");
        assertEquals("Incorrect user email", expectedUserEmail, actualUserEmail);

        String expectedUserName = user.getName();
        String actualUserName = changingResponse.getBody().path("user.name");
        assertEquals("Incorrect user name", expectedUserName, actualUserName);
    }
}
