package io.github.spharris.stash.service;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.ImmutableList;

import io.github.spharris.stash.Environment;
import io.github.spharris.stash.Project;
import io.github.spharris.stash.service.db.ProjectDao;
import io.github.spharris.stash.service.request.CreateProjectRequest;
import io.github.spharris.stash.service.request.DeleteEnvironmentRequest;
import io.github.spharris.stash.service.request.DeleteProjectRequest;
import io.github.spharris.stash.service.request.GetProjectRequest;
import io.github.spharris.stash.service.request.ListEnvironmentsRequest;
import io.github.spharris.stash.service.request.ListProjectsRequest;
import io.github.spharris.stash.service.testing.TestEntities;

@RunWith(MockitoJUnitRunner.class)
public class ProjectServiceImplTest {

  @Mock ProjectDao projectDao;
  @Mock EnvironmentService environmentService;
  
  ProjectService projectService;
  
  @Before
  public void initializeService() {
    projectService = new ProjectServiceImpl(projectDao, environmentService);
  }
  
  @Test
  public void putsData() {
    when(projectDao.createProject(isA(CreateProjectRequest.class)))
      .thenReturn(TestEntities.TEST_PROJECT);

    Project result = projectService.createProject(CreateProjectRequest.builder()
      .setProject(TestEntities.TEST_PROJECT)
      .build());
    
    assertThat(result).isEqualTo(TestEntities.TEST_PROJECT);
  }
  
    
  @Test
  public void listsProjects() {
    ImmutableList<Project> expected = createProjects(10);
    when(projectDao.listProjects(isA(ListProjectsRequest.class))).thenReturn(expected);
    
    ImmutableList<Project> result = projectService.listProjects(
      ListProjectsRequest.builder().build());
    
    assertThat(result).isEqualTo(expected);
  }
  
  @Test
  public void getsProject() {
    when(projectDao.getProject(isA(GetProjectRequest.class)))
      .thenReturn(Optional.of(TestEntities.TEST_PROJECT));
    
    Optional<Project> result = projectService.getProject(GetProjectRequest.builder()
      .setProjectId(TestEntities.TEST_PROJECT_ID)
      .build());
    
    assertThat(result).isEqualTo(Optional.of(TestEntities.TEST_PROJECT));
  }
  
  @Test
  public void deletesAllEnvironments() {
    ImmutableList<Environment> environments = createEnvironments(5);
    when(environmentService.listEnvironments(isA(ListEnvironmentsRequest.class)))
      .thenReturn(environments);
    
    projectService.deleteProject(DeleteProjectRequest.builder()
      .setProjectId(TestEntities.TEST_PROJECT_ID)
      .build());
    
    for (Environment e : environments) {
      verify(environmentService).deleteEnvironment(DeleteEnvironmentRequest.builder()
        .setProjectId(TestEntities.TEST_PROJECT_ID)
        .setEnvironmentId(e.getEnvironmentId())
        .build());
    }
  }
  
  @Test
  public void deletesProject() {
    when(environmentService.listEnvironments(isA(ListEnvironmentsRequest.class)))
      .thenReturn(ImmutableList.of());
    
    projectService.deleteProject(DeleteProjectRequest.builder()
      .setProjectId(TestEntities.TEST_PROJECT_ID)
      .build());
    
    verify(projectDao).deleteProject(isA(DeleteProjectRequest.class));
  }
  
  private static ImmutableList<Project> createProjects(int count) {
    ImmutableList.Builder<Project> builder = ImmutableList.builder();
    for (int i = 0; i < count; i++) {
      builder.add(Project.builder()
          .setProjectId("project-" + i)
          .build());
    }
    
    return builder.build();
  }
  
  private static ImmutableList<Environment> createEnvironments(int count) {
    ImmutableList.Builder<Environment> builder = ImmutableList.builder();
    for (int i = 0; i < count; i++) {
      builder.add(Environment.builder()
        .setEnvironmentId("environment-" + i)
        .build());
    }
    
    return builder.build();
  }
}
