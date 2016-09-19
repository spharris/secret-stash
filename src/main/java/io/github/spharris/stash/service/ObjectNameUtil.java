package io.github.spharris.stash.service;

public class ObjectNameUtil {
  
  private ObjectNameUtil() {}
  
  public static String createS3Path(String projectId) {
    return projectId + "/";
  }
  
  public static String createS3Path(String projectId, String environmentId) {
    return String.join("/", projectId, environmentId) + "/";
  }
  
  public static String createS3Path(String projectId, String environmentId, String secretId) {
    return String.join("/", projectId, environmentId, secretId);
  }
}
