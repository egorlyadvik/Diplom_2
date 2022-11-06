import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import order.Ingredients;
import order.OrderClient;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;

public class OrderCreationNegativeTest {
    OrderClient orderClient;
    private String accessToken;

    @Before
    public void setUp() {
        orderClient = new OrderClient();
    }

    @Test
    @DisplayName("User can't create order without ingredients")
    @Description("User can't create order without ingredients returns 400 status code, flag success: false, correct message")
    public void userCanNotCreateOrderWithoutIngredients() {
        Ingredients ingredients = new Ingredients(Collections.emptyList());
        accessToken = "";

        Response response = orderClient.createOrder(ingredients, accessToken);

        assertEquals("Status code isn't equal to 400", SC_BAD_REQUEST, response.getStatusCode());

        assertFalse("Success value isn't false", response.getBody().path("success"));

        String expectedMessage = "Ingredient ids must be provided";
        String actualMessage = response.getBody().path("message");
        assertEquals("Incorrect message", expectedMessage, actualMessage);
    }

    @Test
    @DisplayName("User can't create order with incorrect ingredients' hash")
    @Description("User can't create order with incorrect ingredients' hash returns 500 status code")
    public void userCanNotCreateOrderWithIncorrectIngredientsHash() {
        List<String> incorrectIngredientsList = Ingredients.getIncorrectIngredients();
        Ingredients ingredients = new Ingredients(incorrectIngredientsList);
        accessToken = "";

        Response response = orderClient.createOrder(ingredients, accessToken);

        assertEquals("Status code isn't equal to 500", SC_INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
