package io.github.spharris.stash.service;

import javax.inject.Inject;

import com.amazonaws.services.s3.AmazonS3;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.ImmutableList;

import io.github.spharris.stash.Environment;
import io.github.spharris.stash.service.Annotations.BucketOfSecrets;
import io.github.spharris.stash.service.request.CreateEnvironmentRequest;
import io.github.spharris.stash.service.request.DeleteEnvironmentRequest;
import io.github.spharris.stash.service.request.GetEnvironmentRequest;
import io.github.spharris.stash.service.request.ListEnvironmentsRequest;
import io.github.spharris.stash.service.request.UpdateEnvironmentRequest;
import io.github.spharris.stash.service.utils.JsonUtil;
import io.github.spharris.stash.service.utils.ObjectNameUtil;

public class EnvironmentServiceImpl implements EnvironmentService {

  private final String bucketName;
  private final AmazonS3 s3client;
  private final JsonUtil json;
  
  @Inject
  EnvironmentServiceImpl(
      @BucketOfSecrets String bucketName, AmazonS3 s3client, JsonUtil json) {
    this.bucketName = bucketName;
    this.s3client = s3client;
    this.json = json;
  }
  
  @Override
  public ImmutableList<Environment> listEnvironments(ListEnvironmentsRequest request) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Environment createEnvironment(CreateEnvironmentRequest request) {
    // TODO(spharris): What to do if the project doesn't exist already?
    Environment environment = request.getEnvironment();
    
    s3client.putObject(bucketName, ObjectNameUtil.createS3Path(
      request.getProjectId(), environment.getEnvironmentId()),
      json.toString(environment));
    
    return environment;
  }

  @Override
  public Environment getEnvironment(GetEnvironmentRequest request) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Environment updateEnvironment(UpdateEnvironmentRequest request) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void deleteEnvironment(DeleteEnvironmentRequest request) {
    // TODO Auto-generated method stub

  }
}
