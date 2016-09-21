package io.github.spharris.stash.service.testing;

import com.amazonaws.services.s3.AmazonS3;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.google.inject.AbstractModule;
import com.google.inject.Key;

import io.github.spharris.stash.service.Annotations.BucketOfSecrets;
import io.github.spharris.stash.service.Annotations.PolicyPrefix;
import io.github.spharris.stash.service.EnvironmentService;
import io.github.spharris.stash.service.EnvironmentServiceImpl;
import io.github.spharris.stash.service.PolicyService;
import io.github.spharris.stash.service.PolicyServiceImpl;
import io.github.spharris.stash.service.ProjectService;
import io.github.spharris.stash.service.ProjectServiceImpl;
import io.github.spharris.stash.service.SecretService;
import io.github.spharris.stash.service.SecretServiceImpl;

/**
 * Bindings to be used for tests 
 */
public class TestModule extends AbstractModule {
  
  @Override
  protected void configure() {
    bind(Key.get(String.class, BucketOfSecrets.class)).toInstance(TestEntities.TEST_BUCKET);
    bind(Key.get(String.class, PolicyPrefix.class)).toInstance(TestEntities.TEST_POLICY_PREFIX);
    
    bind(ProjectService.class).to(ProjectServiceImpl.class);
    bind(EnvironmentService.class).to(EnvironmentServiceImpl.class);
    bind(SecretService.class).to(SecretServiceImpl.class);
    bind(PolicyService.class).to(PolicyServiceImpl.class);
    
    bind(AmazonS3.class).toInstance(new FakeS3Client());
    bind(ObjectMapper.class).toInstance(new ObjectMapper()
      .setSerializationInclusion(Include.NON_NULL)
      .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
      .registerModule(new GuavaModule()));
  }
}
