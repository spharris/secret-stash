package io.github.spharris.stash.service.db;

import static com.google.common.truth.Truth.assertThat;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.ImmutableList;

import io.github.spharris.stash.Secret;
import io.github.spharris.stash.service.request.CreateEnvironmentRequest;
import io.github.spharris.stash.service.request.CreateProjectRequest;
import io.github.spharris.stash.service.request.CreateSecretRequest;
import io.github.spharris.stash.service.request.DeleteSecretRequest;
import io.github.spharris.stash.service.request.GetSecretRequest;
import io.github.spharris.stash.service.request.ListSecretsRequest;
import io.github.spharris.stash.service.testing.TestEntities;

@RunWith(JUnit4.class)
public class SecretDaoImplTest extends BaseDaoTest {

  @Inject SecretDao secretDao;
  @Inject EnvironmentDao environmentDao;
  @Inject ProjectDao projectDao;
  
  @Rule public ExpectedException thrown = ExpectedException.none();

  @Before
  public void initializeData() {
    createProject();
    createEnvironment();
  }
  
  public void createProject() {
    projectDao.createProject(CreateProjectRequest.builder()
      .setProject(TestEntities.TEST_PROJECT)
      .build());
  }
  
  public void createEnvironment() {
    environmentDao.createEnvironment(CreateEnvironmentRequest.builder()
      .setProjectId(TestEntities.TEST_PROJECT_ID)
      .setEnvironment(TestEntities.TEST_ENVIRONMENT)
      .build());
  }
  
  @Test
  public void insertsSecret() throws Exception {
    secretDao.createSecret(CreateSecretRequest.builder()
      .setProjectId(TestEntities.TEST_PROJECT_ID)
      .setEnvironmentId(TestEntities.TEST_ENVIRONMENT_ID)
      .setSecret(TestEntities.TEST_SECRET)
      .build());
    
    Statement statement = dbService.getConnection().createStatement();
    ResultSet rs = statement.executeQuery("select * from secret;");
    
    assertThat(rs.isBeforeFirst()).isTrue();
    
    rs.next();
    assertThat(rs.getString("project_id")).isEqualTo(TestEntities.TEST_PROJECT_ID);
    assertThat(rs.getString("environment_id")).isEqualTo(TestEntities.TEST_ENVIRONMENT_ID);
    assertThat(rs.getString("secret_id")).isEqualTo(TestEntities.TEST_SECRET_ID);
    assertThat(rs.getString("description")).isEqualTo(TestEntities.TEST_SECRET_DESCRIPTION);
  }
  
  
  @Test
  public void errorOnDuplicateSecret() throws Exception {
    secretDao.createSecret(CreateSecretRequest.builder()
      .setProjectId(TestEntities.TEST_PROJECT_ID)
      .setEnvironmentId(TestEntities.TEST_ENVIRONMENT_ID)
      .setSecret(TestEntities.TEST_SECRET)
      .build());
    
    thrown.expect(RuntimeException.class);
    
    secretDao.createSecret(CreateSecretRequest.builder()
      .setProjectId(TestEntities.TEST_PROJECT_ID)
      .setEnvironmentId(TestEntities.TEST_ENVIRONMENT_ID)
      .setSecret(TestEntities.TEST_SECRET)
      .build());
  }
  
  @Test
  public void getsSecret() throws Exception {
    secretDao.createSecret(CreateSecretRequest.builder()
      .setProjectId(TestEntities.TEST_PROJECT_ID)
      .setEnvironmentId(TestEntities.TEST_ENVIRONMENT_ID)
      .setSecret(TestEntities.TEST_SECRET)
      .build());
    
    Secret result = secretDao.getSecret(GetSecretRequest.builder()
      .setProjectId(TestEntities.TEST_PROJECT_ID)
      .setEnvironmentId(TestEntities.TEST_ENVIRONMENT_ID)
      .setSecretId(TestEntities.TEST_SECRET_ID)
      .build()).get();
    
    // Sqlite DB does NOT contain the actual secret value
    assertThat(result).isEqualTo(TestEntities.TEST_SECRET.toBuilder()
      .setSecretValue(null)
      .build());
  }
  
  
  @Test
  public void getNullDescription() throws Exception {
    Secret expected = Secret.builder()
        .setSecretId(TestEntities.TEST_SECRET_ID)
        .build();
    
    secretDao.createSecret(CreateSecretRequest.builder()
      .setProjectId(TestEntities.TEST_PROJECT_ID)
      .setEnvironmentId(TestEntities.TEST_ENVIRONMENT_ID)
      .setSecret(expected)
      .build());
    
    Secret result = secretDao.getSecret(GetSecretRequest.builder()
      .setProjectId(TestEntities.TEST_PROJECT_ID)
      .setEnvironmentId(TestEntities.TEST_ENVIRONMENT_ID)
      .setSecretId(TestEntities.TEST_SECRET_ID)
      .build()).get();
    
    assertThat(result).isEqualTo(expected);
  }
  
  @Test
  public void returnsOptionalAbsent() throws Exception {    
    Optional<Secret> result = secretDao.getSecret(GetSecretRequest.builder()
      .setProjectId(TestEntities.TEST_PROJECT_ID)
      .setEnvironmentId(TestEntities.TEST_ENVIRONMENT_ID)
      .setSecretId(TestEntities.TEST_SECRET_ID)
      .build());
    
    assertThat(result.isPresent()).isFalse();
  }
  
  @Test
  public void listsSecrets() throws Exception {
    ImmutableList<Secret> expected = ImmutableList.copyOf(IntStream.range(0, 2)
      .mapToObj((value) ->  {
        return secretDao.createSecret(CreateSecretRequest.builder()
          .setProjectId(TestEntities.TEST_PROJECT_ID)
          .setEnvironmentId(TestEntities.TEST_ENVIRONMENT_ID)
          .setSecret(Secret.builder()
            .setSecretId("secret-" + value)
            .setDescription("description-" + value)
            .build())
          .build());
      })
      .collect(Collectors.toList()));
           
    ImmutableList<Secret> result = secretDao.listSecrets(
      ListSecretsRequest.builder()
        .setProjectId(TestEntities.TEST_PROJECT_ID)
        .setEnvironmentId(TestEntities.TEST_ENVIRONMENT_ID)
        .build());
    
    assertThat(result).isEqualTo(expected);
  }
  
  @Test
  public void returnsEmptyList() throws Exception {
    ImmutableList<Secret> expected = ImmutableList.of();
    ImmutableList<Secret> result = secretDao.listSecrets(
      ListSecretsRequest.builder()
        .setProjectId(TestEntities.TEST_PROJECT_ID)
        .setEnvironmentId(TestEntities.TEST_ENVIRONMENT_ID)
        .build());
    
    assertThat(result).isEqualTo(expected);
  }
  
  @Test
  public void deletesSecret() throws Exception {
    secretDao.createSecret(CreateSecretRequest.builder()
      .setProjectId(TestEntities.TEST_PROJECT_ID)
      .setEnvironmentId(TestEntities.TEST_ENVIRONMENT_ID)
      .setSecret(TestEntities.TEST_SECRET)
      .build());
    
    secretDao.deleteSecret(DeleteSecretRequest.builder()
      .setProjectId(TestEntities.TEST_PROJECT_ID)
      .setEnvironmentId(TestEntities.TEST_ENVIRONMENT_ID)
      .setSecretId(TestEntities.TEST_SECRET_ID)
      .build());
    
    Optional<Secret> result = secretDao.getSecret(GetSecretRequest.builder()
      .setProjectId(TestEntities.TEST_PROJECT_ID)
      .setEnvironmentId(TestEntities.TEST_ENVIRONMENT_ID)
      .setSecretId(TestEntities.TEST_SECRET_ID)
      .build());
    
    assertThat(result.isPresent()).isFalse();
  }
  
  @Test
  public void noErrorOnNonExistantSecretDelete() throws Exception {
    secretDao.deleteSecret(DeleteSecretRequest.builder()
      .setProjectId(TestEntities.TEST_PROJECT_ID)
      .setEnvironmentId(TestEntities.TEST_ENVIRONMENT_ID)
      .setSecretId(TestEntities.TEST_SECRET_ID)
      .build());
    
    Optional<Secret> result = secretDao.getSecret(GetSecretRequest.builder()
      .setProjectId(TestEntities.TEST_PROJECT_ID)
      .setEnvironmentId(TestEntities.TEST_ENVIRONMENT_ID)
      .setSecretId(TestEntities.TEST_SECRET_ID)
      .build());
    
    assertThat(result.isPresent()).isFalse();
  }
}
