package com.booking.clients;

import com.booking.support.ApiConfig;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class HealthCheckClient {

    public Response getHealthCheck() {
        return getHealthCheck("/booking/actuator/health");
    }

    public Response getHealthCheck(String endpointPath) {
        return given().log().all()
                .when()
                .get(ApiConfig.BASE_URL + endpointPath)
                .then()
                .extract()
                .response();
    }

}
