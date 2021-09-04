package utils;

import static utils.EnvReader.getToken;
import static utils.EnvReader.getUser;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

public class RequestSpecsProvider {

  private RequestSpecification defaultSpec;
  private RequestSpecification authenticatedSpec;

  private static RequestSpecsProvider self = null;

  private RequestSpecsProvider() {
    RestAssured.baseURI = "https://api.github.com";
    RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

    initSpecs();
  }

  public static RequestSpecsProvider getInstance() {
    if (self == null) {
      self = new RequestSpecsProvider();
    }
    return self;
  }

  public RequestSpecification getDefaultSpec() {
    return defaultSpec;
  }

  public RequestSpecification getAuthenticatedSpec() {
    return authenticatedSpec;
  }

  private void initSpecs() {
    defaultSpec = new RequestSpecBuilder()
        .addHeader("Accept", "application/vnd.github.v3+json")
        .addFilter(new AllureRestAssured())
        .build();

    authenticatedSpec = new RequestSpecBuilder()
        .addHeader("Accept", "application/vnd.github.v3+json")
        .addFilter(new AllureRestAssured())
        .build();

    authenticatedSpec.auth().preemptive().basic(getUser(), getToken());
  }
}
