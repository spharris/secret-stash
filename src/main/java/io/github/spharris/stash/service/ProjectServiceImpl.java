package io.github.spharris.stash.service;

import java.io.IOException;
import java.util.stream.Collectors;

import javax.inject.Inject;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3Object;
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
    ListObjectsV2Result result = s3client.listObjectsV2(bucketName);
    
    return ImmutableList.copyOf(result.getObjectSummaries().parallelStream()
      .map((summary) -> { return summary.getKey(); })
      .filter((key) -> { return key.split("/").length == 1; })
      .filter((key) -> { return key.endsWith("/"); })
      .map((key) -> { 
        return Project.builder()
          .setProjectId(key.substring(0, key.length() - 1))
          .build();
      })
      .collect(Collectors.toList()));
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
    S3Object result = s3client.getObject(bucketName, ObjectNameUtil.createS3Path(
      request.getProjectId()));
    
    try {
      return mapper.readValue(result.getObjectContent(), Project.class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
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
