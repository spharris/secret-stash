package io.github.spharris.stash.service;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import io.github.spharris.stash.service.testing.TestEntities;

@RunWith(JUnit4.class)
public class ObjectNameUtilTest {

  @Test
  public void createsProjectName() {
    assertThat(ObjectNameUtil.createS3Path(TestEntities.TEST_PROJECT_ID))
        .isEqualTo(TestEntities.TEST_PROJECT_ID + "/");
  }
  
  @Test
  public void createsEnvironmentName() {
    assertThat(ObjectNameUtil.createS3Path(
      TestEntities.TEST_PROJECT_ID,
      TestEntities.TEST_ENVIRONMENT_ID))
      .isEqualTo(TestEntities.TEST_PROJECT_ID + "/" + TestEntities.TEST_ENVIRONMENT_ID + "/");
  }
  
  @Test
  public void createsSecretName() {
    assertThat(ObjectNameUtil.createS3Path(
      TestEntities.TEST_PROJECT_ID,
      TestEntities.TEST_ENVIRONMENT_ID,
      TestEntities.TEST_SECRET_ID))
      .isEqualTo(TestEntities.TEST_PROJECT_ID
        + "/" + TestEntities.TEST_ENVIRONMENT_ID
        + "/" + TestEntities.TEST_SECRET_ID);
  }
}
