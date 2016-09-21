package io.github.spharris.stash.service;

import static com.google.common.truth.Truth.assertThat;

import java.util.Collection;
import java.util.Map;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.amazonaws.auth.policy.Policy;
import com.amazonaws.auth.policy.Principal;
import com.amazonaws.auth.policy.Statement;
import com.amazonaws.auth.policy.Statement.Effect;
import com.amazonaws.auth.policy.actions.S3Actions;
import com.amazonaws.services.s3.AmazonS3;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.inject.Guice;

import io.github.spharris.stash.service.request.UpdatePolicyRequest;
import io.github.spharris.stash.service.testing.TestEntities;
import io.github.spharris.stash.service.testing.TestModule;

@RunWith(JUnit4.class)
public class PolicyServiceImplTest {

  @Inject AmazonS3 client;
  @Inject PolicyService policyService;
  
  @Before 
  public void createInjector() {
    Guice.createInjector(new TestModule()).injectMembers(this);
  }
  
  @Test
  public void createsPolicyIfNonExistant() {
    policyService.updatePolicy(UpdatePolicyRequest.builder()
      .setProjectId(TestEntities.TEST_PROJECT_ID)
      .setEnvironment(TestEntities.TEST_ENVIRONMENT)
      .build());
    
    Policy policy = Policy.fromJson(
      client.getBucketPolicy(TestEntities.TEST_BUCKET).getPolicyText());
    
    assertThat(policy.getId()).isEqualTo(
      TestEntities.TEST_POLICY_PREFIX + ":" + PolicyService.POLICY_ID);
  }
  
  @Test
  public void addsStatementForNewEnvironment() {
    String policy = "{\"Version\":\"2012-10-17\",\"Id\":\"Policy1474138615524\",\"Statement\":[{\"Sid\":\"Stmt1474138571118\",\"Effect\":\"Allow\",\"Principal\":{\"AWS\":\"arn:aws:iam::198978033631:user/spharris\"},\"Action\":[\"s3:ListBucket\",\"s3:PutBucketPolicy\",\"s3:GetBucketPolicy\"],\"Resource\":\"arn:aws:s3:::spharris.secrets\"},{\"Sid\":\"Stmt1474138571119\",\"Effect\":\"Allow\",\"Principal\":{\"AWS\":\"arn:aws:iam::198978033631:user/spharris\"},\"Action\":[\"s3:DeleteObject\",\"s3:GetObject\",\"s3:PutObject\"],\"Resource\":\"arn:aws:s3:::spharris.secrets/*\"},{\"Sid\":\"Stmt1474138571119\",\"Effect\":\"Allow\",\"Principal\":{\"AWS\":\"arn:aws:iam::198978033631:user/user-with-perms\"},\"Action\":\"s3:GetObject\",\"Resource\":\"arn:aws:s3:::spharris.secrets/test-project/dev/*\"}]}";
    Policy p = Policy.fromJson(policy);
    System.out.println(p);
    
    /*policyService.updatePolicy(UpdatePolicyRequest.builder()
      .setProjectId(TestEntities.TEST_PROJECT_ID)
      .setEnvironment(TestEntities.TEST_ENVIRONMENT)
      .build());
    
    Policy expected = new Policy();
    expected.setId(TestEntities.TEST_POLICY_PREFIX + ":" + PolicyService.POLICY_ID);
    
    Statement statement = new Statement(Effect.Allow);
    statement.setActions(ImmutableList.of(S3Actions.GetObject));
    
    ImmutableList.Builder<Principal> principals = ImmutableList.builder();
    for (String group : TestEntities.TEST_ENVIRONMENT.getAcl().getGroups()) {
      Principal
      principals.add(group);
    }
    statement.setPrincipals();
    
    expected = expected.withStatements(statement);*/
  }
  
  private static void deepComparePolicies(Policy left, Policy right) {
    assertThat(left.getId()).isEqualTo(right.getId());
    assertThat(left.getVersion()).isEqualTo(right.getVersion());
    
    deepCompareStatements(left.getStatements(), right.getStatements());
  }
  
  private static void deepCompareStatements(Collection<Statement> leftCollection,
      Collection<Statement> rightCollection) {
    Map<String, Statement> leftStatements = Maps.uniqueIndex(leftCollection, fromSid());
    Map<String, Statement> rightStatements = Maps.uniqueIndex(rightCollection, fromSid());
    
    assertThat(leftStatements.keySet()).isEqualTo(rightStatements.keySet());
    for (int i = 0; i < leftCollection.size(); i++){
      Statement left = Iterables.get(leftCollection, i);
      Statement right = rightStatements.get(left.getId());
      assertThat(left.getActions()).containsExactly(right.getActions());
      assertThat(left.getEffect()).isEqualTo(right.getEffect());
      assertThat(left.getId()).isEqualTo(right.getId());
      assertThat(left.getPrincipals()).isEqualTo(right.getPrincipals());

      for (int j = 0; j < left.getResources().size(); j++) {
        assertThat(left.getResources().get(j).getId()).isEqualTo(right.getResources().get(j).getId());
      }
    }
  }
  
  private static Function<Statement, String> fromSid() {
    return (statement) -> {
      return statement.getId();
    };
  }
}
