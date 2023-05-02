package nexign_autotests.hw5.api_additional;


import nexign_autotests.hw5.api_additional.extentsion.ApiTestExtension;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.restassured.RestAssured.given;
@DisplayName("POST ../auth")
@ExtendWith(ApiTestExtension.class)
public class ApiAuthTest {
    @Test
    @DisplayName("/auth : 200, успешное создание юзера")
    void authTest(){
        String userName = "admin";
        String userPassword = "password123";

        given()
                .body("{\n" +
                        "    \"username\" : \""+userName+"\",\n" +
                        "    \"password\" : \""+userPassword+"\"\n" +
                        "}")
                .post("/auth")
                .then()
                .statusCode(200)
                .body("token",Matchers.notNullValue());
    }
}



