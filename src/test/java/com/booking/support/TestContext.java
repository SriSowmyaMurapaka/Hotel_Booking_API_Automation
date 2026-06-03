package com.booking.support;

import io.restassured.response.Response;

public final class TestContext {
    private static final ThreadLocal<Response> LAST_RESPONSE = new ThreadLocal<>();

    public static void setLastResponse(Response response) {
        LAST_RESPONSE.set(response);
    }

}
