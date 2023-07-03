package org.example;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class CourierClient extends RestClient {

    private static final String COURIER_PATH = "/api/v1/courier";

    @Step("Создание курьера")
    public ValidatableResponse createCourier(Courier courier) {
        return given()
                .spec(getBaseSpec())
                .body(courier)
                .when()
                .post(COURIER_PATH)
                .then();
    }

    @Step("Логин курьера в системе")
    public ValidatableResponse loginCourier(CourierLogIn logIn) {
        return given()
                .spec(getBaseSpec())
                .when()
                .body(logIn)
                .post("/api/v1/courier/login")
                .then();
    }

     @Step("Удаление курьера")
    public ValidatableResponse deleteCourier(int id) {
        return given()
                .spec(getBaseSpec())
                .when()
                .delete("/api/v1/courier/" + id)
                .then();
    }
}