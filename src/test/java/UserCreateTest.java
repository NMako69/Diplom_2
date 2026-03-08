import io.qameta.allure.Description;
import io.restassured.response.Response;
import models.CreateUser;
import org.junit.Assert;
import org.junit.Test;
import utils.UserGenerator;

public class UserCreateTest extends BaseTest {

    @Test
    @Description("Проверка успешной регистрации нового уникального пользователя")
    public void createUniqueUser() {

        // создаём случайного пользователя через Faker
        CreateUser user = UserGenerator.randomUser(PASSWORD);

        Response response = userClient.registerUser(user);

        Assert.assertEquals(200, response.statusCode());
        Assert.assertTrue(response.path("success"));

        token = response.path("accessToken");
    }

    @Test
    @Description("Проверка что нельзя создать одного пользователя дважды")
    public void createDuplicateUser() {

        CreateUser user = UserGenerator.randomUser(PASSWORD);

        Response firstResponse = userClient.registerUser(user);
        token = firstResponse.path("accessToken");

        Response secondResponse = userClient.registerUser(user);

        Assert.assertEquals(403, secondResponse.statusCode());
        Assert.assertFalse(secondResponse.path("success"));
    }

    @Test
    @Description("Проверка ошибки при создании пользователя без email")
    public void cannotCreateUserWithoutEmail() {

        CreateUser user = UserGenerator.userWithoutEmail(PASSWORD);

        Response response = userClient.registerUser(user);

        Assert.assertEquals(403, response.statusCode());
        Assert.assertFalse(response.path("success"));
        Assert.assertEquals("Email, password and name are required fields",
                response.path("message"));
    }

    @Test
    @Description("Проверка ошибки при создании пользователя без password")
    public void cannotCreateUserWithoutPassword() {

        CreateUser user = UserGenerator.userWithoutPassword();

        Response response = userClient.registerUser(user);

        Assert.assertEquals(403, response.statusCode());
        Assert.assertFalse(response.path("success"));
        Assert.assertEquals("Email, password and name are required fields",
                response.path("message"));
    }

    @Test
    @Description("Проверка ошибки при создании пользователя без name")
    public void cannotCreateUserWithoutName() {

        CreateUser user = UserGenerator.userWithoutName(PASSWORD);

        Response response = userClient.registerUser(user);

        Assert.assertEquals(403, response.statusCode());
        Assert.assertFalse(response.path("success"));
        Assert.assertEquals("Email, password and name are required fields",
                response.path("message"));
    }
}
