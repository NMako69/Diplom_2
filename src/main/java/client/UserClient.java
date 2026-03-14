package client;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.response.Response;
import models.CreateUser;
import models.UserLogin;

import static io.restassured.RestAssured.given;

public class UserClient {

    private static final String REGISTER = "/api/auth/register";
    private static final String LOGIN = "/api/auth/login";
    private static final String DELETE = "/api/auth/user";

    @Step("Регистрация нового пользователя")
    public Response registerUser(CreateUser user) {
        return given()
                .filter(new AllureRestAssured())
                .header("Content-type", "application/json")
                .body(user)
                .post(REGISTER);
    }

    @Step("Авторизация пользователя")
    public Response loginUser(UserLogin login) {
        return given()
                .filter(new AllureRestAssured())
                .header("Content-type", "application/json")
                .body(login)
                .post(LOGIN);
    }

    @Step("Удаление пользователя по токену")
    public Response deleteUser(String token) {
        return given()
                .filter(new AllureRestAssured())
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .delete(DELETE);
    }
}
