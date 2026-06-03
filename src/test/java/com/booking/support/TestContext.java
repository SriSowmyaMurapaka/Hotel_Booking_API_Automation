package com.booking.support;

import io.restassured.response.Response;

public final class TestContext {
    private static final ThreadLocal<Response> LAST_RESPONSE = new ThreadLocal<>();
    private static final ThreadLocal<String> LAST_TOKEN = new ThreadLocal<>();

    private TestContext() {
    }

    public static void setLastResponse(Response response) {
        LAST_RESPONSE.set(response);
    }

    public static Response getLastResponse() {
        return LAST_RESPONSE.get();
    }

    public static void setLastToken(String token) {
        LAST_TOKEN.set(token);
    }

    public static String getLastToken() {
        return LAST_TOKEN.get();
    }
}
