package io.github.spharris.stash.service;

import javax.inject.Inject;

import com.amazonaws.services.s3.AmazonS3;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;

import io.github.spharris.stash.Project;
import io.github.spharris.stash.service.Annotations.BucketOfSecrets;
import io.github.spharris.stash.service.request.CreateProjectRequest;
import io.github.spharris.stash.service.request.DeleteProjectRequest;
import io.github.spharris.stash.service.request.GetProjectRequest;
import io.github.spharris.stash.service.request.ListProjectsRequest;
import io.github.spharris.stash.service.request.UpdateProjectRequest;

public class ProjectServiceImpl implements ProjectService {

  private final String bucketName;
  private final AmazonS3 s3client;
  private final ObjectMapper mapper;
  
  @Inject
  ProjectServiceImpl(@BucketOfSecrets String bucketName, AmazonS3 s3client, ObjectMapper mapper) {
    this.bucketName = bucketName;
    this.s3client = s3client;
    this.mapper = mapper;
  }
  
  @Override
  public ImmutableList<Project> listProjects(ListProjectsRequest request) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Project createProject(CreateProjectRequest request) {
    Project project = request.getProject();
    
    try {
      s3client.putObject(bucketName, ObjectNameUtil.createS3Path(project.getProjectId()),
        mapper.writeValueAsString(project));
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
    
    return project;
  }

  @Override
  public Project getProject(GetProjectRequest request) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Project updateProject(UpdateProjectRequest request) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void deleteProject(DeleteProjectRequest request) {
    // TODO Auto-generated method stub

  }
}
