import io.qameta.allure.Description;
import io.restassured.response.Response;
import models.CreateUser;
import org.junit.Test;
import io.qameta.allure.junit4.DisplayName;
import utils.UserGenerator;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;

public class UserCreateTest extends BaseTest {

    @Test
    @DisplayName("Успешная регистрация нового пользователя")
    @Description("Проверка успешной регистрации нового уникального пользователя")
    public void createUniqueUserTest() {

        CreateUser user = UserGenerator.randomUser(PASSWORD);

        Response response = userClient.registerUser(user);

        response.then()
                .statusCode(SC_OK)
                .body("success", equalTo(true));

        token = response.then()
                .extract()
                .path("accessToken");
    }

    @Test
    @DisplayName("Ошибка при повторной регистрации пользователя")
    @Description("Проверка что нельзя создать одного пользователя дважды")
    public void createDuplicateUserTest() {

        CreateUser user = UserGenerator.randomUser(PASSWORD);

        Response firstResponse = userClient.registerUser(user);

        token = firstResponse.then()
                .extract()
                .path("accessToken");

        userClient.registerUser(user)
                .then()
                .statusCode(SC_FORBIDDEN)
                .body("success", equalTo(false));
    }

    @Test
    @DisplayName("Ошибка при регистрации без email")
    @Description("Проверка ошибки при создании пользователя без email")
    public void cannotCreateUserWithoutEmailTest() {

        CreateUser user = UserGenerator.userWithoutEmail(PASSWORD);

        userClient.registerUser(user)
                .then()
                .statusCode(SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message",
                        equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Ошибка при регистрации без password")
    @Description("Проверка ошибки при создании пользователя без password")
    public void cannotCreateUserWithoutPasswordTest() {

        CreateUser user = UserGenerator.userWithoutPassword();

        userClient.registerUser(user)
                .then()
                .statusCode(SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message",
                        equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Ошибка при регистрации без name")
    @Description("Проверка ошибки при создании пользователя без name")
    public void cannotCreateUserWithoutNameTest() {

        CreateUser user = UserGenerator.userWithoutName(PASSWORD);

        userClient.registerUser(user)
                .then()
                .statusCode(SC_FORBIDDEN)
                .body("success", equalTo(false))
                .body("message",
                        equalTo("Email, password and name are required fields"));
    }
}
