package nexign_autotests.hw5.api;

import nexign_autotests.hw5.api.dto.PhoneDto;
import nexign_autotests.hw5.api.endpoints.ApiCatalogEndpoint;
import nexign_autotests.hw5.api.extentsion.ApiTestExtension;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("/catalog")
@ExtendWith(ApiTestExtension.class)
public class ApiCatalogTest {
    
    @Test
    @DisplayName("/catalog: 200, получение телефонов без авториации")
    void GetCatalogTest(){

        assertThat(new ApiCatalogEndpoint().getAllPhones().stream()
                .map(phoneDto -> phoneDto.getInfo().getName()).collect(Collectors.toList()))
                .contains("Apple iPhone 8 Plus", "Apple iPhone X", "HTC U11");
    }
}
