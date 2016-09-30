package io.github.spharris.stash.service;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import javax.inject.Inject;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.google.common.collect.ImmutableList;

import io.github.spharris.stash.Secret;
import io.github.spharris.stash.service.Annotations.BucketOfSecrets;
import io.github.spharris.stash.service.db.SecretDao;
import io.github.spharris.stash.service.request.CreateSecretRequest;
import io.github.spharris.stash.service.request.DeleteSecretRequest;
import io.github.spharris.stash.service.request.GetSecretRequest;
import io.github.spharris.stash.service.request.ListSecretsRequest;
import io.github.spharris.stash.service.request.UpdateSecretRequest;
import io.github.spharris.stash.service.utils.ObjectNameUtil;

public class SecretServiceImpl implements SecretService {

  private final SecretDao secretDao;
  private final String bucketName;
  private final AmazonS3 s3client;
  
  @Inject
  SecretServiceImpl(
      @BucketOfSecrets String bucketName, AmazonS3 s3client, SecretDao secretDao) {
    this.bucketName = bucketName;
    this.s3client = s3client;
    this.secretDao = secretDao;
  }
  
  @Override
  public ImmutableList<Secret> listSecrets(ListSecretsRequest request) {
    return secretDao.listSecrets(request);
  }

  @Override
  public Secret createSecret(CreateSecretRequest request) {
    secretDao.createSecret(request);
    
    Secret secret = request.getSecret();
    byte[] secretBytes = new byte[0];
    if (secret.getSecretValue() != null) {
      secretBytes = secret.getSecretValue().getBytes(StandardCharsets.UTF_8);
    }
    
    ObjectMetadata meta = new ObjectMetadata();
    meta.setSSEAlgorithm(ObjectMetadata.AES_256_SERVER_SIDE_ENCRYPTION);
    meta.setContentLength(secretBytes.length);
     
    s3client.putObject(bucketName, ObjectNameUtil.createS3Path(
      request.getProjectId(),
      request.getEnvironmentId(),
      request.getSecret().getSecretId()),
      new ByteArrayInputStream(secretBytes),
      meta);
    
    return secret;
  }

  @Override
  public Optional<Secret> getSecret(GetSecretRequest request) {
    Optional<Secret> optionalSecret = secretDao.getSecret(request);
    if (!request.getIncludeSecretValue() || !optionalSecret.isPresent()) {
      return optionalSecret;
    }
    
    Secret secret = optionalSecret.get();
    String secretValue = s3client.getObjectAsString(bucketName, ObjectNameUtil.createS3Path(
          request.getProjectId(),
          request.getEnvironmentId(),
          request.getSecretId()));
    
    if (secretValue.length() == 0) {
      return Optional.of(secret);
    } else {
      return Optional.of(secret.toBuilder()
        .setSecretValue(secretValue)
        .build());
    }
  }

  @Override
  public Secret updateSecret(UpdateSecretRequest request) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void deleteSecret(DeleteSecretRequest request) {
    secretDao.deleteSecret(request);
    s3client.deleteObject(bucketName, ObjectNameUtil.createS3Path(
      request.getProjectId(), request.getEnvironmentId(), request.getSecretId()));
  }
}
