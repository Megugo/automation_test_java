package nexign_autotests.hw5.api_additional.endpoints;

import nexign_autotests.hw5.api.dto.PhoneDto;
import nexign_autotests.hw5.api_additional.dto.AuthDto;
import nexign_autotests.hw5.api_additional.dto.BookingDto;
import nexign_autotests.hw5.api_additional.dto.BookingIdDto;
import nexign_autotests.hw5.api_additional.dto.BookingListDto;

import java.util.List;

import static io.restassured.RestAssured.given;

@Endpoint("/booking")
public class ApiBookingEndpoint extends BaseEndpoint{
    public List<BookingIdDto> getBookingIDs(){
        return List.of(given()
                .get(getEndpoint())
                .then()
                .statusCode(200)
                .extract()
                .as(BookingIdDto[].class));
    }


    public BookingDto getBookingById(int id){
        return given()
                .get(getEndpoint()+"/"+id)
                .then()
                .statusCode(200)
                .extract()
                .as(BookingDto.class);
    }

    public BookingListDto createBooking(BookingDto bookingDto){
        return given()
                .body(bookingDto)
                .post(getEndpoint())
                .then()
                .statusCode(200)
                .extract()
                .as(BookingListDto.class);
    }

    public BookingDto updateBooking(BookingDto bookingDto, int id, String token){
        return given()
                .cookie("token", token)
                .body(bookingDto)
                .put(getEndpoint()+"/"+id)
                .then()
                .statusCode(200)
                .extract()
                .as(BookingDto.class);
    }

    public BookingDto partialUpdateBooking(BookingDto bookingDto, int id, String token){
        return given()
                .cookie("token", token)
                .body(bookingDto)
                .patch(getEndpoint()+"/"+id)
                .then()
                .statusCode(200)
                .extract()
                .as(BookingDto.class);
    }

    public void deleteBooking(int id, String token, int expectedStatusCode){
        given()
                .cookie("token", token)
                .delete(getEndpoint()+"/"+id)
                .then()
                .statusCode(expectedStatusCode);
    }







}
