package nexign_autotests.hw5.api_additional;


import nexign_autotests.hw5.api_additional.dto.AuthDto;
import nexign_autotests.hw5.api_additional.endpoints.ApiAuthEndpoint;
import nexign_autotests.hw5.api_additional.extentsion.ApiTestExtension;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

import static io.restassured.RestAssured.given;
@DisplayName("POST ../auth")
@ExtendWith(ApiTestExtension.class)
public class ApiAuthTest {
    @Test
    @DisplayName("/auth : 200, получение токена")
    void authTest(){

        assertThat(new ApiAuthEndpoint().getToken(AuthDto.builder()
                .username("admin")
                .password("password123")
                .build())).isNotEmpty();

    }
}



