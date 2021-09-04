package com.vmaksymenko.functional;

import static io.restassured.RestAssured.given;

import data.Gist;
import data.GistFile;
import java.util.Arrays;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.GistApiUtils;
import utils.RequestSpecsProvider;
import utils.StringUtils;

public class DeleteGistTest {

  private GistFile gistFile;
  private Gist gist;
  private String gistId;

  @BeforeMethod(alwaysRun = true)
  public void generateGistFile() {
    gistFile = GistFile.genericTextFile();
    gist = Gist.genericGist(
        StringUtils.randomNameFor("Description"),
        true,
        Arrays.asList(gistFile)
    );
    gistId = GistApiUtils.createGist(gist);
  }

  @Test(testName = "Unauthenticated user can't delete private gist")
  public void unauthenticatedUserCanNotDeleteGistTest() {
    // @formatter:off
    given()
        .spec(RequestSpecsProvider.getInstance().getDefaultSpec())
    .when()
        .delete("/gists/{gist_id}", gistId)
    .then()
        .assertThat().statusCode(404);

    given()
        .spec(RequestSpecsProvider.getInstance().getDefaultSpec())
    .when()
        .get("/gists/{gist_id}", gistId)
    .then()
        .assertThat().statusCode(200);
    // @formatter:on
  }

  @Test(testName = "Authenticated user can delete his gist")
  public void authenticatedUserDeletesHisGistTest() {
    // @formatter:off
    given()
        .spec(RequestSpecsProvider.getInstance().getAuthenticatedSpec())
    .when()
        .delete("/gists/{gist_id}", gistId)
    .then()
        .assertThat().statusCode(204);
    // @formatter:on
  }

  /**
   * Right now this test uses random gist and might fail with 404 on attempt to get gist if it will
   * be removed. Ideally, it should check gist created by another test account
   */
  @Test(testName = "Authenticated user can't delete gist of another user")
  public void authenticatedUserDeletesAnotherUsersGistTest() {
    // @formatter:off
    given()
        .spec(RequestSpecsProvider.getInstance().getAuthenticatedSpec())
    .when()
        .delete("/gists/27f0d7084fab24421d46fbc327dcc513")
    .then()
        .assertThat().statusCode(404);

    given()
        .spec(RequestSpecsProvider.getInstance().getAuthenticatedSpec())
    .when()
        .get("/gists/4037120be76060bcfd59f72507f96ea8")
    .then()
        .assertThat().statusCode(200);
    // @formatter:on
  }

  @AfterMethod(alwaysRun = true)
  public void deleteGist() {
    GistApiUtils.deleteGist(gistId);
  }
}
