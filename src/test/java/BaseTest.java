import client.OrderClient;
import client.UserClient;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;

public class BaseTest {

    protected static final String PASSWORD = "123456";
    protected static final String NAME = "TestUser";

    protected UserClient userClient;
    protected OrderClient orderClient;

    protected String token;

    @Before
    public void setUp() {
        setBaseUri();
        userClient = new UserClient();
        orderClient = new OrderClient();
    }

    @After
    public void cleanUp() {
        if (token != null) deleteUser();
    }

    @Step("Установка базового адреса API")
    public void setBaseUri() {
        RestAssured.baseURI = "https://stellarburgers.education-services.ru";
    }

    @Step("Удаление пользователя после теста")
    public void deleteUser() {
        userClient.deleteUser(token).then().statusCode(202);
    }
}
