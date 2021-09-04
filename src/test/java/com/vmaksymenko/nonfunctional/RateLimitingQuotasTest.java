package com.vmaksymenko.nonfunctional;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static utils.EnvReader.getUser;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import utils.GistApiUtils;
import utils.RequestSpecsProvider;

public class RateLimitingQuotasTest {

  @Test(testName = "Rate Limiting Quotas Test")
  public void rateLimitingQuotasTest() {
    // @formatter:off
    int used =
        given()
            .spec(RequestSpecsProvider.getInstance().getAuthenticatedSpec())
        .when()
            .get("rate_limit")
        .then()
            .assertThat().statusCode(200)
        .extract()
            .path("resources.core.used");

    given()
        .spec(RequestSpecsProvider.getInstance().getAuthenticatedSpec())
    .when()
        .get("/users/{username}/gists", getUser())
    .then()
        .assertThat().statusCode(200);

    given()
        .spec(RequestSpecsProvider.getInstance().getAuthenticatedSpec())
    .when()
        .get("rate_limit")
    .then()
        .assertThat().statusCode(200)
        .and().body("resources.core.used", is(used + 1));
    // @formatter:on
  }
}
