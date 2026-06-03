@healthcheck @sanity

Feature: Health Check for the booking website as a hotel staff or guest

  @sanity @positive
  Scenario: System health should be reported as UP
    Given the booking website API is available
    When I request the health check
    Then the health check response code should be 200
    And the health status should be "UP"
