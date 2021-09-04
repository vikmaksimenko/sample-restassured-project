package utils;

public class EnvReader {

  private static String token;
  private static String user;

  static {
    token = System.getenv("GITHUB_TOKEN");
    user = System.getenv("GITHUB_USER");

    if (EnvReader.getUser() == null || EnvReader.getToken() == null) {
      System.err.println("\n!!! GITHUB_TOKEN or GITHUB_USER variables were not provided. !!! \n"
          + "!!! Stopping tests execution !!!\n");
      System.exit(1);
    }
  }

  public static String getToken() {
    return token;
  }

  public static String getUser() {
    return user;
  }
}
