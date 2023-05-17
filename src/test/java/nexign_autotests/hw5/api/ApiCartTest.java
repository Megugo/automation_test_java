package nexign_autotests.hw5.api;

import io.restassured.path.json.JsonPath;
import nexign_autotests.hw5.api.dto.*;
import nexign_autotests.hw5.api.endpoints.ApiAuthRegisterEndpoint;
import nexign_autotests.hw5.api.endpoints.ApiCartEndoint;
import nexign_autotests.hw5.api.endpoints.ApiCatalogEndpoint;
import nexign_autotests.hw5.api.extentsion.ApiTestExtension;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static io.restassured.RestAssured.given;

@DisplayName("/cart")
@ExtendWith(ApiTestExtension.class)
public class ApiCartTest {

    String  phoneID;
    JsonPath jsonData;
    UserDto userDto;
    List<PhoneDto> phoneDtos;
    CartDto cartDtoList;

    @BeforeEach
    void setToken(){

        userDto = new ApiAuthRegisterEndpoint().registerNewUser(ApiAuthTest.successfulCreateUserRequests().findFirst().orElseThrow());
        phoneDtos = new ApiCatalogEndpoint().getAllPhones();
    }
    @Test
    @DisplayName("POST ../api/cart: 200, добавить телефон в корзину")
    void AddPhoneToCartTest(){

        new ApiCartEndoint().addToCart(userDto,AddCartDto.builder()
                        .user(userDto.getId())
                        .product(phoneDtos.get(0).getId())
                        .quantity(1)
                .build());
    }

    @Test
    @DisplayName("GET ../api/cart: 200, получение телефонов в корзине")
    void GetCartTest(){

        new ApiCartEndoint().addToCart(userDto,AddCartDto.builder()
                .user(userDto.getId())
                .product(phoneDtos.get(0).getId())
                .quantity(1)
                .build());

        CartDto cartDto = new ApiCartEndoint().getCart(userDto);

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(cartDto.getItems().get(0).getProduct().getId()).isEqualTo(phoneDtos.get(0).getId());
        softAssertions.assertThat(cartDto.getUser()).isEqualTo(userDto.getId());

        softAssertions.assertAll();
    }
    @Nested
    @DisplayName("Изменение корзины")
    @ExtendWith(ApiTestExtension.class)
    public class ApiRegisterTests {

        CartDto beforeCartDto;

        @BeforeEach
        void setToken(){

            userDto = new ApiAuthRegisterEndpoint().registerNewUser(ApiAuthTest.successfulCreateUserRequests().findFirst().orElseThrow());
            phoneDtos = new ApiCatalogEndpoint().getAllPhones();

            new ApiCartEndoint().addToCart(userDto,AddCartDto.builder()
                    .user(userDto.getId())
                    .product(phoneDtos.get(0).getId())
                    .quantity(1)
                    .build());

            beforeCartDto = new ApiCartEndoint().getCart(userDto);
        }
        @Test
        @DisplayName("PUT ../api/cart: 200, удалить телефон из корзины")
        void DeleteFromCartTest(){


            new ApiCartEndoint().deleteFromCart(userDto, DeleteFromCartDto.builder()
                            .cartId(beforeCartDto.getId())
                            .itemId(beforeCartDto.getItems().get(0).getId())
                    .build());

            CartDto afterCartDto = new ApiCartEndoint().getCart(userDto);

            SoftAssertions softAssertions = new SoftAssertions();
            softAssertions.assertThat(afterCartDto.getItems().size()).isEqualTo(beforeCartDto.getItems().size()-1);
            softAssertions.assertAll();
        }

        @Test
        @DisplayName("DELETE ../api/cart: 200, очистить корзину")
        void ClearCartTest() {
            new ApiCartEndoint().clearCart(userDto, beforeCartDto.getId());

        }

        @Test
        @DisplayName("PUT ../api/cart: 403,  удалить телефон из корзины без авторизации")
        void unsuccessfulDeleteItemFromCartWithoutAuthTest() {

            given()
                    .body(DeleteFromCartDto.builder()
                            .cartId(beforeCartDto.getId())
                            .itemId(beforeCartDto.getItems().get(0).getId())
                            .build())
                    .put(new ApiCartEndoint().getEndpoint())
                    .then()
                    .statusCode(403);
        }

        @Test
        @DisplayName("DELETE ../api/cart: 403, очистить корзину без авторизации")
        void unsuccessfulClearCartWithoutAuthTest() {

            given()
                    .queryParam("id",beforeCartDto.getId())
                    .delete(new ApiCartEndoint().getEndpoint())
                    .then()
                    .statusCode(403);;
        }

    }
}
