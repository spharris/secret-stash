package io.github.spharris.stash.service;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.ImmutableList;

import io.github.spharris.stash.Environment;
import io.github.spharris.stash.Secret;
import io.github.spharris.stash.service.db.EnvironmentDao;
import io.github.spharris.stash.service.request.CreateEnvironmentPolicyRequest;
import io.github.spharris.stash.service.request.CreateEnvironmentRequest;
import io.github.spharris.stash.service.request.DeleteEnvironmentPolicyRequest;
import io.github.spharris.stash.service.request.DeleteEnvironmentRequest;
import io.github.spharris.stash.service.request.DeleteSecretRequest;
import io.github.spharris.stash.service.request.GetEnvironmentRequest;
import io.github.spharris.stash.service.request.ListEnvironmentsRequest;
import io.github.spharris.stash.service.request.ListSecretsRequest;
import io.github.spharris.stash.service.testing.TestEntities;

@RunWith(MockitoJUnitRunner.class)
public class EnvironmentServiceImplTest {

  @Mock EnvironmentDao environmentDao;
  @Mock PolicyService policyService;
  @Mock SecretService secretService;
  
  EnvironmentService environmentService;
  
  @Before
  public void initializeService() {
    environmentService = new EnvironmentServiceImpl(environmentDao, policyService, secretService);
  }
  
  @Test
  public void createsEnvironment() {
    when(policyService.createEnvironmentPolicy(isA(CreateEnvironmentPolicyRequest.class))).thenReturn(
      TestEntities.TEST_POLICY);

    when(environmentDao.createEnvironment(isA(CreateEnvironmentRequest.class)))
      .thenAnswer((invocation) -> {
        return ((CreateEnvironmentRequest) invocation.getArguments()[0]).getEnvironment();
      });
    
    Environment result = environmentService.createEnvironment(CreateEnvironmentRequest.builder()
      .setProjectId(TestEntities.TEST_PROJECT_ID)
      .setEnvironment(TestEntities.TEST_ENVIRONMENT_NO_ARN)
      .build());
    
    assertThat(result).isEqualTo(TestEntities.TEST_ENVIRONMENT);
    verify(environmentDao).createEnvironment(CreateEnvironmentRequest.builder()
      .setProjectId(TestEntities.TEST_PROJECT_ID)
      .setEnvironment(TestEntities.TEST_ENVIRONMENT)
      .build());
  }
  
  @Test
  public void getsEnvironment() {
    when(environmentDao.getEnvironment(isA(GetEnvironmentRequest.class)))
      .thenReturn(Optional.of(TestEntities.TEST_ENVIRONMENT));
    
    Optional<Environment> result = environmentService.getEnvironment(GetEnvironmentRequest.builder()
      .setProjectId(TestEntities.TEST_PROJECT_ID)
      .setEnvironmentId(TestEntities.TEST_ENVIRONMENT_ID)
      .build());
    
    assertThat(result.get()).isEqualTo(TestEntities.TEST_ENVIRONMENT);
  }
  
  @Test
  public void listsEnvironments() {
    ImmutableList<Environment> expected = ImmutableList.of(TestEntities.TEST_ENVIRONMENT);
    when(environmentDao.listEnvironments(isA(ListEnvironmentsRequest.class)))
      .thenReturn(expected);
    
    ImmutableList<Environment> result = environmentService.listEnvironments(
      ListEnvironmentsRequest.builder()
        .setProjectId(TestEntities.TEST_PROJECT_ID)
        .build());
    
    assertThat(result).isEqualTo(expected);
  }
  
  @Test
  public void deletesSecretsAndEnvironment() {
    ImmutableList<Secret> secrets = createSecrets(5);
    when(secretService.listSecrets(isA(ListSecretsRequest.class))).thenReturn(secrets);
    when(environmentDao.getEnvironment(isA(GetEnvironmentRequest.class)))
      .thenReturn(Optional.of(TestEntities.TEST_ENVIRONMENT));
    
    environmentService.deleteEnvironment(DeleteEnvironmentRequest.builder()
      .setProjectId(TestEntities.TEST_PROJECT_ID)
      .setEnvironmentId(TestEntities.TEST_ENVIRONMENT_ID)
      .build());
    
    verify(policyService).deleteEnvironmentPolicy(isA(DeleteEnvironmentPolicyRequest.class));
    verify(environmentDao).deleteEnvironment(isA(DeleteEnvironmentRequest.class));
    for (Secret secret : secrets) {
      verify(secretService).deleteSecret(DeleteSecretRequest.builder()
        .setProjectId(TestEntities.TEST_PROJECT_ID)
        .setEnvironmentId(TestEntities.TEST_ENVIRONMENT_ID)
        .setSecretId(secret.getSecretId())
        .build());
    }
  }
  
  private static ImmutableList<Secret> createSecrets(int count) {
    ImmutableList.Builder<Secret> builder = ImmutableList.builder();
    for (int i = 0; i < count; i++) {
      builder.add(Secret.builder()
        .setSecretId("secret-" + i)
        .build());
    }
    
    return builder.build();
  }
}
