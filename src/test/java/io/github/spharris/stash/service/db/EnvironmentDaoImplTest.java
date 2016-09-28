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

import io.github.spharris.stash.AccessControlList;
import io.github.spharris.stash.Environment;
import io.github.spharris.stash.service.db.exceptions.UnexpectedSqlException;
import io.github.spharris.stash.service.db.exceptions.UniquenessConstraintException;
import io.github.spharris.stash.service.request.CreateEnvironmentRequest;
import io.github.spharris.stash.service.request.CreateProjectRequest;
import io.github.spharris.stash.service.request.DeleteEnvironmentRequest;
import io.github.spharris.stash.service.request.GetEnvironmentRequest;
import io.github.spharris.stash.service.request.ListEnvironmentsRequest;
import io.github.spharris.stash.service.testing.TestEntities;

@RunWith(JUnit4.class)
public class EnvironmentDaoImplTest extends BaseDaoTest { 

  @Inject EnvironmentDao environmentDao;
  @Inject ProjectDao projectDao;
  
  @Rule public ExpectedException thrown = ExpectedException.none();

  @Before
  public void createProject() {
    projectDao.createProject(CreateProjectRequest.builder()
      .setProject(TestEntities.TEST_PROJECT)
      .build());
  }
  
  @Test
  public void insertsEnvironment() throws Exception {
    environmentDao.createEnvironment(CreateEnvironmentRequest.builder()
      .setProjectId(TestEntities.TEST_PROJECT_ID)
      .setEnvironment(TestEntities.TEST_ENVIRONMENT)
      .build());
    
    Statement statement = dbService.getConnection().createStatement();
    ResultSet rs = statement.executeQuery("select * from environment;");
    
    assertThat(rs.isBeforeFirst()).isTrue();
    
    rs.next();
    assertThat(rs.getString("project_id")).isEqualTo(TestEntities.TEST_PROJECT_ID);
    assertThat(rs.getString("environment_id")).isEqualTo(TestEntities.TEST_ENVIRONMENT_ID);
    assertThat(rs.getString("description")).isEqualTo(TestEntities.TEST_ENVIRONMENT_DESCRIPTION);
    assertThat(rs.getString("roles")).isEqualTo(TestEntities.TEST_ROLE);
    assertThat(rs.getString("groups")).isEqualTo(TestEntities.TEST_GROUP);
  }
  
  @Test
  public void errorOnDuplicateEnvironment() throws Exception {
    environmentDao.createEnvironment(CreateEnvironmentRequest.builder()
      .setProjectId(TestEntities.TEST_PROJECT_ID)
      .setEnvironment(TestEntities.TEST_ENVIRONMENT)
      .build());
    
    thrown.expect(UniquenessConstraintException.class);
    
    environmentDao.createEnvironment(CreateEnvironmentRequest.builder()
      .setProjectId(TestEntities.TEST_PROJECT_ID)
      .setEnvironment(TestEntities.TEST_ENVIRONMENT)
      .build());
  }

  @Test
  public void errorOnNonExistantProject() throws Exception {
    thrown.expect(IllegalStateException.class);

    environmentDao.createEnvironment(CreateEnvironmentRequest.builder()
      .setProjectId("another-project")
      .setEnvironment(TestEntities.TEST_ENVIRONMENT)
      .build());
  }
  
  @Test
  public void getsEnvironment() throws Exception {
    environmentDao.createEnvironment(CreateEnvironmentRequest.builder()
      .setProjectId(TestEntities.TEST_PROJECT_ID)
      .setEnvironment(TestEntities.TEST_ENVIRONMENT)
      .build());
    
    Environment result = environmentDao.getEnvironment(GetEnvironmentRequest.builder()
      .setProjectId(TestEntities.TEST_PROJECT_ID)
      .setEnvironmentId(TestEntities.TEST_ENVIRONMENT_ID)
      .build()).get();
    
    assertThat(result).isEqualTo(TestEntities.TEST_ENVIRONMENT);
  }
  
  @Test
  public void getNullDescription() throws Exception {
    Environment expected = Environment.builder()
        .setEnvironmentId(TestEntities.TEST_ENVIRONMENT_ID)
        .setAcl(TestEntities.TEST_ACL)
        .build();
    
    environmentDao.createEnvironment(CreateEnvironmentRequest.builder()
      .setProjectId(TestEntities.TEST_PROJECT_ID)
      .setEnvironment(expected)
      .build());
    
    Environment result = environmentDao.getEnvironment(GetEnvironmentRequest.builder()
      .setProjectId(TestEntities.TEST_PROJECT_ID)
      .setEnvironmentId(TestEntities.TEST_ENVIRONMENT_ID)
      .build()).get();
    
    assertThat(result).isEqualTo(expected);
  }
  
  @Test
  public void returnsOptionalAbsent() throws Exception {    
    Optional<Environment> result = environmentDao.getEnvironment(GetEnvironmentRequest.builder()
      .setProjectId(TestEntities.TEST_PROJECT_ID)
      .setEnvironmentId(TestEntities.TEST_ENVIRONMENT_ID)
      .build());
    
    assertThat(result.isPresent()).isFalse();
  }
  
  @Test
  public void listsEnvironments() throws Exception {
    ImmutableList<Environment> expected = ImmutableList.copyOf(IntStream.range(0, 2)
      .mapToObj((value) ->  {
        return environmentDao.createEnvironment(CreateEnvironmentRequest.builder()
          .setProjectId(TestEntities.TEST_PROJECT_ID)
          .setEnvironment(Environment.builder()
            .setEnvironmentId("environment-" + value)
            .setDescription("description-" + value)
            .setAcl(TestEntities.TEST_ACL)
            .build())
          .build());
      })
      .collect(Collectors.toList()));
           
    ImmutableList<Environment> result = environmentDao.listEnvironments(
      ListEnvironmentsRequest.builder()
        .setProjectId(TestEntities.TEST_PROJECT_ID)
        .build());
    
    assertThat(result).isEqualTo(expected);
  }
  
  @Test
  public void returnsEmptyList() throws Exception {
    ImmutableList<Environment> expected = ImmutableList.of();
    ImmutableList<Environment> result = environmentDao.listEnvironments(
      ListEnvironmentsRequest.builder()
        .setProjectId(TestEntities.TEST_PROJECT_ID)
        .build());
    
    assertThat(result).isEqualTo(expected);
  }
  
  @Test
  public void deletesEnvironment() throws Exception {
    environmentDao.createEnvironment(CreateEnvironmentRequest.builder()
      .setProjectId(TestEntities.TEST_PROJECT_ID)
      .setEnvironment(TestEntities.TEST_ENVIRONMENT)
      .build());
    
    environmentDao.deleteEnvironment(DeleteEnvironmentRequest.builder()
      .setProjectId(TestEntities.TEST_PROJECT_ID)
      .setEnvironmentId(TestEntities.TEST_ENVIRONMENT_ID)
      .build());
    
    Optional<Environment> result = environmentDao.getEnvironment(GetEnvironmentRequest.builder()
      .setProjectId(TestEntities.TEST_PROJECT_ID)
      .setEnvironmentId(TestEntities.TEST_ENVIRONMENT_ID)
      .build());
    
    assertThat(result.isPresent()).isFalse();
  }
  
  @Test
  public void noErrorOnNonExistantEnvironmentDelete() throws Exception {
    environmentDao.deleteEnvironment(DeleteEnvironmentRequest.builder()
      .setProjectId(TestEntities.TEST_PROJECT_ID)
      .setEnvironmentId(TestEntities.TEST_ENVIRONMENT_ID)
      .build());
    
    Optional<Environment> result = environmentDao.getEnvironment(GetEnvironmentRequest.builder()
      .setProjectId(TestEntities.TEST_PROJECT_ID)
      .setEnvironmentId(TestEntities.TEST_ENVIRONMENT_ID)
      .build());
    
    assertThat(result.isPresent()).isFalse();
  }
  
  @Test
  public void errorWithoutPolicyArn() throws Exception {
    thrown.expect(UnexpectedSqlException.class);
    
    environmentDao.createEnvironment(CreateEnvironmentRequest.builder()
      .setProjectId(TestEntities.TEST_PROJECT_ID)
      .setEnvironment(TestEntities.TEST_ENVIRONMENT.toBuilder()
        .setAcl(AccessControlList.builder().build())
        .build())
      .build());
  }
}
