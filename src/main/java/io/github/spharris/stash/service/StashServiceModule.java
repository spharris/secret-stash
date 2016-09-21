package io.github.spharris.stash.service;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.google.inject.AbstractModule;
import com.google.inject.Key;

import io.github.spharris.stash.service.Annotations.BucketOfSecrets;

/**
 * secret-stash backend services
 */
public class StashServiceModule extends AbstractModule {
  
  @Override
  protected void configure() {
    
    bind(AmazonS3.class).toInstance(AmazonS3ClientBuilder.standard()
      .withRegion(Regions.US_WEST_2)
      .build());

    bind(Key.get(String.class, BucketOfSecrets.class)).toInstance("spharris.secrets");
    bind(ProjectService.class).to(ProjectServiceImpl.class);
    bind(EnvironmentService.class).to(EnvironmentServiceImpl.class);
    bind(SecretService.class).to(SecretServiceImpl.class);
    
    bind(ObjectMapper.class).toInstance(new ObjectMapper()
      .setSerializationInclusion(Include.NON_NULL)
      .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
      .registerModule(new GuavaModule()));
  }
}
