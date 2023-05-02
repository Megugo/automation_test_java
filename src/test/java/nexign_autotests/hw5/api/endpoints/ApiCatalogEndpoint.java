package nexign_autotests.hw5.api.endpoints;

import nexign_autotests.hw5.api.dto.PhoneDto;
import nexign_autotests.hw5.api.dto.UserDto;

import java.util.List;

import static io.restassured.RestAssured.given;

@Endpoint("/catalog")
public class ApiCatalogEndpoint extends BaseEndpoint{

    public List<PhoneDto> getAllPhones(){
        return List.of(given()
                .get(getEndpoint())
                .then()
                .statusCode(200)
                .extract()
                .as(PhoneDto[].class));
    }
}
