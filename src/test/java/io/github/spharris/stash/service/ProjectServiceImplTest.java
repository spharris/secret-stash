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

import io.github.spharris.stash.Project;
import io.github.spharris.stash.service.request.CreateProjectRequest;
import io.github.spharris.stash.service.testing.TestEntities;
import io.github.spharris.stash.service.testing.TestModule;

@RunWith(JUnit4.class)
public class ProjectServiceImplTest {

  @Inject ObjectMapper mapper;
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
    
    Project result = mapper.readValue(client.getObject(TestEntities.TEST_BUCKET,
      ObjectNameUtil.createS3Path(TestEntities.TEST_PROJECT_ID)).getObjectContent(), Project.class);
    
    assertThat(result).isEqualTo(TestEntities.TEST_PROJECT);
  }
}
