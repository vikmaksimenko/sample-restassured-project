package data;

import utils.StringUtils;

public class GistFile {

  private String filename;
  private String content;
  private String type;
  private String language;

  public GistFile(String name, String content, String type, String language) {
    this.filename = name;
    this.content = content;
    this.type = type;
    this.language = language;
  }

  public static GistFile genericTextFile() {
    return new GistFile(
        StringUtils.randomNameFor("test_file") + ".txt",
        "Test file",
        "text/plain",
        "Text"
    );
  }

  public String getFilename() {
    return filename;
  }

  public void setFilename(String filename) {
    this.filename = filename;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }
}
