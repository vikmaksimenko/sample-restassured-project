package com.vmaksymenko.functional;

import static io.restassured.RestAssured.given;

import data.Gist;
import data.GistFile;
import java.util.Arrays;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.GistApiUtils;
import utils.RequestSpecsProvider;
import utils.ResponseSpecsProvider;
import utils.StringUtils;

public class ReadGistTest {

  private String description;
  private GistFile privateGistFile;
  private GistFile publicGistFile;
  private Gist privateGist;
  private Gist publicGist;
  private String privateGistId;
  private String publicGistId;

  @BeforeClass
  public void generateGistFiles() {
    privateGistFile = GistFile.genericTextFile();
    publicGistFile = GistFile.genericTextFile();
    description = StringUtils.randomNameFor("Description");

    privateGist = Gist.genericGist(description, false, Arrays.asList(privateGistFile));
    privateGistId = GistApiUtils.createGist(privateGist);

    publicGist = Gist.genericGist(description, true, Arrays.asList(publicGistFile));
    publicGistId = GistApiUtils.createGist(publicGist);
  }

  @Test(testName = "Unauthenticated user can read public gist")
  public void unauthenticatedUserReadsPublicGistTest() {
    // @formatter:off
    given()
        .spec(RequestSpecsProvider.getInstance().getDefaultSpec())
    .when()
        .get("/gists/{gist_id}", publicGistId)
    .then()
        .assertThat().statusCode(200)
        .and().spec(ResponseSpecsProvider.gistResponseSpec(publicGist))
        .and().spec(ResponseSpecsProvider.gistFileResponseSpec(publicGistFile));
    // @formatter:on
  }

  /**
   * This one looks like a bug, but it's not. According to https://bounty.github.com/targets/gist.html:
   * <p>
   * If you share the URL of a secret gist, anyone with access to the URL will be able to see it
   * without authentication. This is an intentional feature.
   * <p>
   * So, the test is obsolete, yet I decided to highlight it
   */

  @Test(testName = "Unauthenticated user can read private gist")
  public void unauthenticatedUserCanNotReadPrivateGistTest() {
    // @formatter:off
    given()
        .spec(RequestSpecsProvider.getInstance().getDefaultSpec())
    .when()
        .get("/gists/{gist_id}", privateGistId)
    .then()
        .assertThat().statusCode(200)
        .and().spec(ResponseSpecsProvider.gistResponseSpec(privateGist))
        .and().spec(ResponseSpecsProvider.gistFileResponseSpec(privateGistFile));
    // @formatter:on
  }

  @Test(testName = "Authenticated user can read private gist")
  public void authenticatedUserCreatesGistTest() {
    // @formatter:off
    given()
        .spec(RequestSpecsProvider.getInstance().getAuthenticatedSpec())
    .when()
        .get("/gists/{gist_id}", privateGistId)
    .then()
        .assertThat().statusCode(200)
        .and().spec(ResponseSpecsProvider.gistResponseSpec(privateGist))
        .and().spec(ResponseSpecsProvider.gistFileResponseSpec(privateGistFile));
    // @formatter:on
  }

  @AfterClass(alwaysRun = true)
  public void deleteGists() {
    GistApiUtils.deleteGist(privateGistId);
    GistApiUtils.deleteGist(publicGistId);
  }
}
