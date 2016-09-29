package io.github.spharris.stash.service;

import static com.google.common.truth.Truth.assertThat;

import java.util.Arrays;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.amazonaws.auth.policy.Statement.Effect;
import com.amazonaws.auth.policy.actions.S3Actions;
import com.amazonaws.services.identitymanagement.AmazonIdentityManagement;
import com.amazonaws.services.identitymanagement.model.AttachedPolicy;
import com.amazonaws.services.identitymanagement.model.GetPolicyRequest;
import com.amazonaws.services.identitymanagement.model.GetPolicyResult;
import com.amazonaws.services.identitymanagement.model.ListAttachedRolePoliciesRequest;
import com.amazonaws.services.identitymanagement.model.ListAttachedRolePoliciesResult;
import com.google.common.base.Joiner;
import com.google.inject.Guice;

import io.github.spharris.stash.service.aws.Policy;
import io.github.spharris.stash.service.aws.Statement;
import io.github.spharris.stash.service.request.CreateEnvironmentPolicyRequest;
import io.github.spharris.stash.service.testing.FakeIamClient;
import io.github.spharris.stash.service.testing.TestEntities;
import io.github.spharris.stash.service.testing.TestServiceModule;
import io.github.spharris.stash.service.utils.JsonUtil;
import io.github.spharris.stash.service.utils.ObjectNameUtil;

@RunWith(JUnit4.class)
public class PolicyServiceImplTest {

  @Inject AmazonIdentityManagement client;
  @Inject PolicyService policyService;
  @Inject JsonUtil json;
  
  @Before 
  public void createInjector() {
    Guice.createInjector(new TestServiceModule()).injectMembers(this);
  }
  
  @Test
  public void createsPolicyIfNonExistant() {
    Policy policy = policyService.createEnvironmentPolicy(CreateEnvironmentPolicyRequest.builder()
      .setProjectId(TestEntities.TEST_PROJECT_ID)
      .setEnvironment(TestEntities.TEST_ENVIRONMENT)
      .build());
    
    GetPolicyResult result = client.getPolicy(new GetPolicyRequest()
      .withPolicyArn(policy.getArn()));
    
    Policy expected = Policy.builder()
        .setArn(FakeIamClient.ARN_PREFIX + TestEntities.TEST_POLICY_PATH + getPolicyName())
        .setStatements(Statement.builder()
          .setEffect(Effect.Allow)
          .setActions(S3Actions.GetObject)
          .setResources(ObjectNameUtil.createEnvironmentResource(TestEntities.TEST_BUCKET, 
            TestEntities.TEST_PROJECT_ID, TestEntities.TEST_ENVIRONMENT_ID))
          .build())
        .build();
    
    assertThat(policy).isEqualTo(expected);
    
    assertThat(result.getPolicy()).isNotNull();
    assertThat(result.getPolicy().getPolicyName()).isEqualTo(getPolicyName());
    assertThat(result.getPolicy().getPath()).isEqualTo(TestEntities.TEST_POLICY_PATH);
  }
  
  @Test
  public void attachesPolicy() {
    Policy policy = policyService.createEnvironmentPolicy(CreateEnvironmentPolicyRequest.builder()
      .setProjectId(TestEntities.TEST_PROJECT_ID)
      .setEnvironment(TestEntities.TEST_ENVIRONMENT)
      .build());
    
    ListAttachedRolePoliciesResult roleResult = client.listAttachedRolePolicies(
      new ListAttachedRolePoliciesRequest()
        .withRoleName(TestEntities.TEST_ROLE));
    
    ListAttachedRolePoliciesResult groupResult = client.listAttachedRolePolicies(
      new ListAttachedRolePoliciesRequest()
        .withRoleName(TestEntities.TEST_GROUP));
    
    AttachedPolicy expected = new AttachedPolicy()
      .withPolicyArn(policy.getArn())
      .withPolicyName(getPolicyName());
    
    assertThat(roleResult.getAttachedPolicies()).containsExactly(expected);
    assertThat(groupResult.getAttachedPolicies()).containsExactly(expected);
  }
  
  private static final String getPolicyName() {
    return TestEntities.TEST_POLICY_PREFIX + Joiner.on("-").join(Arrays.asList(
      TestEntities.TEST_PROJECT_ID, TestEntities.TEST_ENVIRONMENT_ID));
  }
}
