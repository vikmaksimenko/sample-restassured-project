# Sample RestAssured Project

## Prerequisites

1. Java 8+
2. Maven 3+

## Tools

1. TestNG for running tests
2. RestAssured for performing API tests

## Running tests on local environment

1. Make sure that you have JDK 1.8 or greater and Maven 3 installed.
2. Put your GitHub username and token into GITHUB_USER and GITHUB_TOKEN env variables
3. Run tests with `mvn clean test`

## Running tests with Docker

If you have Docker installed, you may run tests in Docker containers. Don't forget to update
GITHUB_USER and GITHUB_TOKEN env variables

```shell
docker run -it --rm --name my-maven-project -v "$(pwd)":/usr/src/mymaven -w /usr/src/mymaven -e GITHUB_TOKEN=${GITHUB_TOKEN} -e GITHUB_USER=${GITHUB_USER} maven:3.3-jdk-8 mvn clean test
```

## Improvements

Some checks might be excessive for assessment, however, they should be mentioned:

* Add JSON Schema validation
* Run tests in parallel (tried doing this, but failed with BeforeMethods, will have to investigate)