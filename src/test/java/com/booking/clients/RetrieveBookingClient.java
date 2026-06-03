package com.booking.clients;

import com.booking.support.ApiConfig;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

public class RetrieveBookingClient {

    public Response getBookingById(String token, int bookingId) {
        if (token == null || token.isBlank()) {
            return given().log().all()
                    .when()
                    .get(ApiConfig.BASE_URL + "/booking/" + bookingId)
                    .then()
                    .extract()
                    .response();
        }

        return given().log().all()
                .header("Cookie", "token=" + token)
                .when()
                .get(ApiConfig.BASE_URL + "/booking/" + bookingId)
                .then()
                .extract()
                .response();
    }

    public Response getBookingById(String token, String bookingId) {
        if (token == null || token.isBlank()) {
            return given().log().all()
                    .when()
                    .get(ApiConfig.BASE_URL + "/booking/" + bookingId)
                    .then()
                    .extract()
                    .response();
        }

        return given().log().all()
                .header("Cookie", "token=" + token)
                .when()
                .get(ApiConfig.BASE_URL + "/booking/" + bookingId)
                .then()
                .extract()
                .response();
    }

    public void assertBookingDetailsReturned(Response response) {
        assertNotNull(response, "No response available for booking retrieval");
        int statusCode = response.getStatusCode();
        assertEquals(200, statusCode,
                "Expected 200 for booking retrieval but got " + statusCode + ". Body: " + response.asString());

        Integer roomId = response.jsonPath().getInt("roomid");
        assertNotNull(roomId, "roomid should not be null. Body: " + response.asString());
    }

    public void assertNotFound(Response response) {
        assertNotNull(response, "No response available for booking retrieval");
        int statusCode = response.getStatusCode();
        assertEquals(404, statusCode,
                "Expected 404 for booking retrieval not found but got " + statusCode + ". Body: " + response.asString());
    }

    public void assertUnauthorized(Response response) {
        assertNotNull(response, "No response available for booking retrieval");
        int statusCode = response.getStatusCode();
        assertEquals(403, statusCode,
                "Expected 403 for unauthorized booking retrieval but got " + statusCode + ". Body: " + response.asString());

        String error = null;
        try {
            error = response.jsonPath().getString("error");
        } catch (Exception ignored) {
        }
        assertTrue(error == null || error.contains("Unauthorized") || response.asString().contains("Unauthorized"),
                "Expected response to indicate Unauthorized. Body: " + response.asString());
    }
}
