@retrievebooking @sanity @regression

Feature: Retrieve Booking Details

  Background: Authenticate successfully with valid credentials
    When I log in to the booking website with valid credentials
    Then the login request should be successful
    And a valid authentication token should be returned

  @sanity @positive @retrievebooking
  Scenario: Retrieve booking details for a created booking
    When I create a new booking with valid booking details
    Then the booking should be created successfully
    When I retrieve the booking details for the created booking id
    Then the booking details should be returned successfully
    And the response matches with json schema "bookingSchema.json"

  @negative @invalid @retrievebookingrejected
  Scenario: Reject retrieving booking details without token
    When I retrieve the booking details for booking id 1 without authentication
    Then the request should be rejected with unauthorized error

  @negative @invalid @retrievebookingrejected
  Scenario Outline: Reject retrieving booking details for invalid booking id
    When I retrieve the booking details for booking id "<bookingId>"
    Then the booking details request should be rejected with not found

    Examples:
      | bookingId |
      | -1        |
      | 0         |
      | 999999    |
      | abc       |
      | @@@       |

  @negative @invalid @retrievebookingrejected
  Scenario Outline: Reject retrieving booking details with malformed or blank token
    When I create a new booking with valid booking details
    Then the booking should be created successfully
    When I retrieve the booking details for the created booking id using token "<token>"
    Then the request should be rejected with unauthorized error

    Examples:
      | token         |
      |               |
      | /             |
      | @             |
      | invalid-token |
