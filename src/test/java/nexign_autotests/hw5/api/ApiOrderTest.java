package nexign_autotests.hw5.api;

import nexign_autotests.hw5.api.dto.Order;
import nexign_autotests.hw5.api.dto.OrderDto;
import nexign_autotests.hw5.api.dto.PhoneDto;
import nexign_autotests.hw5.api.dto.UserDto;
import nexign_autotests.hw5.api.endpoints.ApiAuthRegisterEndpoint;
import nexign_autotests.hw5.api.endpoints.ApiCatalogEndpoint;
import nexign_autotests.hw5.api.endpoints.ApiOrderEndpoint;
import nexign_autotests.hw5.api.endpoints.ApiUserEndpoint;
import nexign_autotests.hw5.api.extentsion.ApiTestExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.LocalDateTime;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("/order")
@ExtendWith(ApiTestExtension.class)
public class ApiOrderTest {
    UserDto userDto;
    PhoneDto phoneDto;
    @BeforeEach
    void setToken(){
        userDto = new ApiAuthRegisterEndpoint().registerNewUser(ApiAuthTest.successfulCreateUserRequests().findFirst().orElseThrow());
        phoneDto = new ApiCatalogEndpoint().getAllPhones().get(0);
    }

    @Test
    @DisplayName("GET ../api/order: 200, успешное оформление заказа")
    void orderSuccessfulTest(){

        Order expectedOrder = Order.builder()
                .dateCreated(LocalDateTime.now())
                .name(phoneDto.getInfo().getName())
                .price(phoneDto.getInfo().getPrice())
                .quantity(1)
                .build();

        new ApiOrderEndpoint().orderPhones(userDto, OrderDto.builder()
                .order(expectedOrder)
                .build());

        assertThat(new ApiUserEndpoint().getUser(userDto).getOrders())
                .as("У юзера добавился 1 заказ")
                .containsExactlyInAnyOrder(expectedOrder);

    }
}
