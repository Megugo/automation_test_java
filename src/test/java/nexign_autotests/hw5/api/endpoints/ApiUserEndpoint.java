package nexign_autotests.hw5.api.endpoints;

import io.restassured.path.json.JsonPath;
import io.restassured.response.ResponseBodyExtractionOptions;
import io.restassured.response.ValidatableResponse;
import nexign_autotests.hw5.api.dto.UserDto;

import static io.restassured.RestAssured.given;

@Endpoint("/user")
public class ApiUserEndpoint extends BaseEndpoint{

    public UserDto getUser(UserDto userDto){
        return given()
                .header(userDto.authHeader())
                .get(getEndpoint())
                .then()
                .statusCode(200)
                .extract()
                .as(UserDto.class);
    }

    public JsonPath putUser(UserDto userDto){
        return given()
                .header(userDto.authHeader())
                .put(getEndpoint())
                .then()
                .statusCode(200)
                .extract()
                .jsonPath();
    }
}
