package io.github.spharris.stash.service;

import com.amazonaws.auth.policy.Action;
import com.amazonaws.auth.policy.actions.IdentityManagementActions;
import com.amazonaws.auth.policy.actions.S3Actions;
import com.amazonaws.services.identitymanagement.AmazonIdentityManagement;
import com.amazonaws.services.identitymanagement.AmazonIdentityManagementClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.google.inject.AbstractModule;
import com.google.inject.Key;

import io.github.spharris.stash.service.Annotations.BucketOfSecrets;
import io.github.spharris.stash.service.Annotations.PolicyPath;
import io.github.spharris.stash.service.Annotations.PolicyPrefix;
import io.github.spharris.stash.service.aws.ActionDeserializer;
import io.github.spharris.stash.service.aws.ActionSerializer;
import io.github.spharris.stash.service.db.EnvironmentDao;
import io.github.spharris.stash.service.db.ProjectDao;
import io.github.spharris.stash.service.db.SecretDao;

/**
 * secret-stash backend services
 */
public class StashServiceModule extends AbstractModule {
  
  @Override
  protected void configure() {
    
    requireBinding(ProjectDao.class);
    requireBinding(EnvironmentDao.class);
    requireBinding(SecretDao.class);
    
    bind(AmazonS3.class).toInstance(AmazonS3ClientBuilder.standard()
      .build());
    
    bind(AmazonIdentityManagement.class).toInstance(
      AmazonIdentityManagementClientBuilder.standard().build());

    bind(Key.get(String.class, BucketOfSecrets.class)).toInstance("spharris.secrets");
    bind(Key.get(String.class, PolicyPath.class)).toInstance("/secret-stash/");
    bind(Key.get(String.class, PolicyPrefix.class)).toInstance("Stash+");
    
    bind(ProjectService.class).to(ProjectServiceImpl.class);
    bind(EnvironmentService.class).to(EnvironmentServiceImpl.class);
    bind(SecretService.class).to(SecretServiceImpl.class);
    bind(PolicyService.class).to(PolicyServiceImpl.class);
    
     bind(ObjectMapper.class).toInstance(new ObjectMapper()
      .setSerializationInclusion(Include.NON_NULL)
      .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
      .registerModule(new GuavaModule())
      .registerModule(new SimpleModule()
        .addSerializer(Action.class, new ActionSerializer())
        .addDeserializer(S3Actions.class, new ActionDeserializer<>(S3Actions.class))
        .addDeserializer(IdentityManagementActions.class, new ActionDeserializer<>(
            IdentityManagementActions.class))));
  }
}
