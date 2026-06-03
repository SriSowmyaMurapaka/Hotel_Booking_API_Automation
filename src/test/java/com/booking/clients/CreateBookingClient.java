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

    public Map<String, Object> buildBookingRequestWithField(String field, String value) {
        Map<String, Object> body = new HashMap<>(buildValidBookingRequest());

        if (field == null || field.isBlank()) {
            throw new IllegalArgumentException("field should not be blank");
        }

        if (field.toLowerCase().startsWith("bookingdates.")) {
            Object bookingDatesObj = body.get("bookingdates");
            Map<String, Object> bookingDates;
            if (bookingDatesObj instanceof Map) {
                bookingDates = new HashMap<>((Map<String, Object>) bookingDatesObj);
            } else {
                bookingDates = new HashMap<>();
            }

            String nestedField = field.substring("bookingdates.".length()).toLowerCase();
            if (!"checkin".equals(nestedField) && !"checkout".equals(nestedField)) {
                throw new IllegalArgumentException("Unsupported bookingdates field: " + nestedField);
            }

            if (value == null || value.isBlank()) {
                bookingDates.put(nestedField, null);
            } else {
                bookingDates.put(nestedField, value);
            }
            body.put("bookingdates", bookingDates);
            return body;
        }

        if ("roomid".equalsIgnoreCase(field)) {
            if (value == null || value.isBlank()) {
                body.put("roomid", null);
                return body;
            }

            try {
                body.put("roomid", Integer.parseInt(value.trim()));
            } catch (NumberFormatException e) {
                body.put("roomid", value);
            }
            return body;
        }

        if ("firstname".equalsIgnoreCase(field) || "lastname".equalsIgnoreCase(field)
                || "email".equalsIgnoreCase(field) || "phone".equalsIgnoreCase(field)) {
            body.put(field.toLowerCase(), value);
            return body;
        }

        throw new IllegalArgumentException("Unsupported field: " + field);
    }

    public Map<String, Object> buildBookingRequestWithDateCondition(String dateCondition) {
        Map<String, Object> body = new HashMap<>(buildValidBookingRequest());
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

    public Map<String, Object> buildBookingRequestWithInvalidRoomId() {
        Map<String, Object> body = new HashMap<>(buildValidBookingRequest());
        body.put("roomid", 0);
        return body;
    }

    public Map<String, Object> buildValidBookingRequest() {
        BookingRequest pojo = buildValidBookingRequestPojo();

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

    public void assertBookingRejected(Response response) {
        assertNotNull(response, "No response available for booking creation");

        int statusCode = response.getStatusCode();
        assertTrue(statusCode >= 400 && statusCode < 500,
                "Expected 4xx for invalid booking creation but got " + statusCode + ". Body: " + response.asString());
    }

    public void assertErrorMessageContains(Response response, String expectedSubstring) {
        assertNotNull(response, "No response available");
        assertNotNull(expectedSubstring, "Expected error substring should not be null");

        String bodyString = response.asString();
        if ((bodyString == null || bodyString.isBlank()) && response.getStatusCode() >= 400) {
            fail("Expected response body to contain '" + expectedSubstring + "' but body was empty. Status: " + response.getStatusCode());
            return;
        }

        List<String> errors = null;
        try {
            errors = response.jsonPath().getList("errors", String.class);
        } catch (Exception ignored) {
        }

        if (errors != null && !errors.isEmpty()) {
            String joined = String.join(" ", errors);
            assertTrue(joined.contains(expectedSubstring),
                    "Expected error messages to contain '" + expectedSubstring + "' but was: " + joined + ". Body: " + response.asString());
            return;
        }

        List<String> fieldErrors = null;
        try {
            fieldErrors = response.jsonPath().getList("fieldErrors", String.class);
        } catch (Exception ignored) {
        }

        if (fieldErrors != null && !fieldErrors.isEmpty()) {
            String joined = String.join(" ", fieldErrors);
            assertTrue(joined.contains(expectedSubstring),
                    "Expected fieldErrors to contain '" + expectedSubstring + "' but was: " + joined + ". Body: " + response.asString());
            return;
        }

        String error = null;
        try {
            error = response.jsonPath().getString("error");
        } catch (Exception ignored) {
        }

        if (error != null) {
            assertTrue(error.contains(expectedSubstring),
                    "Expected error to contain '" + expectedSubstring + "' but was: " + error + ". Body: " + response.asString());
            return;
        }

        assertTrue(bodyString.contains(expectedSubstring),
                "Expected response body to contain '" + expectedSubstring + "' but was: " + bodyString);
    }
}
