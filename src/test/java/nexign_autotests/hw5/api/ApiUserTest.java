package nexign_autotests.hw5.api;

import com.github.javafaker.Faker;
import io.restassured.http.Header;
import io.restassured.path.json.JsonPath;
import nexign_autotests.hw5.api.dto.UserDto;
import nexign_autotests.hw5.api.endpoints.ApiAuthRegisterEndpoint;
import nexign_autotests.hw5.api.endpoints.ApiUserEndpoint;
import nexign_autotests.hw5.api.extentsion.ApiTestExtension;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("/user")
@ExtendWith(ApiTestExtension.class)
public class ApiUserTest {

    JsonPath jsonData;
    String userName;
    String userPassword;
    String userAddress;
    String userPhone;
    String userMail;
    UserDto userDto;

    @BeforeEach
    void createUser(){
        userDto = new ApiAuthRegisterEndpoint().registerNewUser(ApiAuthTest.successfulCreateUserRequests().findFirst().orElseThrow());
    }

    @Test
    @DisplayName("GET ../api/user: 200, получение информации о юзере авторизованныи пользователем")
    void successGetUserTest(){

        assertThat(new ApiUserEndpoint().getUser(userDto))
                .isEqualTo(userDto);
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
                .header(new Header("Authorization","Bearer "+userDto.getToken()))
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
