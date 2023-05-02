package nexign_autotests.hw5.api.endpoints;

import nexign_autotests.hw5.api.dto.OrderDto;
import nexign_autotests.hw5.api.dto.UserDto;

import static io.restassured.RestAssured.given;

@Endpoint("/order")
public class ApiOrderEndpoint extends BaseEndpoint{

    public void orderPhones(UserDto userDto, OrderDto orderRequestDto){
        given()
                .header(userDto.authHeader())
                .body(orderRequestDto)
                .post(getEndpoint())
                .then()
                .statusCode(200);
    }
}
