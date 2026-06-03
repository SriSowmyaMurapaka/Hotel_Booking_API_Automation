@createbooking @sanity @regression

Feature: Creating a Hotel Booking

  Background: Authenticate successfully with valid credentials
    When I log in to the booking website with valid credentials
    Then the login request should be successful
    And a valid authentication token should be returned

  @sanity @positive
  Scenario: Create a new booking with valid data
    When I create a new booking with valid booking details
    Then the booking should be created successfully
    And the response body should contain the booking details
    And the response matches with json schema "bookingSchema.json"