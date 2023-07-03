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


import static org.hamcrest.CoreMatchers.equalTo;

public class DoubleCourierTest {
    boolean isCourierCreated;
    private CourierClient courierClient;
    private int courierID;
    private Courier courier;
    public static final String ERROR_MESSAGE_DOUBLE = "Этот логин уже используется. Попробуйте другой.";

    @Before
    public void setUp() {
        courierClient = new CourierClient();
        courier = CourierGenerator.getSpecific();
    }

    @Test
    @DisplayName("Double Courier Test")
    @Description("Запрос с повторяющимся логином")
    public void createCourierTest() {
        ValidatableResponse createResponseSuccess = courierClient.createCourier(courier);

        int statusCode = createResponseSuccess.extract().statusCode();

        if (statusCode == 201) {
            ValidatableResponse loginResponse = courierClient.loginCourier(CourierLogIn.from(courier));
            isCourierCreated = createResponseSuccess.extract().path("ok");
            courierID = loginResponse.extract().path("id");

            ValidatableResponse createResponseDouble = courierClient.createCourier(courier);
            createResponseDouble.assertThat().statusCode(equalTo(409)).and().body("message", equalTo(ERROR_MESSAGE_DOUBLE));
        } else {
            isCourierCreated = false;
        }
    }

    @After
    public void cleanUp() {
        if (isCourierCreated) {
            courierClient.deleteCourier(courierID);
        }
    }
}