@healthcheck @sanity

Feature: Health Check for the booking website as a hotel staff or guest

  @sanity @positive
  Scenario: System health should be reported as UP
    Given the booking website API is available
    When I request the health check
    Then the health check response code should be 200
    And the health status should be "UP"

  @negative @invalid
  Scenario Outline: Health check should fail for an invalid endpoint
    Given the booking website API is available
    When I request the health check using endpoint "<endpoint>"
    Then the health check response code should be <statusCode>
    And Validate the error response fields
      | field  | value        |
      | status | <statusCode> |
      | error  | Not Found    |

    Examples:
      | endpoint                  | statusCode |
      | /booking/actuator/health1 | 404        |
      | /booking/actuator/HEALTH  | 404        |

  @negative @unsupportedmethod
  Scenario: Health check should reject an unsupported HTTP method
    Given the booking website API is available
    When I request the health check using POST and endpoint "/booking/actuator/health"
    Then the health check response code should be 405
    And Validate the error response fields
      | field  | value              |
      | status | 405                |
      | error  | Method Not Allowed |


