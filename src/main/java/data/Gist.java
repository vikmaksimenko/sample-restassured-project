package data;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Gist {

  private String description;

  @JsonProperty("public")
  private boolean isPublic;

  private Map<String, GistFile> files;

  public Gist(String description, boolean isPublic,
      Map<String, GistFile> files) {
    this.description = description;
    this.isPublic = isPublic;
    this.files = files;
  }

  public static Gist genericGist(String description, boolean isPublic, List<GistFile> files) {
    Map<String, GistFile> gistFiles = new HashMap<>();
    files.stream().forEach(file -> gistFiles.put(file.getFilename(), file));
    return new Gist(description, isPublic, gistFiles);
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public boolean getPublic() {
    return isPublic;
  }

  public void setPublic(boolean aPublic) {
    isPublic = aPublic;
  }

  public Map<String, GistFile> getFiles() {
    return files;
  }

  public void setFiles(Map<String, GistFile> files) {
    this.files = files;
  }
}
