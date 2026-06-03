@deletebooking @sanity @regression

Feature: Delete a Hotel Booking

  Background: Authenticate successfully with valid credentials
    When I log in to the booking website with valid credentials
    Then the login request should be successful
    And a valid authentication token should be returned

  @sanity @positive @deletebooking
  Scenario: Delete an existing booking successfully
    When I create a new booking with valid booking details
    Then the booking should be created successfully
    When I delete the created booking
    Then the booking should be deleted successfully
