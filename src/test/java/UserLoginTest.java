import io.qameta.allure.Description;
import io.restassured.response.Response;
import models.CreateUser;
import models.UserLogin;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import utils.UserGenerator;

public class UserLoginTest extends BaseTest {

    private String email;

    @Before
    public void createUser() {

        CreateUser user = UserGenerator.randomUser(PASSWORD);
        email = user.getEmail();

        Response response = userClient.registerUser(user);

        token = response.path("accessToken");
    }

    @Test
    @Description("Проверка входа существующего пользователя")
    public void loginSuccess() {

        UserLogin login = new UserLogin(email, PASSWORD);

        Response response = userClient.loginUser(login);

        Assert.assertEquals(200, response.statusCode());
        Assert.assertTrue(response.path("success"));
    }

    @Test
    @Description("Проверка ошибки авторизации при неправильном пароле")
    public void loginWrongPassword() {

        UserLogin login = new UserLogin(email, "wrongPassword");

        Response response = userClient.loginUser(login);

        Assert.assertEquals(401, response.statusCode());
        Assert.assertFalse(response.path("success"));
    }
    @Test
    @Description("Проверка ошибки авторизации при неправильном логине")
    public void loginWrongEmail() {

        UserLogin login = new UserLogin("wrongemail@test.ru", PASSWORD);

        Response response = userClient.loginUser(login);

        Assert.assertEquals(401, response.statusCode());
        Assert.assertFalse(response.path("success"));
    }
}
