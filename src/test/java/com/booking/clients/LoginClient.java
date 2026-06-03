package com.booking.clients;

import com.booking.pojo.LoginRequest;
import com.booking.support.ApiConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LoginClient {

    public Response login(String username, String password) {
        return given().log().all()
                .contentType(ContentType.JSON)
                .body(new LoginRequest(username, password))
                .when()
                .post(ApiConfig.BASE_URL + "/auth/login")
                .then()
                .extract()
                .response();
    }

    public String getToken(Response response) {
        try {
            return response.jsonPath().getString("token");
        } catch (Exception e) {
            return null;
        }
    }

    public void assertTokenPresent(Response response) {
        String token = getToken(response);
        assertNotNull(token, "token should not be null");
        assertFalse(token.isBlank(), "token should not be blank");
    }
}
