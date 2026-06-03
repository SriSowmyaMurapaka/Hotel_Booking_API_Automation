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

    @And("the response body should contain the booking details")
    public void theResponseBodyShouldContainTheBookingDetails() {
        CreateBookingClient.assertBookingCreated(response);
        TestContext.setLastResponse(response);
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