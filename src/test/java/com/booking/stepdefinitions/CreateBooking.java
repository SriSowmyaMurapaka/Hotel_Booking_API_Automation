package com.booking.stepdefinitions;

import com.booking.clients.CreateBookingClient;
import com.booking.pojo.BookingRequest;
import com.booking.support.TestContext;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class CreateBooking {

    private Response response;
    private final CreateBookingClient CreateBookingClient = new CreateBookingClient();

    @When("I create a new booking with valid booking details")
    public void iCreateANewBookingWithValidBookingDetails() {
        String token = TestContext.getLastToken();
        BookingRequest body = CreateBookingClient.buildValidBookingRequestPojo();
        response = CreateBookingClient.createBooking(token, body);
        TestContext.setLastResponse(response);
    }

    @Then("the booking should be created successfully")
    public void theBookingShouldBeCreatedSuccessfully() {
        CreateBookingClient.assertBookingCreated(response);
        TestContext.setLastResponse(response);
    }

    @When("I create a booking with {string} as {string}")
    public void iCreateABookingWithAs(String field, String value) {
        String token = TestContext.getLastToken();
        response = CreateBookingClient.createBooking(token, CreateBookingClient.buildBookingRequestWithField(field, value));
        TestContext.setLastResponse(response);
    }

    @When("I create a booking with {string}")
    public void iCreateABookingWith(String dateCondition) {
        String token = TestContext.getLastToken();
        response = CreateBookingClient.createBooking(token, CreateBookingClient.buildBookingRequestWithDateCondition(dateCondition));
        TestContext.setLastResponse(response);
    }

    @When("I create a booking with an invalid room id")
    public void iCreateABookingWithAnInvalidRoomId() {
        String token = TestContext.getLastToken();
        response = CreateBookingClient.createBooking(token, CreateBookingClient.buildBookingRequestWithInvalidRoomId());
        TestContext.setLastResponse(response);
    }

    @And("the response body should contain the booking details")
    public void theResponseBodyShouldContainTheBookingDetails() {
        CreateBookingClient.assertBookingCreated(response);
        TestContext.setLastResponse(response);
    }

    @Then("the booking request should be rejected")
    public void theBookingRequestShouldBeRejected() {
        CreateBookingClient.assertBookingRejected(response);
        TestContext.setLastResponse(response);
    }

    @And("the error message should contain {string}")
    public void theErrorMessageShouldContain(String expectedSubstring) {
        Response lastResponse = TestContext.getLastResponse();
        if (lastResponse == null) {
            lastResponse = response;
        }
        CreateBookingClient.assertErrorMessageContains(lastResponse, expectedSubstring);
        TestContext.setLastResponse(lastResponse);
    }

    @And("the response matches with json schema {string}")
    public void theResponseMatchesWithJsonSchema(String schemaFile) {
        Response lastResponse = TestContext.getLastResponse();
        if (lastResponse == null) {
            lastResponse = response;
        }
        lastResponse.then().assertThat().body(matchesJsonSchemaInClasspath(schemaFile));
        TestContext.setLastResponse(lastResponse);
    }
}