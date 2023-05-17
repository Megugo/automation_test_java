package nexign_autotests.hw5.api.endpoints;

import io.restassured.response.ResponseBodyExtractionOptions;
import nexign_autotests.hw5.api.dto.*;

import static io.restassured.RestAssured.given;

@Endpoint("/cart")
public class ApiCartEndoint extends BaseEndpoint{

    public CartDto getCart(UserDto userDto){
        return given()
                .header(userDto.authHeader())
                .get(getEndpoint())
                .then()
                .statusCode(200)
                .extract()
                .as(CartDto.class);
    }

    public void addToCart(UserDto userDto, AddCartDto addCartDto){
        given()
                .header(userDto.authHeader())
                .body(addCartDto)
                .post(getEndpoint())
                .then()
                .statusCode(200);
    }

    public void deleteFromCart(UserDto userDto, DeleteFromCartDto deleteFromCartDto){
        given()
                .header(userDto.authHeader())
                .body(deleteFromCartDto)
                .put(getEndpoint())
                .then()
                .statusCode(200);
    }

    public void clearCart(UserDto userDto,String id){
        given()
                .header(userDto.authHeader())
                .queryParam("id",id)
                .delete(getEndpoint())
                .then()
                .statusCode(200);
    }
}
