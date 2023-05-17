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
        userDto.setUsername(faker.name().username());

        assertThat(new ApiUserEndpoint().putUser(userDto).getString("message"))
                .isEqualTo("User updated");

    }

    @Test
    @DisplayName("PUT ..api/user: 403, изменение пользователя без авторизации")
    void changeUserNotAuthTest(){
        Faker faker = new Faker();
        userDto = ApiAuthTest.successfulCreateUserRequests().findFirst().orElseThrow();

        given()
                .body(userDto)
                .put(new ApiUserEndpoint().getEndpoint())
                .then()
                .statusCode(403);
    }
}
