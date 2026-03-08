import io.qameta.allure.Description;
import io.restassured.response.Response;
import models.CreateOrder;
import models.CreateUser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import utils.UserGenerator;

import java.util.List;

public class CreateOrderTest extends BaseTest {

    private List<String> ingredients;

    @Before
    public void prepareData() {

        CreateUser user = UserGenerator.randomUser(PASSWORD);

        Response response = userClient.registerUser(user);
        token = response.path("accessToken");

        Response ingredientsResponse = orderClient.getIngredients();

        ingredients = ingredientsResponse.path("data._id");
    }

    @Test
    @Description("Проверка создания заказа авторизованным пользователем")
    public void createOrderAuthorized() {

        CreateOrder order = new CreateOrder(List.of(ingredients.get(0)));

        Response response = orderClient.createOrderAuthorized(order, token);

        Assert.assertEquals(200, response.statusCode());
        Assert.assertTrue(response.path("success"));
    }

    @Test
    @Description("Проверка создания заказа без авторизации")
    public void createOrderWithoutAuth() {

        CreateOrder order = new CreateOrder(List.of(ingredients.get(1)));

        Response response = orderClient.createOrder(order);

        Assert.assertEquals(200, response.statusCode());
        Assert.assertTrue(response.path("success"));
    }

    @Test
    @Description("Проверка ошибки если ингредиенты не переданы")
    public void createOrderWithoutIngredients() {

        CreateOrder order = new CreateOrder(List.of());

        Response response = orderClient.createOrder(order);

        Assert.assertEquals(400, response.statusCode());
        Assert.assertFalse(response.path("success"));
    }
    @Test
    @Description("Проверка создания заказа с ингредиентами")
    public void createOrderWithIngredients() {

        CreateOrder order = new CreateOrder(List.of(
                ingredients.get(0),
                ingredients.get(1)
        ));

        Response response = orderClient.createOrder(order);

        Assert.assertEquals(200, response.statusCode());
        Assert.assertTrue(response.path("success"));
    }
    @Test
    @Description("Проверка ошибки при создании заказа с неверным хешем ингредиентов")
    public void createOrderWithInvalidIngredientHash() {

        CreateOrder order = new CreateOrder(List.of(
                "invalidIngredientHash123"
        ));

        Response response = orderClient.createOrder(order);

        Assert.assertEquals(500, response.statusCode());
    }
}
