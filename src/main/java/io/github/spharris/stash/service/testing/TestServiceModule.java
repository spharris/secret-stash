package io.github.spharris.stash.service.testing;

import com.amazonaws.auth.policy.Action;
import com.amazonaws.auth.policy.actions.IdentityManagementActions;
import com.amazonaws.auth.policy.actions.S3Actions;
import com.amazonaws.services.identitymanagement.AmazonIdentityManagement;
import com.amazonaws.services.s3.AmazonS3;
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
import io.github.spharris.stash.service.EnvironmentService;
import io.github.spharris.stash.service.EnvironmentServiceImpl;
import io.github.spharris.stash.service.PolicyService;
import io.github.spharris.stash.service.PolicyServiceImpl;
import io.github.spharris.stash.service.ProjectService;
import io.github.spharris.stash.service.ProjectServiceImpl;
import io.github.spharris.stash.service.SecretService;
import io.github.spharris.stash.service.SecretServiceImpl;
import io.github.spharris.stash.service.aws.ActionDeserializer;
import io.github.spharris.stash.service.aws.ActionSerializer;
import io.github.spharris.stash.service.utils.JsonUtil;

/**
 * Bindings to be used for tests 
 */
public class TestServiceModule extends AbstractModule {
  
  @Override
  protected void configure() {
    bind(Key.get(String.class, BucketOfSecrets.class)).toInstance(TestEntities.TEST_BUCKET);
    bind(Key.get(String.class, PolicyPath.class)).toInstance(TestEntities.TEST_POLICY_PATH);
    bind(Key.get(String.class, PolicyPrefix.class)).toInstance(TestEntities.TEST_POLICY_PREFIX);
    
    bind(ProjectService.class).to(ProjectServiceImpl.class);
    bind(EnvironmentService.class).to(EnvironmentServiceImpl.class);
    bind(SecretService.class).to(SecretServiceImpl.class);
    bind(PolicyService.class).to(PolicyServiceImpl.class);
    
    bind(AmazonS3.class).toInstance(new FakeS3Client());
    bind(AmazonIdentityManagement.class).toInstance(new FakeIamClient());
    bind(ObjectMapper.class).toInstance(new ObjectMapper()
      .setSerializationInclusion(Include.NON_NULL)
      .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
      .registerModule(new GuavaModule())
      .registerModule(new SimpleModule()
        .addSerializer(Action.class, new ActionSerializer())
        .addDeserializer(S3Actions.class, new ActionDeserializer<>(S3Actions.class))
        .addDeserializer(IdentityManagementActions.class, new ActionDeserializer<>(
            IdentityManagementActions.class))));
    
    bind(JsonUtil.class);
  }
}
