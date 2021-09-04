package utils;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static utils.EnvReader.getUser;

import data.Gist;

public class GistApiUtils {

  public static String createGist(Gist gist) {
    // @formatter:off
    return
        given()
            .spec(RequestSpecsProvider.getInstance().getAuthenticatedSpec())
            .body(gist)
        .when()
            .post("/gists")
        .then()
            .assertThat().statusCode(201)
            .and().body("id", notNullValue())
        .extract()
            .response()
            .path("id");
    // @formatter:on
  }

  public static void deleteGist(String gistId) {
    // @formatter:off
    given()
        .spec(RequestSpecsProvider.getInstance().getAuthenticatedSpec())
    .when()
        .delete("/gists/{gist_id}", gistId)
    .then()
        .statusCode(anyOf(is(204), is(404)));
    // @formatter:on
  }

  public static void cleanupGists() {
    // @formatter:off
    given()
        .spec(RequestSpecsProvider.getInstance().getAuthenticatedSpec())
        .param("per_page", "100")
    .when()
        .get(" /users/{user}/gists", getUser())
    .then()
        .statusCode(200)
    .extract()
        .response()
        .jsonPath()
        .getList("id")
        .forEach(id -> deleteGist((String) id));
    // @formatter:on
  }
}
