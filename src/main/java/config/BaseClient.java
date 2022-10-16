package config;

import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class BaseClient {

    public RequestSpecification getSpec() {
        return given()
                .header("Content-Type", "application/json")
                .baseUri(Config.BASE_URL);
    }
}
