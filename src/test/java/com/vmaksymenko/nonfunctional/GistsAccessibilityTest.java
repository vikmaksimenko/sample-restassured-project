package com.vmaksymenko.nonfunctional;

import static io.restassured.RestAssured.given;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.hamcrest.Matchers.lessThan;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import utils.GistApiUtils;
import utils.RequestSpecsProvider;

public class GistsAccessibilityTest {

  @BeforeSuite
  @AfterSuite(alwaysRun = true)
  public void cleanup() {
    GistApiUtils.cleanupGists();
  }

  @Test(testName = "Gist accessibility test")
  public void unauthenticatedUserReadsPublicGistTest() {
    // @formatter:off
    given()
        .spec(RequestSpecsProvider.getInstance().getDefaultSpec())
    .when()
        .get("/gists")
    .then()
        .assertThat().statusCode(200)
        .and().time(lessThan(5L), SECONDS);
    // @formatter:on
  }
}
