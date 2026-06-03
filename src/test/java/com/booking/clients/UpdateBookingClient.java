package com.booking.clients;

import com.booking.pojo.BookingDates;
import com.booking.pojo.BookingRequest;
import com.booking.support.ApiConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.time.LocalDate;
import java.util.List;
import java.util.HashMap;
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

    public Map<String, Object> buildValidUpdateBookingRequest() {
        BookingRequest pojo = buildValidUpdateBookingRequestPojo();

        Map<String, Object> bookingDates = new HashMap<>();
        if (pojo.getBookingdates() != null) {
            bookingDates.put("checkin", pojo.getBookingdates().getCheckin());
            bookingDates.put("checkout", pojo.getBookingdates().getCheckout());
        }

        Map<String, Object> body = new HashMap<>();
        body.put("roomid", pojo.getRoomid());
        body.put("firstname", pojo.getFirstname());
        body.put("lastname", pojo.getLastname());
        body.put("depositpaid", pojo.getDepositpaid());
        body.put("bookingdates", bookingDates);
        body.put("email", pojo.getEmail());
        body.put("phone", pojo.getPhone());
        return body;
    }

    public Map<String, Object> buildUpdateBookingRequestWithField(String field, String value) {
        Map<String, Object> body = new HashMap<>(buildValidUpdateBookingRequest());

        if (field == null || field.isBlank()) {
            throw new IllegalArgumentException("field should not be blank");
        }

        if ("firstname".equalsIgnoreCase(field) || "lastname".equalsIgnoreCase(field)
                || "email".equalsIgnoreCase(field) || "phone".equalsIgnoreCase(field)) {
            body.put(field.toLowerCase(), value);
            return body;
        }

        if ("roomid".equalsIgnoreCase(field)) {
            if (value == null || value.isBlank()) {
                body.put("roomid", value);
                return body;
            }
            try {
                body.put("roomid", Integer.parseInt(value));
            } catch (Exception ignored) {
                body.put("roomid", value);
            }
            return body;
        }

        if ("depositpaid".equalsIgnoreCase(field)) {
            if (value == null || value.isBlank()) {
                body.put("depositpaid", value);
                return body;
            }
            if ("true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value)) {
                body.put("depositpaid", Boolean.parseBoolean(value));
                return body;
            }
            body.put("depositpaid", value);
            return body;
        }

        if (field.toLowerCase().startsWith("bookingdates.")) {
            Object bookingDatesObj = body.get("bookingdates");
            if (!(bookingDatesObj instanceof Map)) {
                bookingDatesObj = new HashMap<String, Object>();
            }

            Map<String, Object> bookingDates = new HashMap<>((Map<String, Object>) bookingDatesObj);
            String key = field.substring("bookingdates.".length()).toLowerCase();
            bookingDates.put(key, value);
            body.put("bookingdates", bookingDates);
            return body;
        }

        throw new IllegalArgumentException("Unsupported field: " + field);
    }

    public Map<String, Object> buildUpdateBookingRequestWithDateCondition(String dateCondition) {
        Map<String, Object> body = new HashMap<>(buildValidUpdateBookingRequest());
        Object bookingDatesObj = body.get("bookingdates");
        if (!(bookingDatesObj instanceof Map)) {
            return body;
        }

        Map<String, Object> bookingDates = new HashMap<>((Map<String, Object>) bookingDatesObj);

        if (dateCondition == null || dateCondition.isBlank()) {
            return body;
        }

        if (dateCondition.equalsIgnoreCase("checkout before checkin")) {
            bookingDates.put("checkin", "2026-12-10");
            bookingDates.put("checkout", "2026-12-08");
        } else if (dateCondition.equalsIgnoreCase("checkin and checkout same day")) {
            Object checkin = bookingDates.get("checkin");
            if (checkin != null) {
                bookingDates.put("checkout", checkin);
            }
        } else if (dateCondition.equalsIgnoreCase("missing checkin")) {
            bookingDates.remove("checkin");
        } else if (dateCondition.equalsIgnoreCase("missing checkout")) {
            bookingDates.remove("checkout");
        } else {
            throw new IllegalArgumentException("Unknown date condition: " + dateCondition);
        }

        body.put("bookingdates", bookingDates);
        return body;
    }

    public Map<String, Object> buildUpdateBookingRequestMissingField(String field) {
        Map<String, Object> body = new HashMap<>(buildValidUpdateBookingRequest());

        if (field == null || field.isBlank()) {
            throw new IllegalArgumentException("field should not be blank");
        }

        body.remove(field.toLowerCase());
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

    public Response updateBooking(String token, String bookingId, Object updateBody) {
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

    public void assertUnauthorized(Response response) {
        assertNotNull(response, "No response available for booking update");

        int statusCode = response.getStatusCode();
        assertEquals(403, statusCode,
                "Expected 403 for unauthorized booking update but got " + statusCode + ". Body: " + response.asString());
    }

    public void assertUpdateRejected(Response response) {
        assertNotNull(response, "No response available for booking update");

        int statusCode = response.getStatusCode();
        assertTrue(statusCode >= 400 && statusCode < 500,
                "Expected 4xx for invalid booking update but got " + statusCode + ". Body: " + response.asString());
    }

    public void assertNotFound(Response response) {
        assertNotNull(response, "No response available for booking update");

        int statusCode = response.getStatusCode();
        assertEquals(404, statusCode,
                "Expected 404 for not found booking update but got " + statusCode + ". Body: " + response.asString());
    }
}
