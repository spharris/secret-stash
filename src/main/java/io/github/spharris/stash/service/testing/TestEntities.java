package io.github.spharris.stash.service.testing;

import io.github.spharris.stash.Project;

/**
 * Entities used in test cases
 */
public class TestEntities {

  private TestEntities() {}
  
  public static final String TEST_BUCKET = "test-bucket";
  
  public static final String TEST_PROJECT_ID = "test-project";
  public static final String TEST_PROJECT_DESCRIPTION = "test-project-description";
  public static final Project TEST_PROJECT = Project.builder()
      .setProjectId(TEST_PROJECT_ID)
      .setDescription(TEST_PROJECT_DESCRIPTION)
      .build();

  public static final String TEST_ENVIRONMENT_ID = "test-environment";
  public static final String TEST_SECRET_ID = "test-secret";
}
