package nexign_autotests.hw5.api_additional;

import com.github.javafaker.Faker;
import nexign_autotests.hw5.api_additional.dto.AuthDto;
import nexign_autotests.hw5.api_additional.dto.BookingDto;
import nexign_autotests.hw5.api_additional.endpoints.ApiAuthEndpoint;
import nexign_autotests.hw5.api_additional.endpoints.ApiBookingEndpoint;
import nexign_autotests.hw5.api_additional.extentsion.ApiTestExtension;
import nexign_autotests.hw5.api_additional.generator.DtosGenerator;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.assertj.core.api.Assertions.assertThat;

import static io.restassured.RestAssured.given;
@ExtendWith(ApiTestExtension.class)
public class ApiBookingTests {

    int bookingID;

    @Test
    @DisplayName("GET ../booking : 200, получить список букинга")
    void getBookingIdsTest() {

        assertThat(new ApiBookingEndpoint().getBookingIDs()).isNotNull();

    }

    @Test
    @DisplayName("GET ../booking/bookingID : 200, получить букинг")
    void getBookingTest() {

        bookingID = new ApiBookingEndpoint().getBookingIDs().get(0).getBookingid();

        BookingDto bookingDto = new ApiBookingEndpoint().getBookingById(bookingID);

        SoftAssertions softAssertions = new SoftAssertions();

        softAssertions.assertThat(bookingDto.getFirstname()).isNotEmpty();
        softAssertions.assertThat(bookingDto.getLastname()).isNotEmpty();

        softAssertions.assertAll();
    }

    @Test
    @DisplayName("POST ../booking : 200, создать букинг")
    void createBookingTest() {
        Faker faker = new Faker();

        BookingDto bookingDto = new DtosGenerator().generateDto();

        assertThat(new ApiBookingEndpoint().createBooking(bookingDto).getBooking()).isEqualTo(bookingDto);

    }

    @Nested
    @DisplayName("../booking зависимые от токена")
    @ExtendWith(nexign_autotests.hw5.api.extentsion.ApiTestExtension.class)
    public class ApiRegisterTest {
        String userFirstName;
        int bookingid;
        String token;

        @BeforeEach
        void setParams() {
            Faker faker = new Faker();
            String userFirstName = faker.name().firstName();

            token = new ApiAuthEndpoint().getToken(AuthDto.builder()
                    .username("admin")
                    .password("password123")
                    .build());

            bookingid = new ApiBookingEndpoint().createBooking(new DtosGenerator().generateDto())
                    .getBookingid();

        }

        @Test
        @DisplayName("PUT ../booking : 200, изменить букинг")
        void updateBookingTest() {

            BookingDto expectedBooking = new DtosGenerator().generateDto();

            assertThat(new ApiBookingEndpoint().updateBooking(expectedBooking, bookingid, token)).isEqualTo(expectedBooking);
        }

        @Test
        @DisplayName("PUT ../booking : 403, изменить букинг без токена")
        void unsuccessfulUpdateBookingWithoutAuthTest() {

            BookingDto expectedBooking = new DtosGenerator().generateDto();

            given()
                    .cookie("token", "")
                    .body(expectedBooking)
                    .put(new ApiBookingEndpoint().getEndpoint()+"/"+bookingid)
                    .then()
                    .statusCode(403);
        }

        @Test
        @DisplayName("PUT ../booking : 405, изменить несуществующий букинг")
        void unsuccessfulUpdateBookingWrongIdTest() {

            BookingDto expectedBooking = new DtosGenerator().generateDto();

            given()
                    .cookie("token", token)
                    .body(expectedBooking)
                    .put(new ApiBookingEndpoint().getEndpoint()+"/"+123545677)
                    .then()
                    .statusCode(405);
        }

        @Test
        @DisplayName("PATCH ../booking : 200, частично изменить букинг")
        void partialUpdateBookingTest() {

            BookingDto expectedBooking = new DtosGenerator().generateDto();

            assertThat(new ApiBookingEndpoint().partialUpdateBooking(expectedBooking, bookingid, token)).isEqualTo(expectedBooking);

        }


        @Test
        @DisplayName("delete ../booking : 201, удалить букинг")
        void deleteBookingTest() {

            int beforeBookingSize = new ApiBookingEndpoint().getBookingIDs().size();

            new ApiBookingEndpoint().deleteBooking(bookingid, token, 201);
            //из-за открытости не проверить количество
//            assertThat(new ApiBookingEndpoint().getBookingIDs().size()).isEqualTo(beforeBookingSize-1);

        }

        @Test
        @DisplayName("delete ../booking : 403, удалить букинг без токена")
        void unsuccessfulDeleteWithoutTokenBookingTest() {

            new ApiBookingEndpoint().deleteBooking(bookingid, "",403);
            //из-за открытости не проверить количество
//            assertThat(new ApiBookingEndpoint().getBookingIDs().size()).isEqualTo(beforeBookingSize-1);

        }

        @Test
        @DisplayName("delete ../booking : 403, удалить букинг без токена")
        void unsuccessfulDeleteWrongIdBookingTest() {

            new ApiBookingEndpoint().deleteBooking(123441235, token,405);

        }

    }

    @Test
    @DisplayName("GET ../ping : 200, пинг сервера")
    void healthCheckTest() {
        given()
                .get("/ping")
                .then()
                .statusCode(201);
    }
}
