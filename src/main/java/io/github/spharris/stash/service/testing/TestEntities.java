package io.github.spharris.stash.service.testing;

import io.github.spharris.stash.AccessControlList;
import io.github.spharris.stash.Environment;
import io.github.spharris.stash.Project;
import io.github.spharris.stash.Secret;
import io.github.spharris.stash.service.aws.Policy;

/**
 * Entities used in test cases
 */
public class TestEntities {

  private TestEntities() {}
  
  public static final String TEST_BUCKET = "test-bucket";
  
  /*
   * Project-related entities
   */
  public static final String TEST_PROJECT_ID = "test-project";
  public static final String TEST_PROJECT_DESCRIPTION = "test-project-description";
  
  public static final Project TEST_PROJECT = Project.builder()
      .setProjectId(TEST_PROJECT_ID)
      .setDescription(TEST_PROJECT_DESCRIPTION)
      .build();

  /*
   * Policy-related entities
   */
  public static final String TEST_POLICY_NAME = "test-policy-name";
  public static final String TEST_POLICY_ID = "test-policy-id";
  public static final String TEST_POLICY_PATH = "/test/policy/path/";
  public static final String TEST_POLICY_PREFIX = "test-policy-prefix";
  public static final String TEST_POLICY_ARN = "test-policy-arn";
  public static final String TEST_POLICY_ACCOUNT = "12345";
  
  public static final Policy TEST_POLICY = Policy.builder()
      .setArn(TEST_POLICY_ARN)
      .build();
  
  /*
   * Environment-related entities
   */
  public static final String TEST_ENVIRONMENT_ID = "test-environment";
  public static final String TEST_ENVIRONMENT_DESCRIPTION = "test-environment-description";
  public static final String TEST_ROLE = "test-role-arn";
  public static final String TEST_GROUP = "test-group-arn";
  
  public static final AccessControlList TEST_ACL = AccessControlList.builder()
      .setPolicyArn(TEST_POLICY_ARN)
      .setGroups(TEST_GROUP)
      .setRoles(TEST_ROLE)
      .build();
  
  public static final AccessControlList TEST_ACL_NO_ARN = AccessControlList.builder()
      .setGroups(TEST_GROUP)
      .setRoles(TEST_ROLE)
      .build();
  
  public static final Environment TEST_ENVIRONMENT = Environment.builder()
      .setEnvironmentId(TEST_ENVIRONMENT_ID)
      .setDescription(TEST_ENVIRONMENT_DESCRIPTION)
      .setAcl(TEST_ACL)
      .build();
  
  public static final Environment TEST_ENVIRONMENT_NO_ARN = Environment.builder()
      .setEnvironmentId(TEST_ENVIRONMENT_ID)
      .setDescription(TEST_ENVIRONMENT_DESCRIPTION)
      .setAcl(TEST_ACL)
      .build();
  
  /*
   * Secret-related entities
   */
  public static final String TEST_SECRET_ID = "test-secret";
  public static final String TEST_SECRET_DESCRIPTION = "test-secret-description";
  public static final String TEST_SECRET_VALUE = "test-secret-value";
  
  public static final Secret TEST_SECRET = Secret.builder()
      .setSecretId(TEST_SECRET_ID)
      .setDescription(TEST_SECRET_DESCRIPTION)
      .setSecretValue(TEST_SECRET_VALUE)
      .build();
}
