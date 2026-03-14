import io.qameta.allure.Description;
import io.restassured.response.Response;
import models.CreateUser;
import models.UserLogin;
import org.junit.Before;
import org.junit.Test;
import io.qameta.allure.junit4.DisplayName;
import utils.UserGenerator;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;

public class UserLoginTest extends BaseTest {

    private String email;

    @Before
    public void createUser() {

        CreateUser user = UserGenerator.randomUser(PASSWORD);

        email = user.getEmail();

        Response response = userClient.registerUser(user);

        token = response.then()
                .extract()
                .path("accessToken");
    }

    @Test
    @DisplayName("Успешная авторизация пользователя")
    @Description("Проверка входа существующего пользователя")
    public void loginSuccessTest() {

        UserLogin login = new UserLogin(email, PASSWORD);

        userClient.loginUser(login)
                .then()
                .statusCode(SC_OK)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Ошибка авторизации при неправильном пароле")
    @Description("Проверка ошибки авторизации при неправильном пароле")
    public void loginWrongPasswordTest() {

        UserLogin login = new UserLogin(email, "wrongPassword");

        userClient.loginUser(login)
                .then()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false));
    }

    @Test
    @DisplayName("Ошибка авторизации при неправильном email")
    @Description("Проверка ошибки авторизации при неправильном логине")
    public void loginWrongEmailTest() {

        UserLogin login = new UserLogin("wrongemail@test.ru", PASSWORD);

        userClient.loginUser(login)
                .then()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", equalTo(false));
    }
}
