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

  @negative @invalid @deleterejected
  Scenario: Reject deleting booking without authentication
    When I create a new booking with valid booking details
    Then the booking should be created successfully
    When I delete the created booking without authentication
    Then the delete booking request should be rejected with unauthorized error

  @negative @invalid @deleterejected
  Scenario: Reject deleting booking with invalid token
    When I create a new booking with valid booking details
    Then the booking should be created successfully
    When I delete the created booking using token "invalid-token"
    Then the delete booking request should be rejected with unauthorized error

  @negative @invalid @deleterejected
  Scenario Outline: Reject deleting booking with malformed or blank token
    When I create a new booking with valid booking details
    Then the booking should be created successfully
    When I delete the created booking using token "<token>"
    Then the delete booking request should be rejected with unauthorized error

    Examples:
      | token         |
      | /             |
      | @             |
      | invalid-token |
      |               |

  @negative @invalid @deleterejected
  Scenario: Reject deleting the same booking twice
    When I create a new booking with valid booking details
    Then the booking should be created successfully
    When I delete the created booking
    Then the booking should be deleted successfully
    When I delete the created booking again
    Then the delete booking request should be rejected with not found error

  @negative @invalid @deleterejected
  Scenario Outline: Reject deleting booking with invalid booking id
    When I delete booking id "<bookingId>"
    Then the delete booking request should be rejected with not found error

    Examples:
      | bookingId |
      | -1        |
      | 0         |
      | -99999999 |
      | abc       |
      | @@@       |


