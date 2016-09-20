package io.github.spharris.stash.service;

import static com.google.common.truth.Truth.assertThat;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.amazonaws.services.s3.AmazonS3;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Guice;

import io.github.spharris.stash.Environment;
import io.github.spharris.stash.service.request.CreateEnvironmentRequest;
import io.github.spharris.stash.service.testing.TestEntities;
import io.github.spharris.stash.service.testing.TestModule;
import io.github.spharris.stash.service.utils.ObjectNameUtil;

@RunWith(JUnit4.class)
public class EnvironmentServiceImplTest {

  @Inject ObjectMapper mapper;
  @Inject AmazonS3 client;
  @Inject EnvironmentService environmentService;
  
  @Before
  public void createInjector() {
    Guice.createInjector(new TestModule()).injectMembers(this);
  }
  
  @Test
  public void putsEnvironmentFolder() {
    environmentService.createEnvironment(CreateEnvironmentRequest.builder()
      .setProjectId(TestEntities.TEST_PROJECT_ID)
      .setEnvironment(TestEntities.TEST_ENVIRONMENT)
      .build());
    
    assertThat(client.getObject(
      TestEntities.TEST_BUCKET, ObjectNameUtil.createS3Path(
        TestEntities.TEST_PROJECT_ID, TestEntities.TEST_ENVIRONMENT_ID))).isNotNull();
  }
  
  @Test
  public void writesBody() throws Exception {
    environmentService.createEnvironment(CreateEnvironmentRequest.builder()
      .setProjectId(TestEntities.TEST_PROJECT_ID)
      .setEnvironment(TestEntities.TEST_ENVIRONMENT)
      .build());
    
    Environment result = mapper.readValue(client.getObject(TestEntities.TEST_BUCKET,
      ObjectNameUtil.createS3Path(TestEntities.TEST_PROJECT_ID, TestEntities.TEST_ENVIRONMENT_ID))
      .getObjectContent(), Environment.class);
    
    assertThat(result).isEqualTo(TestEntities.TEST_ENVIRONMENT);
  }
}
