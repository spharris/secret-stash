package io.github.spharris.stash.service.utils;

import java.util.function.Predicate;

public class ObjectNameUtil {
  
  private ObjectNameUtil() {}
  
  public static String createS3Path(String projectId) {
    return projectId + "/";
  }
  
  public static Predicate<String> isProjectKey() {
    return (name) -> {
      return name.endsWith("/") && name.split("/").length == 1;
    };
  }
  
  public static String extractProjectId(String key) {
    return key.split("/")[0];
  }
  
  public static String createS3Path(String projectId, String environmentId) {
    return String.join("/", projectId, environmentId) + "/";
  }
  
  public static Predicate<String> isEnvironmentKey() {
    return (name) -> {
      return name.endsWith("/") && name.split("/").length == 2;
    };
  }
  
  public static String extractEnvironmentId(String key) {
    return key.split("/")[1];
  }
  
  public static String createS3Path(String projectId, String environmentId, String secretId) {
    return String.join("/", projectId, environmentId, secretId);
  }
  
  public static Predicate<String> isSecretKey() {
    return (name) -> {
      return !name.startsWith("/") && !name.endsWith("/") && name.split("/").length == 3;
    };
  }
  
  public static String extractSecretId(String key) {
    return key.split("/")[2];
  }
}
