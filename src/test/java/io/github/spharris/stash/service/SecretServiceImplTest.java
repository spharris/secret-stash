package io.github.spharris.stash.service;

import static com.google.common.truth.Truth.assertThat;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Guice;

import io.github.spharris.stash.Secret;
import io.github.spharris.stash.service.request.CreateSecretRequest;
import io.github.spharris.stash.service.testing.TestEntities;
import io.github.spharris.stash.service.testing.TestModule;
import io.github.spharris.stash.service.utils.ObjectNameUtil;

@RunWith(JUnit4.class)
public class SecretServiceImplTest {

  @Inject ObjectMapper mapper;
  @Inject AmazonS3 client;
  @Inject SecretService secretService;
  
  @Before
  public void createInjector() {
    Guice.createInjector(new TestModule()).injectMembers(this);
  }
  
  @Test
  public void putsSecret() {
    secretService.createSecret(CreateSecretRequest.builder()
      .setProjectId(TestEntities.TEST_PROJECT_ID)
      .setEnvironmentId(TestEntities.TEST_ENVIRONMENT_ID)
      .setSecret(TestEntities.TEST_SECRET)
      .build());
    
    assertThat(client.getObject(TestEntities.TEST_BUCKET,
      ObjectNameUtil.createS3Path(
        TestEntities.TEST_PROJECT_ID,
        TestEntities.TEST_ENVIRONMENT_ID,
        TestEntities.TEST_SECRET_ID))).isNotNull();
  }
  
  @Test
  public void writesBody() throws Exception {
    secretService.createSecret(CreateSecretRequest.builder()
      .setProjectId(TestEntities.TEST_PROJECT_ID)
      .setEnvironmentId(TestEntities.TEST_ENVIRONMENT_ID)
      .setSecret(TestEntities.TEST_SECRET)
      .build());
    
    Secret result = mapper.readValue(client.getObject(TestEntities.TEST_BUCKET,
      ObjectNameUtil.createS3Path(
        TestEntities.TEST_PROJECT_ID,
        TestEntities.TEST_ENVIRONMENT_ID,
        TestEntities.TEST_SECRET_ID)).getObjectContent(), Secret.class);
    
    assertThat(result).isEqualTo(TestEntities.TEST_SECRET);
  }
  
  @Test
  public void encryptsSecret() throws Exception {
    secretService.createSecret(CreateSecretRequest.builder()
      .setProjectId(TestEntities.TEST_PROJECT_ID)
      .setEnvironmentId(TestEntities.TEST_ENVIRONMENT_ID)
      .setSecret(TestEntities.TEST_SECRET)
      .build());
    
    ObjectMetadata result = client.getObject(TestEntities.TEST_BUCKET,
      ObjectNameUtil.createS3Path(
        TestEntities.TEST_PROJECT_ID,
        TestEntities.TEST_ENVIRONMENT_ID,
        TestEntities.TEST_SECRET_ID)).getObjectMetadata();
    
    assertThat(result.getSSEAlgorithm()).isEqualTo(ObjectMetadata.AES_256_SERVER_SIDE_ENCRYPTION);
  }
}
