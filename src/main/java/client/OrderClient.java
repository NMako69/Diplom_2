package client;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.response.Response;
import models.CreateOrder;

import static io.restassured.RestAssured.given;

public class OrderClient {

    private static final String ORDERS = "/api/orders";
    private static final String INGREDIENTS = "/api/ingredients";

    @Step("Получение списка ингредиентов")
    public Response getIngredients() {
        return given()
                .filter(new AllureRestAssured())
                .header("Content-type", "application/json")
                .get(INGREDIENTS);
    }

    @Step("Создание заказа без авторизации")
    public Response createOrder(CreateOrder order) {
        return given()
                .filter(new AllureRestAssured())
                .header("Content-type", "application/json")
                .body(order)
                .post(ORDERS);
    }

    @Step("Создание заказа с авторизацией")
    public Response createOrderAuthorized(CreateOrder order, String token) {
        return given()
                .filter(new AllureRestAssured())
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .body(order)
                .post(ORDERS);
    }
}