import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import order.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import user.User;
import user.UserClient;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class UserOrderPositiveTest {

    OrderClient orderClient;
    IngredientsClient ingredientsClient;
    Ingredients ingredients;
    UserOrders userOrders;
    UserClient userClient;
    private int expectedNumberOfOrders;
    private int expectedStatusCode;
    private String accessToken;

    public UserOrderPositiveTest(int numberOfOrders, int expectedStatusCode) {
        this.expectedNumberOfOrders = numberOfOrders;
        this.expectedStatusCode = expectedStatusCode;
    }

    @Parameterized.Parameters(name = "Пользователь = {index}, количество заказов = {0}, статус код = {1}")
    public static Object[][] getOrderData() {
        return new Object[][]{
                {1, SC_OK},
                {50, SC_OK},
                {51, SC_OK}
        };
    }

    @Before
    public void setUp() {
        orderClient = new OrderClient();
        ingredientsClient = new IngredientsClient();
        User user = User.getRandomUser();
        userClient = new UserClient();
        Response userCreationResponse = userClient.createUser(user);
        assertEquals(SC_OK, userCreationResponse.getStatusCode());
        accessToken = userCreationResponse.getBody().path("accessToken");

        List<String> ingredientsList = ingredientsClient.getIngredientsList();
        ingredients = new Ingredients(ingredientsList);

        userOrders = new UserOrders(user, new ArrayList<>());
    }

    @After
    public void tearDown() {
        userClient.deleteUser(accessToken);
    }

    @Test
    @DisplayName("Authorized user gets his orders")
    @Description("Authorized user gets his orders returns 200 status code, flag success: true, correct number of orders")
    public void getOrdersOfAuthorizedUserReturnsOrders() {
        createExpectedNumberOfOrders(expectedNumberOfOrders);

        Response response = orderClient.getUserOrders(accessToken);

        assertEquals(expectedStatusCode, response.getStatusCode());

        assertTrue(response.getBody().path("success"));

        List<Order> expectedOrders = getExpectedOrders();
        List<LinkedHashMap> actualOrders = response.getBody().path("orders");
        assertEquals(expectedOrders.size(), actualOrders.size());
    }

    private void createExpectedNumberOfOrders(int expectedNumberOfOrders) {
        for (int i = 0; i < expectedNumberOfOrders; i++) {
            orderClient.createOrderWithOrderList(userOrders, ingredients, accessToken);
        }
    }

    private List<Order> getExpectedOrders() {
        if (expectedNumberOfOrders < 50) {
            return userOrders.getOrders();
        }
        return userOrders.getOrders().subList(0, 50);
    }
}
