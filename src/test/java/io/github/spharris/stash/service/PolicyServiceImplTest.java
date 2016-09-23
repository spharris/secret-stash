package io.github.spharris.stash.service;

import static com.google.common.truth.Truth.assertThat;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.amazonaws.auth.policy.Statement.Effect;
import com.amazonaws.auth.policy.actions.S3Actions;
import com.amazonaws.services.s3.AmazonS3;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Iterables;
import com.google.inject.Guice;

import io.github.spharris.stash.service.aws.Policy;
import io.github.spharris.stash.service.aws.Statement;
import io.github.spharris.stash.service.request.UpdatePolicyRequest;
import io.github.spharris.stash.service.testing.TestEntities;
import io.github.spharris.stash.service.testing.TestModule;
import io.github.spharris.stash.service.utils.JsonUtil;
import io.github.spharris.stash.service.utils.ObjectNameUtil;

@RunWith(JUnit4.class)
public class PolicyServiceImplTest {

  @Inject AmazonS3 client;
  @Inject PolicyService policyService;
  @Inject JsonUtil json;
  
  @Before 
  public void createInjector() {
    Guice.createInjector(new TestModule()).injectMembers(this);
  }
  
  @Test
  public void createsPolicyIfNonExistant() {
    policyService.updateEnvironmentPolicy(UpdatePolicyRequest.builder()
      .setProjectId(TestEntities.TEST_PROJECT_ID)
      .setEnvironment(TestEntities.TEST_ENVIRONMENT)
      .build());
    
    Policy result = json.fromString(
      client.getBucketPolicy(TestEntities.TEST_BUCKET).getPolicyText(), Policy.class);
    
    assertThat(result.getStatements()).hasSize(1);
    assertThat(result.getId()).isEqualTo(
      TestEntities.TEST_POLICY_PREFIX + ":" + PolicyService.POLICY_ID);
  }
  
  @Test
  public void addsStatementForNewEnvironment() {    
    Policy result = policyService.updateEnvironmentPolicy(UpdatePolicyRequest.builder()
      .setProjectId(TestEntities.TEST_PROJECT_ID)
      .setEnvironment(TestEntities.TEST_ENVIRONMENT)
      .build());
    
    Policy expected = Policy.builder()
        .setId(TestEntities.TEST_POLICY_PREFIX + ":" + PolicyService.POLICY_ID)
        .setStatements(Statement.builder()
          .setId(TestEntities.TEST_POLICY_PREFIX + ":" + ObjectNameUtil.createS3Path(
             TestEntities.TEST_PROJECT_ID, TestEntities.TEST_ENVIRONMENT_ID))
          .setEffect(Effect.Allow)
          .setActions(S3Actions.GetObject)
          .setResources("arn:aws:s3:::" 
              + TestEntities.TEST_PROJECT_ID + "/" 
              + TestEntities.TEST_ENVIRONMENT_ID + "/*")
          .setPrincipals(ImmutableMultimap.<String, String>builder()
            .putAll("AWS",
              Iterables.concat(TestEntities.TEST_ACL.getGroups(), TestEntities.TEST_ACL.getRoles()))
            .build())
          .build())
        .build();
    
    assertThat(result).isEqualTo(expected);
  }
}
