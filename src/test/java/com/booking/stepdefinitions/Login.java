package com.booking.stepdefinitions;

import com.booking.clients.LoginClient;
import com.booking.support.ApiConfig;
import com.booking.support.TestContext;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;

public class Login {
    private Response response;
    private final LoginClient LoginClient = new LoginClient();

    @When("I log in to the booking website with valid credentials")
    public void iLogInToTheBookingWebsiteWithValidCredentials() {
        response = LoginClient.login(ApiConfig.username(), ApiConfig.password());
        TestContext.setLastResponse(response);
    }

    @When("I log in to the booking website using username {string} and password {string}")
    public void iLogInToTheBookingWebsiteUsingUsernameAndPassword(String username, String password) {
        response = LoginClient.login(username, password);
        TestContext.setLastResponse(response);
    }

    @Then("the login request should be successful")
    public void theLoginRequestShouldBeSuccessful() {
        response.then().statusCode(200);
        TestContext.setLastResponse(response);
    }

    @Then("the login request should be rejected")
    public void theLoginRequestShouldBeRejected() {
        response.then().statusCode(401);
        TestContext.setLastResponse(response);
    }

    @And("a valid authentication token should be returned")
    public void aValidAuthenticationTokenShouldBeReturned() {
        LoginClient.assertTokenPresent(response);
        TestContext.setLastToken(LoginClient.getToken(response));
        TestContext.setLastResponse(response);
    }
}
