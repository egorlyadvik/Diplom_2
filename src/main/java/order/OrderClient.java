package order;

import config.BaseClient;
import io.qameta.allure.Step;
import io.restassured.response.Response;

public class OrderClient extends BaseClient {

    private final String CREATE_ORDER_OR_GET_ORDERS = "/orders";

    @Step("Create order")
    public Response createOrder(Ingredients ingredients, String accessToken) {
        return getSpec()
                .header("Authorization", accessToken)
                .body(ingredients)
                .when()
                .post(CREATE_ORDER_OR_GET_ORDERS);
    }

    @Step("Get user's orders")
    public Response getUserOrders(String accessToken) {
        return getSpec()
                .header("Authorization", accessToken)
                .when()
                .get(CREATE_ORDER_OR_GET_ORDERS);
    }

    @Step("Create order and add it into user order list")
    public void createOrderWithOrderList(UserOrders userOrders, Ingredients ingredients, String accessToken) {
        Response response = createOrder(ingredients, accessToken);
        int orderNumber = response.getBody().path("order.number");
        Order order = new Order(orderNumber, ingredients.getIngredients());
        userOrders.addOrder(order);
    }
}
