@updatebooking @sanity @regression

Feature: Update booking details

  Background: Authenticate successfully with valid credentials
    When I log in to the booking website with valid credentials
    Then the login request should be successful
    And a valid authentication token should be returned

  @sanity @positive @updatebooking
  Scenario: Update an existing booking successfully
    When I create a new booking with valid booking details
    Then the booking should be created successfully
    When I update the created booking with valid booking details
    Then the booking should be updated successfully
    And the response matches with json schema "bookingSchema.json"