# API TEST AUTOMATION FOR HOTEL BOOKING SYSTEM

Test automation framework using Rest-Assured,cucumber and java for testing the Hotel Booking API.

## Highlights

- Cucumber is used for BDD.
- Feature files contain examples; inputs are fed into POJOs and requests are constructed dynamically using Jackson.
- Common methods are grouped into reusable utility/client classes.
- JSON schema validation is performed using `JsonSchemaValidator`.
- Booking details responses are validated against schemas under `src/test/resources/spec/`.
- JUnit Platform is used for test execution.
- Tests can be executed using tags at feature level and scenario level.

## Technical stack

- **Java**: 21 (see `pom.xml` compiler properties)
- **Build**: Maven
- **BDD**: Cucumber `7.22.2`
- **Runner**: JUnit Platform Suite + Cucumber engine
- **API client**: RestAssured `5.5.2`
- **JSON schema validation**: RestAssured JSON Schema Validator `5.5.2`
- **JSON serialization**: Jackson Databind `2.17.1`

## Project structure

- `src/test/resources/features/`
  - Cucumber feature files (e.g. login, create booking, retrieve, update, delete, end-to-end)
- `src/test/java/com/booking/stepdefinitions/`
  - Step definitions (glue)
- `src/test/java/com/booking/clients/`
  - RestAssured clients + request builders + response assertions
- `src/test/resources/spec/`
  - JSON schemas (e.g. `bookingSchema.json`)
- `src/test/java/com/booking/TestRunner.java`
  - JUnit Platform Cucumber runner

## Configuration

### Base URL

The base URL is configured in:

- `src/test/java/com/booking/support/ApiConfig.java`

### Credentials

Credentials can be provided via:

- **Environment variables**
  - `BOOKING_USERNAME`
  - `BOOKING_PASSWORD`
- **config file** (optional)
  - `src/test/resources/config.properties`

Example `config.properties`:

## How to run

All commands below are run from the project root (same folder as `pom.xml`).

### 1) Run all tests

```bash
mvn clean test
```

### 2) Run by tag

```bash
mvn test -Dcucumber.filter.tags="@sanity"
```

Examples you may use:

- `@login`
- `@createbooking`
- `@retrievebooking`
- `@updatebooking`
- `@deletebooking`
- `@endtoendbookingflow`

### Run from IntelliJ IDEA

Open a `.feature` file and click the green **Run** icon next to a Feature/Scenario to execute it.

### Reports

The test runner is configured in `TestRunner.java` to generate:

- **HTML report**: `target/cucumber-reports.html`
- **JSON report**: `target/cucumber.json`