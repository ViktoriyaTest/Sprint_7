import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.example.Courier;
import org.example.CourierClient;
import org.example.CourierGenerator;
import org.example.CourierLogIn;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.hamcrest.CoreMatchers.equalTo;

@RunWith(Parameterized.class)
public class CreateCourierTest {
    private final Courier courier;
    private final int statusCodeExpected;
    private final String errorMessage;
    boolean isCourierCreated;
    private CourierClient courierClient;
    private int courierID;

    public static final String ERROR_MESSAGE_NOT_ENOUGH_DATA = "Недостаточно данных для создания учетной записи";

    public CreateCourierTest(Courier courier, int statusCodeExpected, String errorMessage) {
        this.courier = courier;
        this.statusCodeExpected = statusCodeExpected;
        this.errorMessage = errorMessage;
    }

    @Parameterized.Parameters(name = "Создание курьера. Тестовые данные: {0} {1} {2} {3}")
    public static Object[][] getFaqVariants() {
        return new Object[][]{
                {CourierGenerator.getRandom(), 201, ""},
                {CourierGenerator.getRandomNoLogin(), 400, ERROR_MESSAGE_NOT_ENOUGH_DATA},
                {CourierGenerator.getRandomNoPassword(), 400, ERROR_MESSAGE_NOT_ENOUGH_DATA},
                {CourierGenerator.getRandomNoFirstName(), 201, ""},
        };
    }

    @Before
    public void setUp() {
        courierClient = new CourierClient();
    }

    @Test
    @DisplayName("Test create courier")
    @Description("создание курьера")
    public void createCourierTest() {
        ValidatableResponse createResponse = courierClient.createCourier(courier);
        createResponse.assertThat().statusCode(equalTo(statusCodeExpected));
        int statusCode = createResponse.extract().statusCode();
        switch (statusCode) {
            case (201):
                createResponse.assertThat().body("ok", equalTo(true));
                isCourierCreated = createResponse.extract().path("ok");
                ValidatableResponse loginResponse = courierClient.loginCourier(CourierLogIn.from(courier));
                courierID = loginResponse.extract().path("id");
                break;
            case (400):
                isCourierCreated = false;
                createResponse.assertThat().body("message", equalTo(errorMessage));
                break;
            default:
                isCourierCreated = false;
                break;
        }
    }

    @After
    public void cleanUp() {
        if (isCourierCreated) {
            courierClient.deleteCourier(courierID);
        }
    }
}