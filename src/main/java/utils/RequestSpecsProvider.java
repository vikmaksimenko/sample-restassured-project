package utils;

import static utils.EnvReader.getToken;
import static utils.EnvReader.getUser;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

public class RequestSpecsProvider {

  private static RequestSpecification defaultSpec;
  private static RequestSpecification authenticatedSpec;

  static {
    RestAssured.baseURI = "https://api.github.com";
    RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

    initSpecs();
    GistApiUtils.cleanupGists();
  }

  public static RequestSpecification defaultSpec() {
    return defaultSpec;
  }

  public static RequestSpecification authenticatedSpec() {
    return authenticatedSpec;
  }

  private static void initSpecs() {
    defaultSpec = new RequestSpecBuilder()
        .addHeader("Accept", "application/vnd.github.v3+json")
        .build();

    authenticatedSpec = new RequestSpecBuilder()
        .addHeader("Accept", "application/vnd.github.v3+json")
        .build();

    authenticatedSpec.auth().preemptive().basic(getUser(), getToken());
  }
}
