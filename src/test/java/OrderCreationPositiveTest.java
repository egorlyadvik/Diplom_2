import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import order.Ingredients;
import order.IngredientsClient;
import order.OrderClient;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import user.User;
import user.UserClient;

import java.util.List;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class OrderCreationPositiveTest {

    OrderClient orderClient;
    IngredientsClient ingredientsClient;
    static UserClient userClient;
    private String accessToken;
    private int expectedStatusCode;
    private static String userCreationAccessToken;

    public OrderCreationPositiveTest(String accessToken, int expectedStatusCode) {
        this.accessToken = accessToken;
        this.expectedStatusCode = expectedStatusCode;
    }

    @Parameterized.Parameters(name = "Пользователь = {index}, токен = {0}, статус код = {1}")
    public static Object[][] getOrderData() {
        User user = User.getRandomUser();
        userClient = new UserClient();
        Response userCreationResponse = userClient.createUser(user);
        assertEquals("Status code isn't equal to 200", SC_OK, userCreationResponse.getStatusCode());
        userCreationAccessToken = userCreationResponse.getBody().path("accessToken");

        return new Object[][]{
                {userCreationAccessToken, SC_OK},
                {"", SC_OK}
        };
    }

    @Before
    public void setUp() {
        orderClient = new OrderClient();
        ingredientsClient = new IngredientsClient();
    }

    @AfterClass
    public static void tearDown() {
        userClient.deleteUser(userCreationAccessToken);
    }

    @Test
    @DisplayName("User can create order with ingredients")
    @Description("User can create order with ingredients returns 200 status code, correct order name and number, flag success: false")
    public void userCanCreateOrder() {
        List<String> ingredientsList = ingredientsClient.getIngredientsList();
        Ingredients ingredients = new Ingredients(ingredientsList);

        Response response = orderClient.createOrder(ingredients, accessToken);

        assertEquals("Incorrect status code", expectedStatusCode, response.getStatusCode());

        String orderName = response.getBody().path("name");
        assertThat("There isn't name value", orderName, is(notNullValue()));

        int orderNumber = response.getBody().path("order.number");
        assertThat("There isn't order number value", orderNumber, is(notNullValue()));

        assertTrue("Success value isn't true", response.getBody().path("success"));
    }
}
