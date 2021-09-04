package com.vmaksymenko.functional;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.not;
import static utils.EnvReader.getUser;
import static utils.RequestSpecsProvider.authenticatedSpec;
import static utils.RequestSpecsProvider.defaultSpec;

import data.Gist;
import data.GistFile;
import java.util.Arrays;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.DateUtils;
import utils.GistApiUtils;
import utils.ResponseSpecsProvider;
import utils.StringUtils;

public class ListGistsForUserTest {

  /**
   * Note: In this test we do not validate all of the pages. For making test simpler we declare that
   * test user will not have more than 30 gists
   */

  private String timestamp;
  private GistFile privateGistFile;
  private GistFile publicGistFile;
  private Gist privateGist;
  private Gist publicGist;
  private String privateGistId;
  private String publicGistId;

  @BeforeClass
  public void generateGistFiles() throws InterruptedException {
    String description = StringUtils.randomNameFor("Description");

    privateGistFile = GistFile.genericTextFile();
    publicGistFile = GistFile.genericTextFile();

    privateGist = Gist.genericGist(description, false, Arrays.asList(privateGistFile));
    privateGistId = GistApiUtils.createGist(privateGist);

    Thread.sleep(1000);   // Need to wait for a second
    timestamp = DateUtils.currentTimeInISO8601();

    publicGist = Gist.genericGist(description, true, Arrays.asList(publicGistFile));
    publicGistId = GistApiUtils.createGist(publicGist);
  }

  @Test(testName = "Unauthenticated user can read public gists for user")
  public void unauthenticatedUserReadsPublicUsersGistsTest() {
    // @formatter:off
    given()
        .spec(defaultSpec())
    .when()
        .get("/users/{username}/gists", getUser())
    .then()
        .assertThat().statusCode(200)
        .and().body("id", not(hasItems(privateGistId)))
        .and().body("id", hasItems(publicGistId))
        .and().spec(ResponseSpecsProvider.gistsResponseSpec(publicGist, publicGistId))
        .and().spec(ResponseSpecsProvider.gistsFileResponseSpec(publicGistFile, publicGistId));
    // @formatter:on
  }

  @Test(testName = "Authenticated user can read all his gist")
  public void authenticatedUserReadsAllHisGistsTest() {
    // @formatter:off
    given()
        .spec(authenticatedSpec())
    .when()
        .get("/users/{username}/gists", getUser())
    .then()
        .assertThat().statusCode(200)
        .and().body("id", hasItems(publicGistId, privateGistId))
        .and().spec(ResponseSpecsProvider.gistsResponseSpec(publicGist, publicGistId))
        .and().spec(ResponseSpecsProvider.gistsFileResponseSpec(publicGistFile, publicGistId))
        .and().spec(ResponseSpecsProvider.gistsResponseSpec(privateGist, privateGistId))
        .and().spec(ResponseSpecsProvider.gistsFileResponseSpec(privateGistFile, privateGistId));
    // @formatter:on
  }

  @Test(testName = "Test filtering by update date")
  public void filterByUpdateTest() {
    // @formatter:off
    given()
        .spec(authenticatedSpec())
        .param("since", timestamp)
    .when()
        .get("/users/{username}/gists", getUser())
    .then()
        .assertThat().statusCode(200)
        .and().body("id", hasItems(publicGistId))
        .and().body("id", not(hasItems(privateGist)));
    // @formatter:on
  }

  /**
   * Note: Test for paging is missing. We have only one test account, so its execution might affect
   * other tests
   */

  @AfterClass(alwaysRun = true)
  public void deleteGists() {
    GistApiUtils.deleteGist(privateGistId);
    GistApiUtils.deleteGist(publicGistId);
  }
}
