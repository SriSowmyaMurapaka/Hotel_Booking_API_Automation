package com.booking.stepdefinitions;

import com.booking.clients.UpdateBookingClient;
import com.booking.pojo.BookingRequest;
import com.booking.support.TestContext;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import static org.junit.jupiter.api.Assertions.assertNotNull;

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

    @Then("the booking should be updated successfully")
    public void theBookingShouldBeUpdatedSuccessfully() {
        updateBookingClient.assertBookingUpdated(response);
        TestContext.setLastResponse(response);
    }


}
