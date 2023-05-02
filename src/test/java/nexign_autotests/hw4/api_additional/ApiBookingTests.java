package nexign_autotests.hw4.api_additional;

import com.github.javafaker.Faker;
import nexign_autotests.hw4.api_additional.extentsion.ApiTestExtension;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.restassured.RestAssured.given;
@DisplayName("../booking")
@ExtendWith(ApiTestExtension.class)
public class ApiBookingTests {

    String bookingID;

    @Test
    @DisplayName("GET ../booking : 200, получить список букинга")
    void getBookingIdsTest() {

        given()
                .get("/booking")
                .then()
                .statusCode(200)
                .body(Matchers.notNullValue());
    }

    @Test
    @DisplayName("GET ../booking/bookingID : 200, получить букинг")
    void getBookingTest() {

        bookingID = given()
                .get("/booking")
                .then()
                .extract()
                .jsonPath()
                .getString("bookingid[0]");

        given()
                .get("/booking/" + bookingID)
                .then()
                .statusCode(200)
                .body(Matchers.notNullValue());
    }

    @Test
    @DisplayName("POST ../booking : 200, создать букинг")
    void createBookingTest() {

        Faker faker = new Faker();
        String userName = faker.name().firstName();

        given()
                .body("{\n" +
                        "    \"firstname\" : \"" + userName + "\",\n" +
                        "    \"lastname\" : \"Brown\",\n" +
                        "    \"totalprice\" : 111,\n" +
                        "    \"depositpaid\" : true,\n" +
                        "    \"bookingdates\" : {\n" +
                        "        \"checkin\" : \"2018-01-01\",\n" +
                        "        \"checkout\" : \"2019-01-01\"\n" +
                        "    },\n" +
                        "    \"additionalneeds\" : \"Breakfast\"\n" +
                        "}")
                .post("/booking")
                .then()
                .statusCode(200)
                .body("firstname", Matchers.equalTo(userName));


    }

    @Nested
    @DisplayName("../booking зависимые от токена")
    @ExtendWith(nexign_autotests.hw4.api.extentsion.ApiTestExtension.class)
    public class ApiRegisterTest {
        String userFirstName;
        String userName = "admin";
        String userPassword = "password123";
        String bookingid;
        String token;

        @BeforeEach
        void setParams() {
            Faker faker = new Faker();
            String userFirstName = faker.name().firstName();

            token = given()
                    .body("{\n" +
                            "    \"username\" : \"" + userName + "\",\n" +
                            "    \"password\" : \"" + userPassword + "\"\n" +
                            "}")
                    .post("/auth")
                    .then()
                    .extract()
                    .jsonPath()
                    .getString("token");

            bookingid = given()
                    .body("{\n" +
                            "    \"firstname\" : \"" + userFirstName + "\",\n" +
                            "    \"lastname\" : \"Brown\",\n" +
                            "    \"totalprice\" : 111,\n" +
                            "    \"depositpaid\" : true,\n" +
                            "    \"bookingdates\" : {\n" +
                            "        \"checkin\" : \"2018-01-01\",\n" +
                            "        \"checkout\" : \"2019-01-01\"\n" +
                            "    },\n" +
                            "    \"additionalneeds\" : \"Breakfast\"\n" +
                            "}")
                    .post("/booking")
                    .then()
                    .extract()
                    .jsonPath()
                    .getString("bookingid");

        }

        @Test
        @DisplayName("PUT ../booking : 200, изменить букинг")
        void updateBookingTest() {
            Faker faker = new Faker();
            userFirstName = faker.name().firstName();

            given()
                    .cookie("token", token)
                    .body("{\n" +
                            "    \"firstname\" : \"" + userFirstName + "\",\n" +
                            "    \"lastname\" : \"Brown\",\n" +
                            "    \"totalprice\" : 111,\n" +
                            "    \"depositpaid\" : true,\n" +
                            "    \"bookingdates\" : {\n" +
                            "        \"checkin\" : \"2018-01-01\",\n" +
                            "        \"checkout\" : \"2019-01-01\"\n" +
                            "    },\n" +
                            "    \"additionalneeds\" : \"Breakfast\"\n" +
                            "}")
                    .put("/booking/" + bookingid)
                    .then()
                    .statusCode(200)
                    .body("firstname", Matchers.equalTo(userFirstName));

        }

        @Test
        @DisplayName("PATCH ../booking : 200, изменить букинг")
        void partialUpdateBookingTest() {
            Faker faker = new Faker();
            userFirstName = faker.name().firstName();

            given()
                    .cookie("token", token)
                    .body("{\n" +
                            "    \"firstname\" : \"" + userFirstName + "\",\n" +
                            "    \"lastname\" : \"Brown\",\n" +
                            "    \"totalprice\" : 222\n" +
                            "}")
                    .patch("/booking/" + bookingid)
                    .then()
                    .statusCode(200)
                    .body("firstname", Matchers.equalTo(userFirstName));

        }

        @Test
        @DisplayName("delete ../booking : 200, удалить букинг")
        void deleteBookingTest() {
            Faker faker = new Faker();

            userFirstName = faker.name().firstName();
            given()
                    .cookie("token", token)
                    .delete("/booking/" + bookingid)
                    .then()
                    .statusCode(201);

        }

    }

    @Test
    @DisplayName("GET ../ping : 200, пинг сервера")
    void healthCheckTest() {
        given()
                .get("/ping")
                .then()
                .statusCode(201);
    }
}
