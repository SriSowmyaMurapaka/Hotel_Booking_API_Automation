package com.booking.stepdefinitions;

import com.booking.clients.RetrieveBookingClient;
import com.booking.support.TestContext;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RetrieveBooking {

    private Response response;
    private final RetrieveBookingClient retrieveBookingClient = new RetrieveBookingClient();

    @When("I retrieve the booking details for the created booking id")
    public void iRetrieveTheBookingDetailsForTheCreatedBookingId() {
        Response createResponse = TestContext.getLastResponse();
        assertNotNull(createResponse, "No previous response found. Ensure a booking was created before retrieving details.");

        Integer bookingId = null;
        try {
            bookingId = createResponse.jsonPath().getInt("bookingid");
        } catch (Exception ignored) {
        }
        assertNotNull(bookingId, "bookingid should not be null in the create booking response. Body: " + createResponse.asString());

        String token = TestContext.getLastToken();
        response = retrieveBookingClient.getBookingById(token, bookingId);
        TestContext.setLastResponse(response);
    }

    @Then("the booking details should be returned successfully")
    public void theBookingDetailsShouldBeReturnedSuccessfully() {
        retrieveBookingClient.assertBookingDetailsReturned(response);
        TestContext.setLastResponse(response);
    }

    @When("I retrieve the booking details for booking id {int} without authentication")
    public void iRetrieveTheBookingDetailsForBookingIdWithoutAuthentication(int bookingId) {
        response = retrieveBookingClient.getBookingById(null, bookingId);
        TestContext.setLastResponse(response);
    }

    @Then("the request should be rejected with unauthorized error")
    public void theRequestShouldBeRejectedWithUnauthorizedError() {
        retrieveBookingClient.assertUnauthorized(response);
        TestContext.setLastResponse(response);
    }

    @When("I retrieve the booking details for booking id {string}")
    public void iRetrieveTheBookingDetailsForBookingId(String bookingId) {
        String token = TestContext.getLastToken();
        response = retrieveBookingClient.getBookingById(token, bookingId);
        TestContext.setLastResponse(response);
    }

    @Then("the booking details request should be rejected with not found")
    public void theBookingDetailsRequestShouldBeRejectedWithNotFound() {
        retrieveBookingClient.assertNotFound(response);
        TestContext.setLastResponse(response);
    }

    @When("I retrieve the booking details for the created booking id using token {string}")
    public void iRetrieveTheBookingDetailsForTheCreatedBookingIdUsingToken(String token) {
        Response createResponse = TestContext.getLastResponse();
        assertNotNull(createResponse, "No previous response found. Ensure a booking was created before retrieving details.");

        Integer bookingId = null;
        try {
            bookingId = createResponse.jsonPath().getInt("bookingid");
        } catch (Exception ignored) {
        }
        assertNotNull(bookingId, "bookingid should not be null in the create booking response. Body: " + createResponse.asString());

        response = retrieveBookingClient.getBookingById(token, bookingId);
        TestContext.setLastResponse(response);
    }
}
