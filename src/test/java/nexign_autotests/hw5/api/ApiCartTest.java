package nexign_autotests.hw5.api;

import com.github.javafaker.Faker;
import io.restassured.http.Header;
import io.restassured.path.json.JsonPath;
import nexign_autotests.hw5.api.extentsion.ApiTestExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.restassured.RestAssured.given;

@DisplayName("/cart")
@ExtendWith(ApiTestExtension.class)
public class ApiCartTest {

    String  phoneID;
    JsonPath jsonData;
    @BeforeEach
    void setToken(){
        Faker faker = new Faker();
        String userName = faker.name().fullName();
        String userPassword = faker.internet().password();
        String userAddress = faker.address().fullAddress();
        String userPhone = faker.phoneNumber().phoneNumber();
        String userMail = faker.internet().emailAddress();

        jsonData = given()
                .body("{\n" +
                        "  \"address\": \""+userAddress+"\",\n" +
                        "  \"email\": \""+userMail+"\",\n" +
                        "  \"password\": \""+userPassword+"\",\n" +
                        "  \"phone\": \""+userPhone+"\",\n" +
                        "  \"username\": \""+userName+"\"\n" +
                        "}")
                .post("/auth/register")
                .then()
                .extract()
                .jsonPath();

        phoneID = given()
                .get("/catalog")
                .then()
                .extract()
                .jsonPath()
                .getString("_id[0]");
    }
    @Test
    @DisplayName("POST ../api/cart: 200, добавить телефон в корзину")
    void addPhoneToCartTest(){
        given()
                .header(new Header("Authorization","Bearer "+jsonData.getString("token")))
                .body("{\n" +
                        "  \"product\": \""+phoneID+"\",\n" +
                        "  \"quantity\": 1,\n" +
                        "  \"user\": \""+jsonData.getString("id")+"\"\n" +
                        "}")
                .post("/cart")
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("GET ../api/cart: 200, получение телефонов в корзине")
    void GetCatalogTest(){

        given()
                .header(new Header("Authorization","Bearer "+jsonData.getString("token")))
                .body("{\n" +
                        "  \"product\": \""+phoneID+"\",\n" +
                        "  \"quantity\": 1,\n" +
                        "  \"user\": \""+jsonData.getString("id")+"\"\n" +
                        "}")
                .post("/cart");

        given()
                .header(new Header("Authorization","Bearer "+jsonData.getString("token")))
                .get("/cart")
                .then()
                .statusCode(200);
    }
}
