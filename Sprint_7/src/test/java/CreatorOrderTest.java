import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.example.ColorArray;
import org.example.Order;
import org.example.OrderClient;
import org.example.OrderGenerator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(Parameterized.class)
public class CreatorOrderTest {
    private OrderClient orderClient;
    private final Order order;

    public CreatorOrderTest(Order order) {
        this.order = order;
    }

    @Parameterized.Parameters(name = "Заказ. Тестовые данные: {0} {1} {2} {3}")
    public static Object[][] getOrderVariants() {
        return new Object[][]{
                {OrderGenerator.getOrder(ColorArray.getListBlack())},
                {OrderGenerator.getOrder(ColorArray.getListGrey())},
                {OrderGenerator.getOrder(ColorArray.getListBlackAndGrey())},
                {OrderGenerator.getOrder(null)},
        };
    }

    @Before
    public void setUp() {
        orderClient = new OrderClient();
    }

    @Test
    @DisplayName("Order Test")
    @Description("Создаем заказы, проверяем track")
    public void testOrderCreate() {
        ValidatableResponse createResponse = orderClient.createOrder(order);
        createResponse.assertThat().statusCode(equalTo(201));
        createResponse.assertThat().body("track", notNullValue());
        createResponse.extract().path("track");
    }
}