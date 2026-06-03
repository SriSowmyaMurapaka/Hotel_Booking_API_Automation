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

  @negative @invalid @rejectbooking
  Scenario Outline: Reject booking creation when guest name is invalid
    When I create a booking with "<field>" as "<value>"
    Then the booking request should be rejected
    And the error message should contain "<error>"

    Examples:
      | field     | value                              | error                         |
      | firstname | A                                  | size must be between          |
      | lastname  | B                                  | size must be between          |
      | firstname | FirstnameIsWayTooLongForValidation | size must be between          |
      | lastname  | LastnameIsWayTooLongForValidation  | size must be between          |
      | firstname |                                    | Firstname should not be blank |
      | lastname  |                                    | Lastname should not be blank  |

  @negative @invalid @rejectbooking
  Scenario Outline: Reject booking creation when contact details are invalid
    When I create a booking with "<field>" as "<value>"
    Then the booking request should be rejected
    And the error message should contain "<error>"

    Examples:
      | field | value                     | error                               |
      | email | invalid-email             | must be a well-formed email address |
      | email |                           | must not be empty                   |
      | email | invalid-email@            | must be a well-formed email address |
      | email | invalid-email@@#$$        | must be a well-formed email address |
      | phone | 123                       | size must be between                |
      | phone | 1234567890123456789012345 | size must be between                |
      | phone | abcdefghijksbkjas@$#@#$%@ | size must be between                |
      | phone | @#@#@$$@                  | size must be between                |
      | phone |                           | size must be between                |

  @negative @invalid @rejectbooking
  Scenario Outline: Reject booking creation when booking dates are invalid
    When I create a booking with "<date_condition>"
    Then the booking request should be rejected
    And the error message should contain "<error>"

    Examples:
      | date_condition                | error                    |
      | checkout before checkin       | Failed to create booking |
      | checkin and checkout same day | Failed to create booking |
      | missing checkin               | must not be null         |
      | missing checkout              | must not be null         |

  @negative @invalid @rejectbooking
  Scenario Outline: Reject booking creation when booking dates have invalid format
    When I create a booking with "<field>" as "<value>"
    Then the booking request should be rejected
    And the error message should contain "<error>"

    Examples:
      | field                 | value      | error                    |
      | bookingdates.checkin  | 0000/00/00 | Failed to create booking |
      | bookingdates.checkout | 0000/00/00 | Failed to create booking |
      | bookingdates.checkin  | 2026/13/40 | Failed to create booking |
      | bookingdates.checkout | 2026/13/40 | Failed to create booking |
      | bookingdates.checkin  | not-a-date | Failed to create booking |
      | bookingdates.checkout | not-a-date | Failed to create booking |

  @negative @invalid @rejectbooking
  Scenario: Reject booking creation when room id is invalid
    When I create a booking with an invalid room id
    Then the booking request should be rejected
    And the error message should contain "must be greater than or equal to 1"

  @negative @invalid @rejectbooking
  Scenario Outline: Reject booking creation when room id is invalid
    When I create a booking with "roomid" as "<roomid>"
    Then the booking request should be rejected
    And the error message should contain "<error>"

    Examples:
      | roomid | error                              |
      | 0      | must be greater than or equal to 1 |
      | -1     | must be greater than or equal to 1 |
      | -99999 | must be greater than or equal to 1 |
      |        | Failed to create booking           |
      | abc    | Failed to create booking           |