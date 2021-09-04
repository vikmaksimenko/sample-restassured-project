package utils;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import data.Gist;
import data.GistFile;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.ResponseSpecification;

public class ResponseSpecsProvider {

  public static ResponseSpecification gistResponseSpec(Gist gist) {
    return new ResponseSpecBuilder()
        .expectBody("public", is(gist.getPublic()))
        .expectBody("description", is(gist.getDescription()))
        .build();
  }

  public static ResponseSpecification gistFileResponseSpec(GistFile file) {
    String filePath = "files['" + file.getFilename() + "']";
    return validateGistsFileResponseSpec(file, filePath);
  }

  public static ResponseSpecification gistsResponseSpec(Gist gist, String gistId) {
    return new ResponseSpecBuilder()
        .expectBody("find { it.id == '" + gistId + "' }.public", is(gist.getPublic()))
        .expectBody("find { it.id == '" + gistId + "' }.description", is(gist.getDescription()))
        .build();
  }

  public static ResponseSpecification gistsFileResponseSpec(GistFile file, String gistId) {
    String filePath = "find { it.id == '" + gistId + "' }.files['" + file.getFilename() + "']";
    return validateGistsFileResponseSpec(file, filePath);
  }

  public static ResponseSpecification validateGistsFileResponseSpec(GistFile file,
      String filePath) {
    return new ResponseSpecBuilder()
        .expectBody(filePath, notNullValue())
        .expectBody(filePath + "['filename']", is(file.getFilename()))
        .expectBody(filePath + "['type']", is(file.getType()))
        .expectBody(filePath + "['language']", is(file.getLanguage()))
        .build();
  }
}
