import io.qameta.allure.Description;
import io.restassured.response.Response;
import models.CreateOrder;
import models.CreateUser;
import org.junit.Before;
import org.junit.Test;
import io.qameta.allure.junit4.DisplayName;
import utils.UserGenerator;

import java.util.List;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;

public class CreateOrderTest extends BaseTest {

    private List<String> ingredients;

    @Before
    public void prepareData() {

        CreateUser user = UserGenerator.randomUser(PASSWORD);

        Response response = userClient.registerUser(user);

        token = response.then()
                .extract()
                .path("accessToken");

        Response ingredientsResponse = orderClient.getIngredients();

        ingredients = ingredientsResponse.path("data._id");
    }

    @Test
    @DisplayName("Создание заказа авторизованным пользователем")
    @Description("Проверка создания заказа авторизованным пользователем")
    public void createOrderAuthorizedTest() {

        CreateOrder order = new CreateOrder(List.of(ingredients.get(0)));

        orderClient.createOrderAuthorized(order, token)
                .then()
                .statusCode(SC_OK)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    @Description("Проверка создания заказа без авторизации")
    public void createOrderWithoutAuthTest() {

        CreateOrder order = new CreateOrder(List.of(ingredients.get(1)));

        orderClient.createOrder(order)
                .then()
                .statusCode(SC_OK)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Ошибка при создании заказа без ингредиентов")
    @Description("Проверка ошибки если ингредиенты не переданы")
    public void createOrderWithoutIngredientsTest() {

        CreateOrder order = new CreateOrder(List.of());

        orderClient.createOrder(order)
                .then()
                .statusCode(SC_BAD_REQUEST)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа с несколькими ингредиентами")
    @Description("Проверка создания заказа с ингредиентами")
    public void createOrderWithIngredientsTest() {

        CreateOrder order = new CreateOrder(List.of(
                ingredients.get(0),
                ingredients.get(1)
        ));

        orderClient.createOrder(order)
                .then()
                .statusCode(SC_OK)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Ошибка при создании заказа с неверным хешем ингредиента")
    @Description("Проверка ошибки при создании заказа с неверным хешем ингредиентов")
    public void createOrderWithInvalidIngredientHashTest() {

        CreateOrder order = new CreateOrder(List.of("invalidIngredientHash123"));

        orderClient.createOrder(order)
                .then()
                .statusCode(SC_INTERNAL_SERVER_ERROR);
    }
}
