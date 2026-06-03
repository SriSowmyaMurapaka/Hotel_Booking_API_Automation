package com.booking.common;

import com.booking.support.TestContext;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.restassured.response.Response;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ErrorResponseSteps {

    private Response latestResponse() {
        Response response = TestContext.getLastResponse();
        assertNotNull(response, "No response found in TestContext. Make sure a previous step stored the response.");
        return response;
    }

    private void assertJsonFields(Response response, Map<String, String> expectedFields, String context) {
        for (Map.Entry<String, String> entry : expectedFields.entrySet()) {
            String field = entry.getKey();
            String expectedValue = entry.getValue();
            if (field == null || field.isBlank() || expectedValue == null) {
                continue;
            }

            String actualValue;
            try {
                actualValue = response.jsonPath().getString(field);
            } catch (Exception e) {
                fail("Response body is not JSON or cannot read field '" + field + "' for '" + context + "': " + e.getMessage());
                return;
            }

            assertEquals(expectedValue, actualValue, "Unexpected value for field '" + field + "' for '" + context + "'");
        }
    }

    @And("Validate the {string} error response")
    public void validateTheErrorResponse(String errorName, DataTable dataTable) {
        Response response = latestResponse();
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        assertFalse(rows.isEmpty(), "Error response table must contain at least 1 row");

        Map<String, String> expected = rows.get(0);
        assertJsonFields(response, expected, errorName);
    }

    @And("Validate the error response fields")
    public void validateTheErrorResponseFields(DataTable dataTable) {
        Response response = latestResponse();
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        assertFalse(rows.isEmpty(), "Error response fields table must contain at least 1 row");

        Map<String, String> expected = new LinkedHashMap<>();
        for (Map<String, String> row : rows) {
            String field = row.get("field");
            String value = row.get("value");
            if (field != null && !field.isBlank()) {
                expected.put(field, value);
            }
        }

        assertJsonFields(response, expected, "error response");
    }
}
