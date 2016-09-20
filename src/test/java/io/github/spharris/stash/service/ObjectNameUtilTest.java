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
  
  @Test
  public void identifiesProjectKey() {
    assertThat(ObjectNameUtil.isProjectKey().test("hello/")).isTrue();
    assertThat(ObjectNameUtil.isProjectKey().test("/hello")).isFalse();
    assertThat(ObjectNameUtil.isProjectKey().test("he/llo")).isFalse();
  }
  
  @Test
  public void extractsProjectName() {
    assertThat(ObjectNameUtil.extractProjectId("hello/")).isEqualTo("hello");
    assertThat(ObjectNameUtil.extractProjectId("hello/world")).isEqualTo("hello");
    assertThat(ObjectNameUtil.extractProjectId("hello/world/the")).isEqualTo("hello");
  }
  
  @Test
  public void identifiesEnvironmentKey() {
    assertThat(ObjectNameUtil.isEnvironmentKey().test("hello/world/")).isTrue();
    assertThat(ObjectNameUtil.isEnvironmentKey().test("hello/world")).isFalse();
    assertThat(ObjectNameUtil.isEnvironmentKey().test("/hello/world")).isFalse();
    assertThat(ObjectNameUtil.isEnvironmentKey().test("hello")).isFalse();
  }
  
  @Test
  public void extractsEnvironmentName() {
    assertThat(ObjectNameUtil.extractEnvironmentId("hello/world")).isEqualTo("world");
    assertThat(ObjectNameUtil.extractEnvironmentId("hello/world/the")).isEqualTo("world");
  }
  
  @Test
  public void identifiesSecretKey() {
    assertThat(ObjectNameUtil.extractSecretId("hello/world/the")).isEqualTo("the");
  }
}
