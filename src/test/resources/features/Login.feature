@login @sanity

Feature: Login for hotel booking

 #Positive scenario
  @sanity
  Scenario: Authenticate successfully with valid credentials
    When I log in to the booking website with valid credentials
    Then the login request should be successful
    And a valid authentication token should be returned