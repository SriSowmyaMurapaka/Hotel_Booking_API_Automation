package com.booking.clients;

import com.booking.pojo.BookingDates;
import com.booking.pojo.BookingRequest;
import com.booking.support.ApiConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

public class UpdateBookingClient {

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

    public BookingRequest buildValidUpdateBookingRequestPojo() {
        LocalDate checkin = randomFutureCheckinDate();
        LocalDate checkout = randomCheckoutDate(checkin);

        String unique = unique3Digits();
        int roomId = randomRoomId();

        BookingRequest body = new BookingRequest();
        body.setRoomid(roomId);
        body.setFirstname("UpdJ" + unique);
        body.setLastname("UpdD" + unique);
        body.setDepositpaid(true);
        body.setBookingdates(new BookingDates(checkin.toString(), checkout.toString()));
        body.setEmail("updated.john.doe+" + unique + "@example.com");
        body.setPhone("12345678901");
        return body;
    }

    public Response updateBooking(String token, int bookingId, Object updateBody) {
        if (token == null || token.isBlank()) {
            return given().log().all()
                    .contentType(ContentType.JSON)
                    .body(updateBody)
                    .when()
                    .put(ApiConfig.BASE_URL + "/booking/" + bookingId)
                    .then()
                    .extract()
                    .response();
        }

        return given().log().all()
                .contentType(ContentType.JSON)
                .header("Cookie", "token=" + token)
                .body(updateBody)
                .when()
                .put(ApiConfig.BASE_URL + "/booking/" + bookingId)
                .then()
                .extract()
                .response();
    }

    public void assertBookingUpdated(Response response) {
        assertNotNull(response, "No response available for booking update");

        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode,
                "Expected 200 for booking update but got " + statusCode + ". Body: " + response.asString());

        Integer roomId = response.jsonPath().get("roomid");
        if (roomId == null) {
            roomId = response.jsonPath().get("booking.roomid");
        }
        assertNotNull(roomId,
                "roomid should not be null (tried roomid and booking.roomid). Body: " + response.asString());

        String firstname = response.jsonPath().getString("firstname");
        if (firstname == null) {
            firstname = response.jsonPath().getString("booking.firstname");
        }
        assertNotNull(firstname,
                "firstname should not be null (tried firstname and booking.firstname). Body: " + response.asString());
    }
}
