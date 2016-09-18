package io.github.spharris.stash.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.amazonaws.services.s3.AmazonS3;

import io.github.spharris.stash.Project;

@RunWith(MockitoJUnitRunner.class)
public class ProjectServiceImplTest {

  final static String TEST_BUCKET = "test-bucket";
  final static String TEST_PROJECT_ID = "test-project-id"; 
  final static String TEST_PROJECT_DESCRIPTION = "test-project-description";
  
  @Mock AmazonS3 s3Client;
  
  ProjectService projectService;
  
  @Before
  public void createInjector() {
    projectService = new ProjectServiceImpl(TEST_BUCKET, s3Client);
  }
  
  @Test
  public void sendsPutObjectRequest() {
    Project project = Project.builder()
        .setProjectId(TEST_PROJECT_ID)
        .setDescription(TEST_PROJECT_DESCRIPTION)
        .build();
  }
}
