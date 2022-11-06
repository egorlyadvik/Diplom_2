package order;

import config.BaseClient;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.List;

public class IngredientsClient extends BaseClient {

    private final String GET_INGREDIENTS = "/ingredients";

    @Step("Get ingredients list")
    public List<String> getIngredientsList() {
        Response response = getSpec()
                .when()
                .get(GET_INGREDIENTS);
        return response.getBody().path("data._id");
    }
}
