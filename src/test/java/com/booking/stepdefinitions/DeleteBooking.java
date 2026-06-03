package com.booking.stepdefinitions;

import com.booking.clients.DeleteBookingClient;
import com.booking.support.TestContext;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DeleteBooking {

    private Response response;
    private Integer createdBookingId;
    private final DeleteBookingClient deleteBookingClient = new DeleteBookingClient();

    private int getCreatedBookingId() {
        if (createdBookingId != null) {
            return createdBookingId;
        }

        Response createResponse = TestContext.getLastResponse();
        assertNotNull(createResponse, "No previous response found. Ensure a booking was created before deleting.");

        Integer bookingId = null;
        try {
            bookingId = createResponse.jsonPath().getInt("bookingid");
        } catch (Exception ignored) {
        }
        assertNotNull(bookingId, "bookingid should not be null in the create booking response. Body: " + createResponse.asString());
        createdBookingId = bookingId;
        return bookingId;
    }

    @When("I delete the created booking")
    public void iDeleteTheCreatedBooking() {
        int bookingId = getCreatedBookingId();
        String token = TestContext.getLastToken();
        response = deleteBookingClient.deleteBooking(token, bookingId);
        TestContext.setLastResponse(response);
    }

    @When("I delete booking id {string}")
    public void iDeleteBookingId(String bookingId) {
        String token = TestContext.getLastToken();
        response = deleteBookingClient.deleteBooking(token, bookingId);
        TestContext.setLastResponse(response);
    }

    @When("I delete the created booking using token {string}")
    public void iDeleteTheCreatedBookingUsingToken(String token) {
        int bookingId = getCreatedBookingId();
        response = deleteBookingClient.deleteBooking(token, bookingId);
        TestContext.setLastResponse(response);
    }

    @Then("the delete booking request should be rejected with not found error")
    public void theDeleteBookingRequestShouldBeRejectedWithNotFoundError() {
        deleteBookingClient.assertNotFound(response);
        TestContext.setLastResponse(response);
    }

    @Then("the delete booking request should be rejected with unauthorized error")
    public void theDeleteBookingRequestShouldBeRejectedWithUnauthorizedError() {
        deleteBookingClient.assertUnauthorized(response);
        TestContext.setLastResponse(response);
    }

    @When("I delete the created booking again")
    public void iDeleteTheCreatedBookingAgain() {
        int bookingId = getCreatedBookingId();
        String token = TestContext.getLastToken();
        response = deleteBookingClient.deleteBooking(token, bookingId);
        TestContext.setLastResponse(response);
    }

    @When("I delete the created booking without authentication")
    public void iDeleteTheCreatedBookingWithoutAuthentication() {
        int bookingId = getCreatedBookingId();
        response = deleteBookingClient.deleteBooking(null, bookingId);
        TestContext.setLastResponse(response);
    }

    @Then("the booking should be deleted successfully")
    public void theBookingShouldBeDeletedSuccessfully() {
        deleteBookingClient.assertBookingDeleted(response);
        TestContext.setLastResponse(response);
    }
}
