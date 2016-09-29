package io.github.spharris.stash.service;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.google.common.collect.ImmutableList;
import com.google.common.io.ByteStreams;

import io.github.spharris.stash.Secret;
import io.github.spharris.stash.service.db.SecretDao;
import io.github.spharris.stash.service.request.CreateSecretRequest;
import io.github.spharris.stash.service.request.DeleteSecretRequest;
import io.github.spharris.stash.service.request.GetSecretRequest;
import io.github.spharris.stash.service.request.ListSecretsRequest;
import io.github.spharris.stash.service.testing.FakeS3Client;
import io.github.spharris.stash.service.testing.TestEntities;
import io.github.spharris.stash.service.utils.ObjectNameUtil;

@RunWith(MockitoJUnitRunner.class)
public class SecretServiceImplTest {
  
  @Mock SecretDao secretDao;
  
  AmazonS3 client = new FakeS3Client();
  SecretService secretService;
  
  @Before
  public void initializeService() {
    client = new FakeS3Client();
    secretService = new SecretServiceImpl(TestEntities.TEST_BUCKET, client, secretDao);
  }
  
  @Test
  public void putsSecret() {
    when(secretDao.createSecret(isA(CreateSecretRequest.class))).thenAnswer((invocation) -> {
      return ((CreateSecretRequest) invocation.getArguments()[0]).getSecret();
    });

    Secret result = secretService.createSecret(CreateSecretRequest.builder()
      .setProjectId(TestEntities.TEST_PROJECT_ID)
      .setEnvironmentId(TestEntities.TEST_ENVIRONMENT_ID)
      .setSecret(TestEntities.TEST_SECRET)
      .build());
    
    assertThat(result).isEqualTo(TestEntities.TEST_SECRET);
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
    
    byte[] bytes = ByteStreams.toByteArray(client.getObject(TestEntities.TEST_BUCKET,
      ObjectNameUtil.createS3Path(
        TestEntities.TEST_PROJECT_ID,
        TestEntities.TEST_ENVIRONMENT_ID,
        TestEntities.TEST_SECRET_ID)).getObjectContent());
    
    String result = new String(bytes, StandardCharsets.UTF_8);
    
    assertThat(result).isEqualTo(TestEntities.TEST_SECRET_VALUE);
  }
  
  @Test
  public void writesLengthToMetadata() {
    AmazonS3 mockClient = mock(AmazonS3.class);
    SecretService service = new SecretServiceImpl(TestEntities.TEST_BUCKET, mockClient, secretDao);
    service.createSecret(CreateSecretRequest.builder()
      .setProjectId(TestEntities.TEST_PROJECT_ID)
      .setEnvironmentId(TestEntities.TEST_ENVIRONMENT_ID)
      .setSecret(TestEntities.TEST_SECRET)
      .build());
    
    ArgumentCaptor<ObjectMetadata> arg = ArgumentCaptor.forClass(ObjectMetadata.class);
    verify(mockClient).putObject(
      isA(String.class), isA(String.class), isA(InputStream.class), arg.capture());
    
    byte[] bytes = TestEntities.TEST_SECRET_VALUE.getBytes(StandardCharsets.UTF_8);
    assertThat(arg.getValue().getContentLength()).isEqualTo(bytes.length);
  }
  
  @Test
  public void encryptsSecret() {
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
  
  @Test
  public void getsSecretWithoutValue() {
    AmazonS3 mockClient = mock(AmazonS3.class);
    SecretService service = new SecretServiceImpl(TestEntities.TEST_BUCKET, mockClient, secretDao);

    when(secretDao.getSecret(isA(GetSecretRequest.class)))
      .thenReturn(Optional.of(TestEntities.TEST_SECRET_NO_VALUE));
    
    Secret result = service.getSecret(GetSecretRequest.builder()
      .setProjectId(TestEntities.TEST_PROJECT_ID)
      .setEnvironmentId(TestEntities.TEST_ENVIRONMENT_ID)
      .setSecretId(TestEntities.TEST_SECRET_ID)
      .build()).get();
    
    assertThat(result).isEqualTo(TestEntities.TEST_SECRET_NO_VALUE);
    verify(mockClient, never()).getObject(anyString(), anyString());
  }
  
  @Test
  public void getsSecretWithValue() {
    when(secretDao.getSecret(isA(GetSecretRequest.class)))
      .thenReturn(Optional.of(TestEntities.TEST_SECRET_NO_VALUE));
    
    secretService.createSecret(CreateSecretRequest.builder()
      .setProjectId(TestEntities.TEST_PROJECT_ID)
      .setEnvironmentId(TestEntities.TEST_ENVIRONMENT_ID)
      .setSecret(TestEntities.TEST_SECRET)
      .build());
    
    Secret result = secretService.getSecret(GetSecretRequest.builder()
      .setProjectId(TestEntities.TEST_PROJECT_ID)
      .setEnvironmentId(TestEntities.TEST_ENVIRONMENT_ID)
      .setSecretId(TestEntities.TEST_SECRET_ID)
      .setIncludeSecretValue(true)
      .build()).get();
    
    assertThat(result).isEqualTo(TestEntities.TEST_SECRET);
  }
  
  @Test
  public void getsSecretWithNull() {
    when(secretDao.getSecret(isA(GetSecretRequest.class)))
      .thenReturn(Optional.of(TestEntities.TEST_SECRET_NO_VALUE));
    
    secretService.createSecret(CreateSecretRequest.builder()
      .setProjectId(TestEntities.TEST_PROJECT_ID)
      .setEnvironmentId(TestEntities.TEST_ENVIRONMENT_ID)
      .setSecret(TestEntities.TEST_SECRET_NO_VALUE)
      .build());
    
    Secret result = secretService.getSecret(GetSecretRequest.builder()
      .setProjectId(TestEntities.TEST_PROJECT_ID)
      .setEnvironmentId(TestEntities.TEST_ENVIRONMENT_ID)
      .setSecretId(TestEntities.TEST_SECRET_ID)
      .setIncludeSecretValue(true)
      .build()).get();
    
    assertThat(result).isEqualTo(TestEntities.TEST_SECRET_NO_VALUE);
  }
  
  @Test
  public void listsSecrets() {
    ImmutableList<Secret> expected = createSecrets(10);
    when(secretDao.listSecrets(isA(ListSecretsRequest.class))).thenReturn(expected);
    
    ImmutableList<Secret> result = secretService.listSecrets(ListSecretsRequest.builder()
      .setProjectId(TestEntities.TEST_PROJECT_ID)
      .setEnvironmentId(TestEntities.TEST_ENVIRONMENT_ID)
      .build());
    
    assertThat(result).isEqualTo(expected);
  }
  
  @Test
  public void deletesSecret() {
    AmazonS3 mockClient = mock(AmazonS3.class);
    SecretService service = new SecretServiceImpl(TestEntities.TEST_BUCKET, mockClient, secretDao);
    
    DeleteSecretRequest request = DeleteSecretRequest.builder()
        .setProjectId(TestEntities.TEST_PROJECT_ID)
        .setEnvironmentId(TestEntities.TEST_ENVIRONMENT_ID)
        .setSecretId(TestEntities.TEST_SECRET_ID)
        .build();
    
    service.deleteSecret(request);
    
    verify(mockClient).deleteObject(anyString(), anyString());
    verify(secretDao).deleteSecret(request);
  }
  
  private ImmutableList<Secret> createSecrets(int count) {
    ImmutableList.Builder<Secret> builder = ImmutableList.builder();
    for (int i = 0; i < count; i++) {
      builder.add(Secret.builder()
        .setSecretId("secret-" + i)
        .build());
    }
    
    return builder.build();
  }
}
