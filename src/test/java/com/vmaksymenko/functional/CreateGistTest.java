package com.vmaksymenko.functional;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import data.Gist;
import data.GistFile;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.HashMap;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.GistApiUtils;
import utils.RequestSpecsProvider;
import utils.ResponseSpecsProvider;
import utils.StringUtils;

public class CreateGistTest {

  private GistFile gistFile;
  private Gist gist;
  private String gistId;

  @BeforeMethod(alwaysRun = true)
  public void generateGist() {
    gistFile = GistFile.genericTextFile();
    gist = Gist.genericGist(
        StringUtils.randomNameFor("Description"),
        true,
        Arrays.asList(gistFile)
    );
  }

  @Test(testName = "Unauthenticated user can't create gist")
  public void unauthenticatedUserCreatesGistTest() {
    // @formatter:off
    given()
        .spec(RequestSpecsProvider.getInstance().getDefaultSpec())
        .body(gist)
    .when()
        .post("/gists")
    .then()
        .assertThat().statusCode(401);
    // @formatter:on
  }

  @Test(testName = "Authenticated user can create gist")
  public void authenticatedUserCreatesGistTest() {
    // @formatter:off
    Response response =
        given()
            .spec(RequestSpecsProvider.getInstance().getAuthenticatedSpec())
            .body(gist)
        .when()
            .post("/gists")
        .then()
            .assertThat().statusCode(201)
            .and().body("id", notNullValue())
            .and().spec(ResponseSpecsProvider.gistResponseSpec(gist))
            .and().spec(ResponseSpecsProvider.gistFileResponseSpec(gistFile))
        .extract()
            .response();
    // @formatter:on
  }

  @Test(testName = "Can't create gist without files")
  public void canNotCreateGistWithoutFilesTest() {
    // @formatter:off
    given()
        .spec(RequestSpecsProvider.getInstance().getAuthenticatedSpec())
        .body(new HashMap<>())
    .when()
        .post("/gists")
    .then()
        .assertThat().statusCode(422)
        .and().body("message", is("Invalid request.\n\n\"files\" wasn't supplied."));
    // @formatter:on
  }

  @AfterMethod(alwaysRun = true)
  public void deleteGistIfCreated() {
    if (gistId != null) {
      GistApiUtils.deleteGist(gistId);
    }
  }
}