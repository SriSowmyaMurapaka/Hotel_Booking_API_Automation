package com.booking.clients;

import com.booking.pojo.BookingDates;
import com.booking.pojo.BookingRequest;
import com.booking.support.ApiConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

public class CreateBookingClient {

    private static ThreadLocalRandom rnd() {
        return ThreadLocalRandom.current();
    }

    private static LocalDate randomFutureCheckinDate() {
        return LocalDate.now().plusDays(rnd().nextInt(3650, 7300));
    }

    private static LocalDate randomCheckoutDate(LocalDate checkin) {
        return checkin.plusDays(rnd().nextInt(1, 4));
    }

    private static String unique3Digits() {
        return "%03d".formatted(rnd().nextInt(1000));
    }

    private static int randomRoomId() {
        return rnd().nextInt(1, 4);
    }

    public BookingRequest buildValidBookingRequestPojo() {
        LocalDate checkin = randomFutureCheckinDate();
        LocalDate checkout = randomCheckoutDate(checkin);

        String unique = unique3Digits();
        int roomId = randomRoomId();

        BookingRequest body = new BookingRequest();
        body.setRoomid(roomId);
        body.setFirstname("John" + unique);
        body.setLastname("Doe" + unique);
        body.setDepositpaid(true);
        body.setBookingdates(new BookingDates(checkin.toString(), checkout.toString()));
        body.setEmail("john.doe+" + unique + "@example.com");
        body.setPhone("12345678901");
        return body;
    }

    public Response createBooking(String token, Object bookingBody) {
        if (token == null || token.isBlank()) {
            return given().log().all()
                    .contentType(ContentType.JSON)
                    .body(bookingBody)
                    .when()
                    .post(ApiConfig.BASE_URL + "/booking")
                    .then()
                    .extract()
                    .response();
        }

        return given().log().all()
                .contentType(ContentType.JSON)
                .header("Cookie", "token=" + token)
                .body(bookingBody)
                .when()
                .post(ApiConfig.BASE_URL + "/booking")
                .then()
                .extract()
                .response();
    }

    public void assertBookingCreated(Response response) {
        assertNotNull(response, "No response available for booking creation");

        int statusCode = response.getStatusCode();
        assertTrue(statusCode == 200 || statusCode == 201,
                "Expected 200 or 201 for booking creation but got " + statusCode + ". Body: " + response.asString());

        Integer bookingId = response.jsonPath().get("bookingid");
        assertNotNull(bookingId, "bookingid should not be null. Body: " + response.asString());

        Integer roomId = response.jsonPath().get("booking.roomid");
        if (roomId == null) {
            roomId = response.jsonPath().get("roomid");
        }
        assertNotNull(roomId,
                "roomid should not be null (tried booking.roomid and roomid). Body: " + response.asString());

        String firstname = response.jsonPath().getString("booking.firstname");
        if (firstname == null) {
            firstname = response.jsonPath().getString("firstname");
        }
        assertNotNull(firstname,
                "firstname should not be null (tried booking.firstname and firstname). Body: " + response.asString());
    }


}
