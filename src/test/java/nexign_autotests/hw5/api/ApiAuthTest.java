package nexign_autotests.hw5.api;


import com.github.javafaker.Faker;
import nexign_autotests.hw5.api.dto.UserDto;
import nexign_autotests.hw5.api.endpoints.ApiAuthRegisterEndpoint;
import nexign_autotests.hw5.api.endpoints.ApiLoginEndpoint;
import nexign_autotests.hw5.api.extentsion.ApiTestExtension;
import org.assertj.core.api.SoftAssertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class ApiAuthTest {

    public static Stream<UserDto> successfulCreateUserRequests(){

        Faker faker = new Faker();

        return Stream.of(
                UserDto.builder()
                        .username(faker.name().fullName())
                        .password(faker.internet().password())
                        .build(),
                UserDto.builder()
                        .username(faker.name().fullName())
                        .password(faker.internet().password())
                        .phone(faker.phoneNumber().phoneNumber())
                        .email(faker.internet().emailAddress())
                        .address(faker.address().fullAddress())
                        .build());
    }
    @Nested
    @DisplayName("POST ../api/auth/register")
    @ExtendWith(ApiTestExtension.class)
    public class ApiRegisterTests {
        @ParameterizedTest
        @DisplayName("/auth/register: 200, успешное создание юзера")
        @MethodSource("successfulCreateUserRequests")
        void createUserTest(UserDto userDto){


            UserDto actualUser = new ApiAuthRegisterEndpoint().registerNewUser(userDto);

            SoftAssertions softAssertions = new SoftAssertions();

            softAssertions.assertThat(actualUser)
                    .as("Созданный юзер должен быть равен ожидаемому")
                    .usingRecursiveComparison()
                    .ignoringFields("id","orders","password","token")
                    .isEqualTo(userDto);

            softAssertions.assertThat(actualUser.getId()).isNotEmpty();
            softAssertions.assertThat(actualUser.getToken()).isNotEmpty();
            softAssertions.assertThat(actualUser.getPassword()).isNotEmpty();
            softAssertions.assertThat(actualUser.getOrders()).isEmpty();

            softAssertions.assertAll();
        }

        @Test
        @DisplayName("/auth/register: 409, регистрация на занятое имя")
        void createExistUserTest(){
            UserDto userDto = successfulCreateUserRequests().findFirst().orElseThrow();
            new ApiAuthRegisterEndpoint().registerNewUser(userDto);

            given()
                    .body(userDto)
                    .post(new ApiAuthRegisterEndpoint().getEndpoint())
                    .then()
                    .statusCode(409)
                    .body("message", Matchers.equalTo("User already exists"));
        }
    }

    @Nested
    @DisplayName("POST ../api/auth/login")
    @ExtendWith(ApiTestExtension.class)
    public class ApiLoginTests {

        UserDto userDto;
        @BeforeEach
        void createUser(){
            userDto = successfulCreateUserRequests().findFirst().orElseThrow();
            new ApiAuthRegisterEndpoint().registerNewUser(userDto);
        }
        @Test
        @DisplayName("/auth/login: 200, успешное вход юзера")
        void loginUserTest(){
            UserDto actualUser = new ApiLoginEndpoint().loginUser(userDto);

            SoftAssertions softAssertions = new SoftAssertions();

            softAssertions.assertThat(actualUser)
                    .as("Созданный юзер должен быть равен залогиненому")
                    .usingRecursiveComparison()
                    .ignoringFields("id","orders","password","token")
                    .isEqualTo(userDto);

            softAssertions.assertThat(actualUser.getId()).isNotEmpty();
            softAssertions.assertThat(actualUser.getToken()).isNotEmpty();
            softAssertions.assertThat(actualUser.getPassword()).isNotEmpty();
            softAssertions.assertThat(actualUser.getOrders()).isEmpty();

            softAssertions.assertAll();

        }

        @Test
        @DisplayName("/auth/login: 401, вход под неверными кредами (пароль=логин)")
        void loginInvalidUserTest(){
            userDto.setPassword(userDto.getUsername());

            given()
                    .body(userDto)
                    .post(new ApiLoginEndpoint().getEndpoint())
                    .then()
                    .statusCode(401)
                    .body("message", Matchers.equalTo("Invalid credentials"));

        }




        @Test
        @DisplayName("/auth/login: 401, вход под несуществующими кредами")
        void loginNotExistUserTest(){
            userDto = ApiAuthTest.successfulCreateUserRequests().findFirst().orElseThrow();
            given()
                    .body(userDto)
                    .post(new  ApiLoginEndpoint().getEndpoint())
                    .then()
                    .statusCode(401)
                    .body("message", Matchers.equalTo("No user found"));

        }


    }
}



