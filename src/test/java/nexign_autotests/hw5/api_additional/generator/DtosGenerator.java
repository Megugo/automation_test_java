package nexign_autotests.hw5.api_additional.generator;

import com.github.javafaker.Faker;
import nexign_autotests.hw5.api_additional.dto.BookingDto;
import nexign_autotests.hw5.api_additional.dto.Bookingdates;

public class DtosGenerator {
    Faker faker = new Faker();
    public BookingDto generateDto(){

        return BookingDto.builder()
                .firstname(faker.name().firstName())
                .lastname(faker.name().lastName())
                .totalprice(100)
                .depositpaid(true)
                .bookingdates(Bookingdates.builder()
                        .checkin("2018-01-01")
                        .checkout("2019-01-01")
                        .build())
                .additionalneeds(faker.lorem().word())
                .build();
    }
}
