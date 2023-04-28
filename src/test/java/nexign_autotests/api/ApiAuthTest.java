package nexign_autotests.api;


import com.github.javafaker.Faker;
import nexign_autotests.api.extentsion.ApiTestExtension;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.restassured.RestAssured.given;

public class ApiAuthTest {
    @Nested
    @DisplayName("POST ../api/auth/register")
    @ExtendWith(ApiTestExtension.class)
    public class ApiRegisterTest {

        @Test
        @DisplayName("/auth/register: 200, успешное создание юзера")
        void createUserTest(){
            Faker faker = new Faker();
            String userName = faker.name().fullName();
            String userAddress = faker.address().fullAddress();
            String userPhone = faker.phoneNumber().phoneNumber();
            String userMail = faker.internet().emailAddress();

            given()
                    .body("{\n" +
                            "  \"address\": \"russia\",\n" +
                            "  \"email\": \"sdgrdsg@vas.ru\",\n" +
                            "  \"password\": \"vasya\",\n" +
                            "  \"phone\": \"8999999999\",\n" +
                            "  \"username\": \""+userName+"\"\n" +
                            "}")
                    .post("/auth/register")
                    .then()
                    .statusCode(201)
                    .body("address", Matchers.equalTo("russia"))
                    .body("email", Matchers.equalTo("sdgrdsg@vas.ru"))
                    .body("phone", Matchers.equalTo("8999999999"))
                    .body("username", Matchers.equalTo(userName));
        }

        @Test
        @DisplayName("/auth/register: 409, регистрация на занятое имя")
        void createExistUserTest(){
            Faker faker = new Faker();
            String userName = faker.name().fullName();
            String userAddress = faker.address().fullAddress();
            String userPhone = faker.phoneNumber().phoneNumber();
            String userMail = faker.internet().emailAddress();

            given()
                    .body("{\n" +
                            "  \"address\": \"russia\",\n" +
                            "  \"email\": \"sdgrdsg@vas.ru\",\n" +
                            "  \"password\": \"vasya\",\n" +
                            "  \"phone\": \"8999999999\",\n" +
                            "  \"username\": \""+userName+"\"\n" +
                            "}")
                    .post("/auth/register");

            given()
                    .body("{\n" +
                            "  \"address\": \"russia\",\n" +
                            "  \"email\": \"sdgrdsg@vas.ru\",\n" +
                            "  \"password\": \"vasya\",\n" +
                            "  \"phone\": \"8999999999\",\n" +
                            "  \"username\": \""+userName+"\"\n" +
                            "}")
                    .post("/auth/register")
                    .then()
                    .statusCode(409)
                    .body("message", Matchers.equalTo("User already exists"));
        }
    }

    @Nested
    @DisplayName("POST ../api/auth/login")
    @ExtendWith(ApiTestExtension.class)
    public class ApiLoginTest {
        String userName;
        String userPassword;
        String userAddress;
        String userPhone;
        String userMail;

        @BeforeEach
        void setToken(){
            Faker faker = new Faker();
            userName = faker.name().fullName();
            userPassword = faker.internet().password();
            userAddress = faker.address().fullAddress();
            userPhone = faker.phoneNumber().phoneNumber();
            userMail = faker.internet().emailAddress();


            given()
                    .body("{\n" +
                            "  \"address\": \""+userAddress+"\",\n" +
                            "  \"email\": \""+userMail+"\",\n" +
                            "  \"password\": \""+userPassword+"\",\n" +
                            "  \"phone\": \""+userPhone+"\",\n" +
                            "  \"username\": \""+userName+"\"\n" +
                            "}")
                    .post("/auth/register");
        }
        @Test
        @DisplayName("/auth/login: 200, успешное вход юзера")
        void loginUserTest(){
            given()
                    .body("{\n" +
                            "  \"password\": \""+userPassword+"\",\n" +
                            "  \"username\": \""+userName+"\"\n" +
                            "}")
                    .post("/auth/login")
                    .then()
                    .statusCode(200)
                    .body("address", Matchers.equalTo(userAddress))
                    .body("email", Matchers.equalTo(userMail))
                    .body("phone", Matchers.equalTo(userPhone))
                    .body("username", Matchers.equalTo(userName));

        }

        @Test
        @DisplayName("/auth/login: 401, вход под несуществующими кредами")
        void loginNotExistUserTest(){
            given()
                    .body("{\n" +
                            "  \"password\": \""+userName+"\",\n" +
                            "  \"username\": \""+userPassword+"\"\n" +
                            "}")
                    .post("/auth/login")
                    .then()
                    .statusCode(401)
                    .body("message", Matchers.equalTo("No user found"));

        }

        @Test
        @DisplayName("/auth/login: 401, вход под неверными кредами (пароль=логин)")
        void loginInvalidUserTest(){
            given()
                    .body("{\n" +
                            "  \"password\": \""+userName+"\",\n" +
                            "  \"username\": \""+userName+"\"\n" +
                            "}")
                    .post("/auth/login")
                    .then()
                    .statusCode(401)
                    .body("message", Matchers.equalTo("Invalid credentials"));

        }
    }
}



