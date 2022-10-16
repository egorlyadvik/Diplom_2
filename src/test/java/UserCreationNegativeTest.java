import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import user.User;
import user.UserClient;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;

@RunWith(Enclosed.class)
public class UserCreationNegativeTest {

    public static class NotParameterizedUserCreationNegativeTest {

        User user;
        UserClient userClient;

        @Before
        public void setUp() {
            user = User.getRandomUser();
            userClient = new UserClient();
        }

        @Test
        @DisplayName("Create a user who has already registered")
        @Description("Create a user who has already registered returns 403 status code, flag success: false, correct message")
        public void userWithExistedDataCanNotBeCreated() {
            userClient.createUser(user).then().statusCode(SC_OK);

            Response response = userClient.createUser(user);

            assertEquals(SC_FORBIDDEN, response.getStatusCode());

            assertFalse(response.getBody().path("success"));

            String expectedMessage = "User already exists";
            String actualMessage = response.getBody().path("message");
            assertEquals(expectedMessage, actualMessage);
        }
    }

    @RunWith(Parameterized.class)
    public static class ParameterizedUserCreationNegativeTest {

        User user;
        UserClient userClient = new UserClient();
        private int expectedStatusCode;
        private String expectedMessage;

        public ParameterizedUserCreationNegativeTest(User user, int expectedStatusCode, String expectedMessage) {
            this.user = user;
            this.expectedStatusCode = expectedStatusCode;
            this.expectedMessage = expectedMessage;
        }

        @Parameterized.Parameters(name = "Пользователь = {index}, статус код = {1}, сообщение об ошибке = {2}")
        public static Object[][] getUserData() {
            return new Object[][]{
                    {User.getRandomUserWithoutEmail(), SC_FORBIDDEN, "Email, password and name are required fields"},
                    {User.getRandomUserWithoutPassword(), SC_FORBIDDEN, "Email, password and name are required fields"},
                    {User.getRandomUserWithoutName(), SC_FORBIDDEN, "Email, password and name are required fields"}
            };
        }

        @Test
        @DisplayName("User without required fields can't be created")
        @Description("User without required fields can't be created returns 403 status code, flag success: false, correct message")
        public void userWithoutRequiredDataCanNotBeCreated() {
            Response response = userClient.createUser(user);

            assertEquals(expectedStatusCode, response.getStatusCode());

            assertFalse(response.getBody().path("success"));

            String actualMessage = response.getBody().path("message");
            assertEquals(expectedMessage, actualMessage);
        }
    }
}
