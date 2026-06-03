package com.booking.support;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ApiConfig {

    public static final String BASE_URL = "https://automationintesting.online/api";

    private static final String USERNAME_SYSPROP = "booking.username";
    private static final String USERNAME_ENV = "BOOKING_USERNAME";
    private static final String PASSWORD_SYSPROP = "booking.password";
    private static final String PASSWORD_ENV = "BOOKING_PASSWORD";

    private static final Properties PROPS = new Properties();

    static {
        try (InputStream in = ApiConfig.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (in != null) {
                PROPS.load(in);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    public static String username() {
        return getConfig(USERNAME_SYSPROP, USERNAME_ENV, USERNAME_SYSPROP);
    }

    public static String password() {
        return getConfig(PASSWORD_SYSPROP, PASSWORD_ENV, PASSWORD_SYSPROP);
    }

    private static String getConfig(String systemPropertyName, String envVarName, String propertiesKey) {
        return firstNonBlank(
                System.getProperty(systemPropertyName),
                System.getenv(envVarName),
                PROPS.getProperty(propertiesKey),
                null
        );
    }

    private static String firstNonBlank(String... values) {
        if (values == null) {
            return null;
        }
        for (String v : values) {
            if (v != null && !v.isBlank()) {
                return v;
            }
        }
        return null;
    }
}
