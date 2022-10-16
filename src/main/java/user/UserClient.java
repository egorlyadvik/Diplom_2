package user;

import config.BaseClient;
import io.qameta.allure.Step;
import io.restassured.response.Response;

public class UserClient extends BaseClient {

    private final String CREATE_USER = "/auth/register";
    private final String LOGIN_USER = "/auth/login";
    private final String CHANGE_USER_PROFILE_OR_DELETE_USER = "/auth/user";

    @Step("Create new user")
    public Response createUser(User user) {
        return getSpec()
                .body(user)
                .when()
                .post(CREATE_USER);
    }

    @Step("Login user")
    public Response loginUser(UserCredentials userCredentials) {
        return getSpec()
                .body(userCredentials)
                .when()
                .post(LOGIN_USER);
    }

    @Step("Change user profile data")
    public Response changeUserProfile(UserProfileData userProfileData, String accessToken) {
        return getSpec()
                .header("Authorization", accessToken)
                .body(userProfileData)
                .when()
                .patch(CHANGE_USER_PROFILE_OR_DELETE_USER);
    }

    @Step("Delete user")
    public void deleteUser(String accessToken) {
        getSpec()
                .header("Authorization", accessToken)
                .when()
                .delete(CHANGE_USER_PROFILE_OR_DELETE_USER)
                .then()
                .statusCode(202);
    }
}
