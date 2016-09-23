package io.github.spharris.stash.service;

import java.util.stream.Collectors;

import javax.inject.Inject;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3Object;
import com.google.common.collect.ImmutableList;

import io.github.spharris.stash.Project;
import io.github.spharris.stash.service.Annotations.BucketOfSecrets;
import io.github.spharris.stash.service.request.CreateProjectRequest;
import io.github.spharris.stash.service.request.DeleteProjectRequest;
import io.github.spharris.stash.service.request.GetProjectRequest;
import io.github.spharris.stash.service.request.ListProjectsRequest;
import io.github.spharris.stash.service.request.UpdateProjectRequest;
import io.github.spharris.stash.service.utils.JsonUtil;
import io.github.spharris.stash.service.utils.ObjectNameUtil;

public class ProjectServiceImpl implements ProjectService {

  private final String bucketName;
  private final AmazonS3 s3client;
  private final JsonUtil json;
  
  @Inject
  ProjectServiceImpl(@BucketOfSecrets String bucketName, AmazonS3 s3client, JsonUtil json) {
    this.bucketName = bucketName;
    this.s3client = s3client;
    this.json = json;
  }
  
  @Override
  public ImmutableList<Project> listProjects(ListProjectsRequest request) {
    ListObjectsV2Result result = s3client.listObjectsV2(bucketName);
    
    return ImmutableList.copyOf(result.getObjectSummaries().parallelStream()
      .map((summary) -> { return summary.getKey(); })
      .filter(ObjectNameUtil.isProjectKey())
      .map((key) -> { 
        return Project.builder()
          .setProjectId(ObjectNameUtil.extractProjectId(key))
          .build();
      })
      .collect(Collectors.toList()));
  }

  @Override
  public Project createProject(CreateProjectRequest request) {
    Project project = request.getProject();
    s3client.putObject(bucketName, ObjectNameUtil.createS3Path(project.getProjectId()),
      json.toString(project));
    
    return project;
  }

  @Override
  public Project getProject(GetProjectRequest request) {
    S3Object result = s3client.getObject(bucketName, ObjectNameUtil.createS3Path(
      request.getProjectId()));

    return json.fromInputStream(result.getObjectContent(), Project.class);
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
