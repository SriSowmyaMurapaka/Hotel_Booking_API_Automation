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

  @negative @invalid @rejectupdate
  Scenario: Reject updating booking without authentication
    When I create a new booking with valid booking details
    Then the booking should be created successfully
    When I update the created booking without authentication
    Then the update booking request should be rejected with unauthorized error

  @negative @invalid @rejectupdate
  Scenario Outline: Reject updating booking with malformed or blank token
    When I create a new booking with valid booking details
    Then the booking should be created successfully
    When I update the created booking using token "<token>"
    Then the update booking request should be rejected with unauthorized error

    Examples:
      | token         |
      |               |
      | /             |
      | @             |
      | invalid-token |

  @negative @invalid @rejectupdate
  Scenario Outline: Reject updating booking for invalid booking id
    When I update booking id <bookingId> with valid booking details
    Then the update booking request should be rejected with not found error

    Examples:
      | bookingId |
      | -1        |
      | 0         |
      | 99999999  |

  @negative @invalid @rejectupdate
  Scenario Outline: Reject updating booking for invalid booking id (non-numeric)
    When I update booking id "<bookingId>" with valid booking details
    Then the update booking request should be rejected with not found error

    Examples:
      | bookingId |
      | abc       |
      | @@@       |

  @negative @invalid @rejectupdate
  Scenario Outline: Reject updating booking when a required field is missing
    When I create a new booking with valid booking details
    Then the booking should be created successfully
    When I update the created booking with missing field "<field>"
    Then the update booking request should be rejected
    And the error message should contain "<error>"

    Examples:
      | field     | error     |
      | firstname | Firstname |
      | lastname  | Lastname  |

  @negative @invalid @rejectupdate
  Scenario Outline: Reject updating booking when guest/contact fields are invalid
    When I create a new booking with valid booking details
    Then the booking should be created successfully
    When I update the created booking with "<field>" as "<value>"
    Then the update booking request should be rejected
    And the error message should contain "<error>"

    Examples:
      | field     | value         | error                               |
      | firstname | A             | size must be between                |
      | lastname  | B             | size must be between                |
      | firstname |               | Firstname                           |
      | lastname  |               | Lastname                            |
      | email     | invalid-email | must be a well-formed email address |
      | email     |               | must not be empty                   |
      | phone     | 123           | size must be between                |
      | phone     | abc           | size must be between                |

  @negative @invalid @rejectupdate
  Scenario Outline: Reject updating booking when booking dates conflict
    When I create a new booking with valid booking details
    Then the booking should be created successfully
    When I update the created booking with "<date_condition>"
    Then the update booking request should return status <status>

    Examples:
      | date_condition                | status |
      | checkout before checkin       | 409    |
      | checkin and checkout same day | 409    |

  @negative @invalid @rejectupdate
  Scenario Outline: Reject updating booking when booking dates are missing
    When I create a new booking with valid booking details
    Then the booking should be created successfully
    When I update the created booking with "<date_condition>"
    Then the update booking request should be rejected
    And the error message should contain "<error>"

    Examples:
      | date_condition   | error            |
      | missing checkin  | must not be null |
      | missing checkout | must not be null |

  @negative @invalid @rejectupdate
  Scenario Outline: Reject updating booking when booking dates have invalid format
    When I create a new booking with valid booking details
    Then the booking should be created successfully
    When I update the created booking with "<field>" as "<value>"
    Then the update booking request should be rejected
    And the error message should contain "<error>"

    Examples:
      | field                 | value      | error       |
      | bookingdates.checkin  | 0000/00/00 | Bad Request |
      | bookingdates.checkout | 0000/00/00 | Bad Request |
      | bookingdates.checkin  | not-a-date | Bad Request |
      | bookingdates.checkout | not-a-date | Bad Request |

  @negative @invalid @rejectupdate
  Scenario Outline: Reject updating booking when room id is invalid
    When I create a new booking with valid booking details
    Then the booking should be created successfully
    When I update the created booking with "roomid" as "<roomid>"
    Then the update booking request should be rejected
    And the error message should contain "<error>"

    Examples:
      | roomid | error                              |
      | 0      | must be greater than or equal to 1 |
      | -1     | must be greater than or equal to 1 |
      | -99999 | must be greater than or equal to 1 |
      |        | Bad Request                        |
      | abc    | Bad Request                        |

  @negative @invalid @rejectupdate
  Scenario: Reject updating booking when booking id does not exist
    When I update booking id 111111 with valid booking details
    Then the update booking request should be rejected with not found error
