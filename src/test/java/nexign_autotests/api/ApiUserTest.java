package nexign_autotests.api;

import com.github.javafaker.Faker;
import io.restassured.http.Header;
import io.restassured.path.json.JsonPath;
import nexign_autotests.api.extentsion.ApiTestExtension;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.restassured.RestAssured.given;

@DisplayName("/user")
@ExtendWith(ApiTestExtension.class)
public class ApiUserTest {

    JsonPath jsonData;
    String userName;
    String userPassword;
    String userAddress;
    String userPhone;
    String userMail;

    @BeforeEach
    void setToken(){
        Faker faker = new Faker();
        userName = faker.name().fullName();
        userPassword = faker.internet().password();
        userAddress = faker.address().fullAddress();
        userPhone = faker.phoneNumber().phoneNumber();
        userMail = faker.internet().emailAddress();

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
    }

    @Test
    @DisplayName("GET ../api/user: 200, получение информации о юзере авторизованныи пользователем")
    void successGetUserTest(){
        given()
                .header(new Header("Authorization","Bearer "+jsonData.getString("token")))
                .get("/user")
                .then()
                .statusCode(200)
                .body("address", Matchers.equalTo(userAddress))
                .body("email", Matchers.equalTo(userMail))
                .body("phone", Matchers.equalTo(userPhone))
                .body("username", Matchers.equalTo(userName));
    }

    @Test
    @DisplayName("PUT ../api/user: 200, изменение юзера")
    void changeUserTest(){
        Faker faker = new Faker();
        userName = faker.name().fullName();
        userPassword = faker.internet().password();
        userAddress = faker.address().fullAddress();
        userPhone = faker.phoneNumber().phoneNumber();
        userMail = faker.internet().emailAddress();

        given()
                .header(new Header("Authorization","Bearer "+jsonData.getString("token")))
                .body("{\n" +
                        "  \"address\": \""+userAddress+"\",\n" +
                        "  \"email\": \""+userMail+"\",\n" +
                        "  \"id\": \""+jsonData.getString("id")+"\",\n" +
                        "  \"orders\": "+jsonData.getString("orders")+",\n" +
                        "  \"password\": \""+jsonData.getString("password")+"\",\n" +
                        "  \"phone\": \""+userPhone+"\",\n" +
                        "  \"token\": \""+jsonData.getString("token")+"\",\n" +
                        "  \"username\": \""+userName+"\"\n" +
                        "}")
                .put("/user")
                .then()
                .statusCode(200)
                .body("message", Matchers.equalTo("User updated"));
    }

    @Test
    @DisplayName("PUT ..api/user: 403, изменение пользователя без авторизации")
    void changeUserNotAuthTest(){
        Faker faker = new Faker();
        userName = faker.name().fullName();
        userPassword = faker.internet().password();
        userAddress = faker.address().fullAddress();
        userPhone = faker.phoneNumber().phoneNumber();
        userMail = faker.internet().emailAddress();

        given()
                .body("{\n" +
                        "  \"address\": \""+userAddress+"\",\n" +
                        "  \"email\": \""+userMail+"\",\n" +
                        "  \"id\": \""+jsonData.getString("id")+"\",\n" +
                        "  \"orders\": "+jsonData.getString("orders")+",\n" +
                        "  \"password\": \""+jsonData.getString("password")+"\",\n" +
                        "  \"phone\": \""+userPhone+"\",\n" +
                        "  \"token\": \""+jsonData.getString("token")+"\",\n" +
                        "  \"username\": \""+userName+"\"\n" +
                        "}")
                .put("/user")
                .then()
                .statusCode(403);
    }
}
