package com.booking.stepdefinitions;

import com.booking.clients.HealthCheckClient;
import com.booking.support.TestContext;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

public class HealthCheck {

    private final HealthCheckClient HealthCheckClient = new HealthCheckClient();
    private Response response;

    @Given("the booking website API is available")
    public void theBookingWebsiteApiIsAvailable() {
    }

    @When("I request the health check")
    public void iRequestTheHealthCheck() {
        response = HealthCheckClient.getHealthCheck();
        TestContext.setLastResponse(response);
    }

    @Then("the health check response code should be {int}")
    public void theHealthCheckResponseCodeShouldBe(int statusCode) {
        response.then().statusCode(statusCode);
        TestContext.setLastResponse(response);
    }

    @And("the health status should be {string}")
    public void theHealthStatusShouldBe(String expectedStatus) {
        String actualStatus = response.jsonPath().getString("status");
        org.junit.jupiter.api.Assertions.assertEquals(expectedStatus, actualStatus, "Unexpected health status");
        TestContext.setLastResponse(response);
    }

    @When("I request the health check using endpoint {string}")
    public void iRequestTheHealthCheckUsingEndpoint(String endpointPath) {
        response = HealthCheckClient.getHealthCheck(endpointPath);
        TestContext.setLastResponse(response);
    }

    @When("I request the health check using POST and endpoint {string}")
    public void iRequestTheHealthCheckUsingPOSTAndEndpoint(String endpointPath) {
        response = HealthCheckClient.postHealthCheck(endpointPath);
        TestContext.setLastResponse(response);
    }
}
