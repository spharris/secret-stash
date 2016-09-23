package io.github.spharris.stash.service;

import static com.google.common.truth.Truth.assertThat;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.amazonaws.services.s3.AmazonS3;
import com.google.common.collect.ImmutableList;
import com.google.inject.Guice;

import io.github.spharris.stash.Project;
import io.github.spharris.stash.service.request.CreateProjectRequest;
import io.github.spharris.stash.service.request.GetProjectRequest;
import io.github.spharris.stash.service.request.ListProjectsRequest;
import io.github.spharris.stash.service.testing.TestEntities;
import io.github.spharris.stash.service.testing.TestModule;
import io.github.spharris.stash.service.utils.JsonUtil;
import io.github.spharris.stash.service.utils.ObjectNameUtil;

@RunWith(JUnit4.class)
public class ProjectServiceImplTest {

  @Inject JsonUtil json;
  @Inject AmazonS3 client;
  @Inject ProjectService projectService;
  
  @Before
  public void createInjector() {
    Guice.createInjector(new TestModule()).injectMembers(this);
  }
  
  @Test
  public void putsData() {
    projectService.createProject(CreateProjectRequest.builder()
      .setProject(TestEntities.TEST_PROJECT)
      .build());
    
    assertThat(client.getObject(
      TestEntities.TEST_BUCKET, ObjectNameUtil.createS3Path(
        TestEntities.TEST_PROJECT_ID))).isNotNull();
  }
  
  @Test
  public void writesBody() throws Exception {
    projectService.createProject(CreateProjectRequest.builder()
      .setProject(TestEntities.TEST_PROJECT)
      .build());
    
    Project result = json.fromInputStream(client.getObject(TestEntities.TEST_BUCKET,
      ObjectNameUtil.createS3Path(TestEntities.TEST_PROJECT_ID)).getObjectContent(), Project.class);
    
    assertThat(result).isEqualTo(TestEntities.TEST_PROJECT);
  }
  
  @Test
  public void listsWhenJustProjects() {
    int projectCount = 10;
    createProjects(projectCount);
    
    ImmutableList<Project> projects = projectService.listProjects(
      ListProjectsRequest.builder().build());
    
    assertThat(projects).hasSize(projectCount);
    for (int i = 0; i < projectCount; i++) {
      assertThat(projects.get(i).getProjectId()).isEqualTo("project-" + i);
    }
  }
  
  @Test
  public void filtersOutRandomFiles() {
    int projectCount = 10;
    createProjects(projectCount);
    client.putObject(TestEntities.TEST_BUCKET, "derp.js", "data");
    
    ImmutableList<Project> projects = projectService.listProjects(
      ListProjectsRequest.builder().build());
    
    assertThat(projects).hasSize(projectCount);
    for (int i = 0; i < projectCount; i++) {
      assertThat(projects.get(i).getProjectId()).isEqualTo("project-" + i);
    }
  }
  
  @Test
  public void listsWhenMoreThanProjects() {
    int projectCount = 10;
    createProjects(projectCount);
    
    client.putObject(TestEntities.TEST_BUCKET, "project-1/test/", "data");
    client.putObject(TestEntities.TEST_BUCKET, "project-1/test/test-key", "data");
    
    ImmutableList<Project> projects = projectService.listProjects(
      ListProjectsRequest.builder().build());
    
    assertThat(projects).hasSize(projectCount);
    for (int i = 0; i < projectCount; i++) {
      assertThat(projects.get(i).getProjectId()).isEqualTo("project-" + i);
    }
  }
  
  @Test
  public void getsProject() {
    projectService.createProject(CreateProjectRequest.builder()
      .setProject(TestEntities.TEST_PROJECT)
      .build());
    
    assertThat(projectService.getProject(GetProjectRequest.builder()
      .setProjectId(TestEntities.TEST_PROJECT_ID)
      .build())).isEqualTo(TestEntities.TEST_PROJECT);
  }
  
  private void createProjects(int count) {
    for (int i = 0; i < count; i++) {
      projectService.createProject(CreateProjectRequest.builder()
        .setProject(Project.builder()
          .setProjectId("project-" + i)
          .build())
        .build());
    }
  }
}
