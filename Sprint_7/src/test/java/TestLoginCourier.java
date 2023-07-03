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
public class TestLoginCourier {
    private final int statusCodeExpected;
    private final String errorMessage;
    private final CourierLogIn credentials;
    boolean isCourierCreated;
    private CourierClient courierClient;
    private int courierID;

    public static final String ERROR_MESSAGE_NOT_ENOUGH_DATA_FOR_LOGIN = "Недостаточно данных для входа";
    public static final String ERROR_MESSAGE_NOT_DATA_FOUND = "Учетная запись не найдена";

    public TestLoginCourier(int statusCodeExpected, String errorMessage, CourierLogIn credentials) {
        this.statusCodeExpected = statusCodeExpected;
        this.errorMessage = errorMessage;
        this.credentials = credentials;
    }

    @Parameterized.Parameters(name = "Сообщения. Тестовые данные: {0} {1} {2} {3}")
    public static Object[][] getMessage() {
        return new Object[][]{
                {200, "", CourierLogIn.from(CourierGenerator.getSpecific())},
                {400, ERROR_MESSAGE_NOT_ENOUGH_DATA_FOR_LOGIN, CourierLogIn.noLoginFrom(CourierGenerator.getSpecific())},
                {400, ERROR_MESSAGE_NOT_ENOUGH_DATA_FOR_LOGIN, CourierLogIn.noPasswordFrom(CourierGenerator.getSpecific())},
                {404, ERROR_MESSAGE_NOT_DATA_FOUND, CourierLogIn.from(CourierGenerator.getSpecific())},
        };
    }

    @Before
    public void setUp() {
        Courier courier = CourierGenerator.getSpecific();
        courierClient = new CourierClient();
        if (statusCodeExpected != 404) {
            courierClient.createCourier(courier);
            isCourierCreated = true;
            ValidatableResponse login = courierClient.loginCourier(CourierLogIn.from(courier));
            courierID = login.extract().path("id");
        } else {
            isCourierCreated = false;
        }

    }

    @Test
    @DisplayName("Test логин курьера")
    public void loginCourier() {
        ValidatableResponse loginResponse = courierClient.loginCourier(credentials);
        loginResponse.assertThat().statusCode(equalTo(statusCodeExpected));
        if (loginResponse.extract().statusCode() == 200) {
            loginResponse.assertThat().body("id", equalTo(courierID));
        } else {
            loginResponse.assertThat().body("message", equalTo(errorMessage));
        }
    }

    @After
    public void cleanUp() {
        if (isCourierCreated) {
            courierClient.deleteCourier(courierID);
        }
    }
}