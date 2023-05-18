package nexign_autotests.hw5.api_additional.endpoints;

import nexign_autotests.hw5.api_additional.dto.AuthDto;

import static io.restassured.RestAssured.given;

@Endpoint("/auth")
public class ApiAuthEndpoint extends BaseEndpoint {
    public String getToken(AuthDto authDto){
        return given()
                .body(authDto)
                .post(getEndpoint())
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getString("token");
    }
}
