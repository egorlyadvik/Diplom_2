import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import order.OrderClient;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;

public class UserOrderNegativeTest {

    OrderClient orderClient;
    private String accessToken;

    @Before
    public void setUp() {
        orderClient = new OrderClient();
    }

    @Test
    @DisplayName("Unauthorized user can't get his orders")
    @Description("Unauthorized user can't get his orders returns 401 status code, flag success: false, correct message")
    public void getOrdersOfAuthorizedUserReturnsOrders() {
        accessToken = "";

        Response response = orderClient.getUserOrders(accessToken);

        assertEquals("Status code isn't equal to 401", SC_UNAUTHORIZED, response.getStatusCode());

        assertFalse("Success value isn't false", response.getBody().path("success"));

        String expectedMessage = "You should be authorised";
        String actualMessage = response.getBody().path("message");
        assertEquals("Incorrect message", expectedMessage, actualMessage);
    }
}
