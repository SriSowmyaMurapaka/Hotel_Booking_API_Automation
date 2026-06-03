@login @sanity

Feature: Login for hotel booking

  @sanity @positive @authentication
  Scenario: Authenticate successfully with valid credentials
    When I log in to the booking website with valid credentials
    Then the login request should be successful
    And a valid authentication token should be returned

  @negative @authentication
  Scenario Outline: Login to the booking website with invalid credentials (wrong username,wrong password, empty credentials)
    When I log in to the booking website using username "<username>" and password "<password>"
    Then the login request should be rejected
    And Validate the "Unauthorized" error response
      | error   |
      | <error> |

    Examples:
      | username                              | password                              | error               |
      | admin1                                | password                              | Invalid credentials |
      | admin                                 | password1                             | Invalid credentials |
      |                                       |                                       | Invalid credentials |
      | admin                                 |                                       | Invalid credentials |
      |                                       | password                              | Invalid credentials |
      | ADMIN                                 | password                              | Invalid credentials |
      | admin                                 | PASSWORD                              | Invalid credentials |
      | admin                                 | wrong-password-!@#                    | Invalid credentials |
      | ' OR '1'='1                           | password                              | Invalid credentials |
      | admin                                 | ' OR '1'='1                           | Invalid credentials |
      | <script>alert(1)</script>             | password                              | Invalid credentials |
      | admin                                 | <script>alert(1)</script>             | Invalid credentials |
      | this_username_is_way_too_long_to_work | this_password_is_way_too_long_to_work | Invalid credentials |


