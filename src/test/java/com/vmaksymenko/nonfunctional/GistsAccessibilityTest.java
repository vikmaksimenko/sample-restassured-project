package com.vmaksymenko.nonfunctional;

import static io.restassured.RestAssured.given;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.hamcrest.Matchers.lessThan;
import static utils.RequestSpecsProvider.defaultSpec;

import org.testng.annotations.Test;

public class GistsAccessibilityTest {

  /**
   * There are no strict requirements for checking gist accessibility, so we will check that GET
   * request to /gists returns code 200 and response time is not bigger than 5 seconds.
   * <p>
   * Also, I'm not sure that I get the task properly here, because service unavailability and
   * reaching a rate limit should be a show stopper. However, we may also check the response time
   * and rate limit changing
   */

  @Test(testName = "Gist accessibility test")
  public void unauthenticatedUserReadsPublicGistTest() {
    // @formatter:off
    given()
        .spec(defaultSpec())
    .when()
        .get("/gists")
    .then()
        .assertThat().statusCode(200)
        .and().time(lessThan(5L), SECONDS);
    // @formatter:on
  }
}
