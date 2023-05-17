package nexign_autotests.hw5.api.endpoints;

import nexign_autotests.hw5.api.dto.UserDto;

import static io.restassured.RestAssured.given;

@Endpoint("/auth/login")
public class ApiLoginEndpoint extends BaseEndpoint{

    public UserDto loginUser(UserDto userDto){
        return given()
                .body(userDto)
                .post(getEndpoint())
                .then()
                .statusCode(200)
                .extract()
                .as(UserDto.class);
    }
}
