package nexign_autotests.hw5.api.endpoints;

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
}
