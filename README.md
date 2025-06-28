# Fake API Test Task

## Prerequisites

* **Java 17** (or higher)
* **Maven 3.9+**

## Running the Tests

```bash
mvn clean test
```

### Run a Specific Suite

```bash
mvn clean test -Dsurefire.suiteXmlFiles=src/test/resources/suites/<suite>.xml
```

> Replace `<suite>` with the name of the XML suite file you want to execute.

## Viewing the Test Report (Allure)

After the test run finishes, generate and open the Allure report:

```bash
mvn allure:serve
```

This command will build the report and launch a local server at [http://localhost\:port](http://localhost:port), opening it automatically in your default browser.
Example of the report you can see in `report-example` folder, open `index.html` file.