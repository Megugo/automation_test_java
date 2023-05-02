package nexign_autotests.hw4.api;

import nexign_autotests.hw4.api.extentsion.ApiTestExtension;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.restassured.RestAssured.given;

@DisplayName("/catalog")
@ExtendWith(ApiTestExtension.class)
public class ApiCatalogTest {
    
    @Test
    @DisplayName("/catalog: 200, получение телефонов без авториации")
    void GetCatalogTest(){
        given()
                .get("/catalog")
                .then()
                .statusCode(200)
                .body("info.name", Matchers.hasItems("Apple iPhone 8 Plus","Apple iPhone X","HTC U11"));
    }
}
