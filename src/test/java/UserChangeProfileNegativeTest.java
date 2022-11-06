import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import user.User;
import user.UserClient;
import user.UserProfileData;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class UserChangeProfileNegativeTest {

    User user;
    UserClient userClient = new UserClient();
    private int expectedStatusCode;
    private String expectedMessage;
    private String accessToken;

    public UserChangeProfileNegativeTest(User user, int expectedStatusCode, String expectedMessage) {
        this.user = user;
        this.expectedStatusCode = expectedStatusCode;
        this.expectedMessage = expectedMessage;
    }

    @Parameterized.Parameters(name = "Пользователь = {index}, статус код = {1}, сообщение об ошибке = {2}")
    public static Object[][] getProfileData() {
        return new Object[][]{
                {User.getRandomUser(), SC_UNAUTHORIZED, "You should be authorised"},
                {User.getRandomUserWithoutEmail(), SC_UNAUTHORIZED, "You should be authorised"},
                {User.getRandomUserWithoutPassword(), SC_UNAUTHORIZED, "You should be authorised"},
                {User.getRandomUserWithoutName(), SC_UNAUTHORIZED, "You should be authorised"}
        };
    }

    @After
    public void tearDown() {
        userClient.deleteUser(accessToken);
    }

    @Test
    @DisplayName("Profile data can't be changed without authorization")
    @Description("Profile data can't be changed without authorization returns status code 401, flag success: false, correct error message")
    public void userCanNotChangeProfileWithoutAuthorization() {
        Response creationResponse = userClient.createUser(User.getRandomUser());
        assertEquals("Status code isn't equal to 200", SC_OK, creationResponse.getStatusCode());
        accessToken = creationResponse.getBody().path("accessToken");

        Response changingResponse = userClient.changeUserProfile(UserProfileData.from(user), "");

        assertEquals("Incorrect status code", expectedStatusCode, changingResponse.getStatusCode());

        assertFalse("Success value isn't false", changingResponse.getBody().path("success"));

        String actualMessage = changingResponse.getBody().path("message");
        assertEquals("Incorrect message", expectedMessage, actualMessage);
    }
}
