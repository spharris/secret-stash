package io.github.spharris.stash.service.db;

import static com.google.common.truth.Truth.assertThat;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.inject.Inject;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.ImmutableList;

import io.github.spharris.stash.Environment;
import io.github.spharris.stash.Project;
import io.github.spharris.stash.Secret;
import io.github.spharris.stash.service.request.CreateEnvironmentRequest;
import io.github.spharris.stash.service.request.CreateProjectRequest;
import io.github.spharris.stash.service.request.CreateSecretRequest;
import io.github.spharris.stash.service.request.DeleteProjectRequest;
import io.github.spharris.stash.service.request.GetEnvironmentRequest;
import io.github.spharris.stash.service.request.GetProjectRequest;
import io.github.spharris.stash.service.request.GetSecretRequest;
import io.github.spharris.stash.service.request.ListProjectsRequest;
import io.github.spharris.stash.service.testing.TestEntities;

@RunWith(JUnit4.class)
public class ProjectDaoImplTest extends BaseDaoTest {

  @Inject ProjectDao projectDao;
  @Inject EnvironmentDao environmentDao;
  @Inject SecretDao secretDao;
  
  @Rule public ExpectedException thrown = ExpectedException.none();

  @Test
  public void insertsProject() throws Exception {
    projectDao.createProject(CreateProjectRequest.builder()
      .setProject(TestEntities.TEST_PROJECT)
      .build());
    
    Statement statement = dbService.getConnection().createStatement();
    ResultSet rs = statement.executeQuery("select * from project;");
    
    assertThat(rs.isBeforeFirst()).isTrue();
    
    rs.next();
    assertThat(rs.getString("project_id")).isEqualTo(TestEntities.TEST_PROJECT_ID);
    assertThat(rs.getString("description")).isEqualTo(TestEntities.TEST_PROJECT_DESCRIPTION);
  }
  
  @Test
  public void errorOnDuplicateProject() throws Exception {
    projectDao.createProject(CreateProjectRequest.builder()
      .setProject(TestEntities.TEST_PROJECT)
      .build());
    
    thrown.expect(RuntimeException.class);
    
    projectDao.createProject(CreateProjectRequest.builder()
      .setProject(TestEntities.TEST_PROJECT)
      .build());
  }
  
  @Test
  public void getsProject() throws Exception {
    projectDao.createProject(CreateProjectRequest.builder()
      .setProject(TestEntities.TEST_PROJECT)
      .build());
    
    Project result = projectDao.getProject(GetProjectRequest.builder()
      .setProjectId(TestEntities.TEST_PROJECT_ID)
      .build()).get();
    
    assertThat(result).isEqualTo(TestEntities.TEST_PROJECT);
  }
  
  @Test
  public void returnsOptionalAbsent() throws Exception {    
    Optional<Project> result = projectDao.getProject(GetProjectRequest.builder()
      .setProjectId(TestEntities.TEST_PROJECT_ID)
      .build());
    
    assertThat(result.isPresent()).isFalse();
  }
  
  @Test
  public void listsProjects() throws Exception {
    ImmutableList<Project> expected = ImmutableList.copyOf(IntStream.range(0, 10)
      .mapToObj((value) ->  {
        return projectDao.createProject(CreateProjectRequest.builder()
          .setProject(Project.builder()
            .setProjectId("project-" + value)
            .setDescription("description-" + value)
            .build())
          .build());
      })
      .collect(Collectors.toList()));
           
    ImmutableList<Project> result = projectDao.listProjects(ListProjectsRequest.builder().build());
    
    assertThat(result).isEqualTo(expected);
  }
  
  @Test
  public void returnsEmptyList() throws Exception {
    ImmutableList<Project> expected = ImmutableList.of();
    ImmutableList<Project> result = projectDao.listProjects(ListProjectsRequest.builder().build());
    
    assertThat(result).isEqualTo(expected);
  }
  
  @Test
  public void deletesProject() throws Exception {
    projectDao.createProject(CreateProjectRequest.builder()
      .setProject(TestEntities.TEST_PROJECT)
      .build());
    
    projectDao.deleteProject(DeleteProjectRequest.builder()
      .setProjectId(TestEntities.TEST_PROJECT_ID)
      .build());
    
    Optional<Project> result = projectDao.getProject(GetProjectRequest.builder()
      .setProjectId(TestEntities.TEST_PROJECT_ID)
      .build());
    
    assertThat(result.isPresent()).isFalse();
  }
  
  @Test
  public void noErrorOnNonExistantProjectDelete() throws Exception {
    projectDao.deleteProject(DeleteProjectRequest.builder()
      .setProjectId(TestEntities.TEST_PROJECT_ID)
      .build());
    
    Optional<Project> result = projectDao.getProject(GetProjectRequest.builder()
      .setProjectId(TestEntities.TEST_PROJECT_ID)
      .build());
    
    assertThat(result.isPresent()).isFalse();
  }
  
  @Test
  public void cascadeDeletes() throws Exception {
    projectDao.createProject(CreateProjectRequest.builder()
      .setProject(TestEntities.TEST_PROJECT)
      .build());
    
    environmentDao.createEnvironment(CreateEnvironmentRequest.builder()
      .setProjectId(TestEntities.TEST_PROJECT_ID)
      .setEnvironment(TestEntities.TEST_ENVIRONMENT)
      .build());

    secretDao.createSecret(CreateSecretRequest.builder()
      .setProjectId(TestEntities.TEST_PROJECT_ID)
      .setEnvironmentId(TestEntities.TEST_ENVIRONMENT_ID)
      .setSecret(TestEntities.TEST_SECRET)
      .build());
    
    projectDao.deleteProject(DeleteProjectRequest.builder()
      .setProjectId(TestEntities.TEST_PROJECT_ID)
      .build());
    
    Optional<Secret> secret = secretDao.getSecret(GetSecretRequest.builder()
      .setProjectId(TestEntities.TEST_PROJECT_ID)
      .setEnvironmentId(TestEntities.TEST_ENVIRONMENT_ID)
      .setSecretId(TestEntities.TEST_SECRET_ID)
      .build());
    
    Optional<Environment> environment = environmentDao.getEnvironment(
      GetEnvironmentRequest.builder()
        .setProjectId(TestEntities.TEST_PROJECT_ID)
        .setEnvironmentId(TestEntities.TEST_ENVIRONMENT_ID)
        .build());
    
    assertThat(secret.isPresent()).isFalse();
    assertThat(environment.isPresent()).isFalse();
  }
}
