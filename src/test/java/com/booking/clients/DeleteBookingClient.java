package com.booking.clients;

import com.booking.support.ApiConfig;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DeleteBookingClient {

    public Response deleteBooking(String token, int bookingId) {
        if (token == null || token.isBlank()) {
            return given().log().all()
                    .when()
                    .delete(ApiConfig.BASE_URL + "/booking/" + bookingId)
                    .then()
                    .extract()
                    .response();
        }

        return given().log().all()
                .header("Cookie", "token=" + token)
                .when()
                .delete(ApiConfig.BASE_URL + "/booking/" + bookingId)
                .then()
                .extract()
                .response();
    }

    public void assertBookingDeleted(Response response) {
        assertNotNull(response, "No response available for booking delete");

        int statusCode = response.getStatusCode();
        assertEquals(202, statusCode,
                "Expected 202 for booking delete but got " + statusCode + ". Body: " + response.asString());
    }
}
