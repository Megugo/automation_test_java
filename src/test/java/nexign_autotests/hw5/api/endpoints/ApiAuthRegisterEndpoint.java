package nexign_autotests.hw5.api.endpoints;

import nexign_autotests.hw5.api.dto.UserDto;

import static io.restassured.RestAssured.given;

@Endpoint("/auth/register")
public class ApiAuthRegisterEndpoint extends BaseEndpoint{

    public UserDto registerNewUser(UserDto userDto){
        return given()
                .body(userDto)
                .post(getEndpoint())
                .then()
                .statusCode(201)
                .extract()
                .as(UserDto.class);
    }
}
