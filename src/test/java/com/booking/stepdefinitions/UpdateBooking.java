package com.booking.stepdefinitions;

import com.booking.clients.UpdateBookingClient;
import com.booking.pojo.BookingRequest;
import com.booking.support.TestContext;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import static org.junit.jupiter.api.Assertions.*;

public class UpdateBooking {

    private Response response;
    private final UpdateBookingClient updateBookingClient = new UpdateBookingClient();

    @When("I update the created booking with valid booking details")
    public void iUpdateTheCreatedBookingWithValidBookingDetails() {
        Response createResponse = TestContext.getLastResponse();
        assertNotNull(createResponse, "No previous response found. Ensure a booking was created before updating.");

        Integer bookingId = null;
        try {
            bookingId = createResponse.jsonPath().getInt("bookingid");
        } catch (Exception ignored) {
        }
        assertNotNull(bookingId, "bookingid should not be null in the create booking response. Body: " + createResponse.asString());

        String token = TestContext.getLastToken();
        BookingRequest body = updateBookingClient.buildValidUpdateBookingRequestPojo();
        response = updateBookingClient.updateBooking(token, bookingId, body);
        TestContext.setLastResponse(response);
    }

    @When("I update the created booking using token {string}")
    public void iUpdateTheCreatedBookingUsingToken(String token) {
        Response createResponse = TestContext.getLastResponse();
        assertNotNull(createResponse, "No previous response found. Ensure a booking was created before updating.");

        Integer bookingId = null;
        try {
            bookingId = createResponse.jsonPath().getInt("bookingid");
        } catch (Exception ignored) {
        }
        assertNotNull(bookingId, "bookingid should not be null in the create booking response. Body: " + createResponse.asString());

        BookingRequest body = updateBookingClient.buildValidUpdateBookingRequestPojo();
        response = updateBookingClient.updateBooking(token, bookingId, body);
        TestContext.setLastResponse(response);
    }

    @When("I update the created booking without authentication")
    public void iUpdateTheCreatedBookingWithoutAuthentication() {
        Response createResponse = TestContext.getLastResponse();
        assertNotNull(createResponse, "No previous response found. Ensure a booking was created before updating.");

        Integer bookingId = null;
        try {
            bookingId = createResponse.jsonPath().getInt("bookingid");
        } catch (Exception ignored) {
        }
        assertNotNull(bookingId, "bookingid should not be null in the create booking response. Body: " + createResponse.asString());

        BookingRequest body = updateBookingClient.buildValidUpdateBookingRequestPojo();
        response = updateBookingClient.updateBooking(null, bookingId, body);
        TestContext.setLastResponse(response);
    }

    @Then("the booking should be updated successfully")
    public void theBookingShouldBeUpdatedSuccessfully() {
        updateBookingClient.assertBookingUpdated(response);
        TestContext.setLastResponse(response);
    }

    @Then("the update booking request should be rejected with unauthorized error")
    public void theUpdateBookingRequestShouldBeRejectedWithUnauthorizedError() {
        updateBookingClient.assertUnauthorized(response);
        TestContext.setLastResponse(response);
    }

    @When("I update the created booking with {string} as {string}")
    public void iUpdateTheCreatedBookingWithAs(String field, String value) {
        Response createResponse = TestContext.getLastResponse();
        assertNotNull(createResponse, "No previous response found. Ensure a booking was created before updating.");

        Integer bookingId = null;
        try {
            bookingId = createResponse.jsonPath().getInt("bookingid");
        } catch (Exception ignored) {
        }
        assertNotNull(bookingId, "bookingid should not be null in the create booking response. Body: " + createResponse.asString());

        String token = TestContext.getLastToken();
        response = updateBookingClient.updateBooking(token, bookingId, updateBookingClient.buildUpdateBookingRequestWithField(field, value));
        TestContext.setLastResponse(response);
    }

    @When("I update the created booking with {string}")
    public void iUpdateTheCreatedBookingWith(String dateCondition) {
        Response createResponse = TestContext.getLastResponse();
        assertNotNull(createResponse, "No previous response found. Ensure a booking was created before updating.");

        Integer bookingId = null;
        try {
            bookingId = createResponse.jsonPath().getInt("bookingid");
        } catch (Exception ignored) {
        }
        assertNotNull(bookingId, "bookingid should not be null in the create booking response. Body: " + createResponse.asString());

        String token = TestContext.getLastToken();
        response = updateBookingClient.updateBooking(token, bookingId, updateBookingClient.buildUpdateBookingRequestWithDateCondition(dateCondition));
        TestContext.setLastResponse(response);
    }

    @When("I update the created booking with missing field {string}")
    public void iUpdateTheCreatedBookingWithMissingField(String field) {
        Response createResponse = TestContext.getLastResponse();
        assertNotNull(createResponse, "No previous response found. Ensure a booking was created before updating.");

        Integer bookingId = null;
        try {
            bookingId = createResponse.jsonPath().getInt("bookingid");
        } catch (Exception ignored) {
        }
        assertNotNull(bookingId, "bookingid should not be null in the create booking response. Body: " + createResponse.asString());

        String token = TestContext.getLastToken();
        response = updateBookingClient.updateBooking(token, bookingId, updateBookingClient.buildUpdateBookingRequestMissingField(field));
        TestContext.setLastResponse(response);
    }

    @Then("the update booking request should be rejected")
    public void theUpdateBookingRequestShouldBeRejected() {
        updateBookingClient.assertUpdateRejected(response);
        TestContext.setLastResponse(response);
    }

    @Then("the update booking request should return status {int}")
    public void theUpdateBookingRequestShouldReturnStatus(int expectedStatus) {
        assertNotNull(response, "No response available for booking update");
        assertEquals(expectedStatus, response.getStatusCode(),
                "Expected " + expectedStatus + " for booking update but got " + response.getStatusCode() + ". Body: " + response.asString());
        TestContext.setLastResponse(response);
    }

    @When("I update booking id {int} with valid booking details")
    public void iUpdateBookingIdWithValidBookingDetails(int bookingId) {
        String token = TestContext.getLastToken();
        BookingRequest body = updateBookingClient.buildValidUpdateBookingRequestPojo();
        response = updateBookingClient.updateBooking(token, bookingId, body);
        TestContext.setLastResponse(response);
    }

    @When("I update booking id {string} with valid booking details")
    public void iUpdateBookingIdWithValidBookingDetails(String bookingId) {
        String token = TestContext.getLastToken();
        BookingRequest body = updateBookingClient.buildValidUpdateBookingRequestPojo();
        response = updateBookingClient.updateBooking(token, bookingId, body);
        TestContext.setLastResponse(response);
    }

    @Then("the update booking request should be rejected with not found error")
    public void theUpdateBookingRequestShouldBeRejectedWithNotFoundError() {
        updateBookingClient.assertNotFound(response);
        TestContext.setLastResponse(response);
    }
}
