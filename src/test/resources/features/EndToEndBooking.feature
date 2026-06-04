@endtoendbookingflow

Feature: End to end booking flow

  Background: Authenticate successfully with valid credentials
    When I log in to the booking website with valid credentials
    Then the login request should be successful
    And a valid authentication token should be returned

  @endtoendbookingflow
  Scenario: Create, update and delete a booking successfully
    When I create a new booking with valid booking details
    Then the booking should be created successfully

    When I retrieve the booking details for the created booking id
    Then the booking details should be returned successfully

    When I update the created booking with valid booking details
    Then the booking should be updated successfully

    When I delete the created booking
    Then the booking should be deleted successfully
