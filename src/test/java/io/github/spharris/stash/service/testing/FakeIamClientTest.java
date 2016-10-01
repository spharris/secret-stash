package io.github.spharris.stash.service.testing;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.amazonaws.auth.policy.Action;
import com.amazonaws.auth.policy.actions.S3Actions;
import com.amazonaws.services.identitymanagement.AmazonIdentityManagement;
import com.amazonaws.services.identitymanagement.model.AttachGroupPolicyRequest;
import com.amazonaws.services.identitymanagement.model.AttachRolePolicyRequest;
import com.amazonaws.services.identitymanagement.model.AttachedPolicy;
import com.amazonaws.services.identitymanagement.model.CreatePolicyRequest;
import com.amazonaws.services.identitymanagement.model.CreatePolicyResult;
import com.amazonaws.services.identitymanagement.model.DetachGroupPolicyRequest;
import com.amazonaws.services.identitymanagement.model.DetachRolePolicyRequest;
import com.amazonaws.services.identitymanagement.model.EntityType;
import com.amazonaws.services.identitymanagement.model.GetPolicyRequest;
import com.amazonaws.services.identitymanagement.model.GetPolicyResult;
import com.amazonaws.services.identitymanagement.model.ListAttachedGroupPoliciesRequest;
import com.amazonaws.services.identitymanagement.model.ListAttachedGroupPoliciesResult;
import com.amazonaws.services.identitymanagement.model.ListAttachedRolePoliciesRequest;
import com.amazonaws.services.identitymanagement.model.ListAttachedRolePoliciesResult;
import com.amazonaws.services.identitymanagement.model.ListEntitiesForPolicyRequest;
import com.amazonaws.services.identitymanagement.model.ListEntitiesForPolicyResult;
import com.amazonaws.services.identitymanagement.model.PolicyGroup;
import com.amazonaws.services.identitymanagement.model.PolicyRole;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.guava.GuavaModule;

import io.github.spharris.stash.service.aws.ActionDeserializer;
import io.github.spharris.stash.service.aws.ActionSerializer;
import io.github.spharris.stash.service.aws.Policy;
import io.github.spharris.stash.service.utils.JsonUtil;

@RunWith(JUnit4.class)
public class FakeIamClientTest {

  final ObjectMapper mapper = new ObjectMapper()
      .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
      .registerModule(new GuavaModule())
      .registerModule(new SimpleModule()
        .addSerializer(Action.class, new ActionSerializer())
        .addDeserializer(S3Actions.class, new ActionDeserializer<>(S3Actions.class)));
  
  final JsonUtil json = new JsonUtil(mapper); 
  final AmazonIdentityManagement iamClient = new FakeIamClient();
  
  @Test
  public void createsPolicy() {
    Policy p = Policy.builder()
        .setId(TestEntities.TEST_POLICY_ID)
        .build();
    
    CreatePolicyRequest request = new CreatePolicyRequest();
    request.setPolicyName(TestEntities.TEST_POLICY_NAME);
    request.setPolicyDocument(json.toString(p));
    
    CreatePolicyResult response = iamClient.createPolicy(request);
    
    assertThat(response.getPolicy().getArn()).isEqualTo(createArn(TestEntities.TEST_POLICY_NAME));
    assertThat(response.getPolicy().getPolicyName()).isEqualTo(TestEntities.TEST_POLICY_NAME);
    assertThat(response.getPolicy().getPath()).isEqualTo("/");
  }
  
  @Test
  public void usesPath() {
    Policy p = Policy.builder()
        .setId(TestEntities.TEST_POLICY_ID)
        .build();
    
    CreatePolicyRequest request = new CreatePolicyRequest();
    request.setPolicyName(TestEntities.TEST_POLICY_NAME);
    request.setPolicyDocument(json.toString(p));
    request.setPath(TestEntities.TEST_POLICY_PATH);
    
    CreatePolicyResult response = iamClient.createPolicy(request);
    
    assertThat(response.getPolicy().getArn()).isEqualTo("arn:aws:iam::" 
        + TestEntities.TEST_POLICY_ACCOUNT + ":policy" + TestEntities.TEST_POLICY_PATH 
        + TestEntities.TEST_POLICY_NAME);
    assertThat(response.getPolicy().getPolicyName()).isEqualTo(TestEntities.TEST_POLICY_NAME);
    assertThat(response.getPolicy().getPath()).isEqualTo(TestEntities.TEST_POLICY_PATH);
  }
  
  @Test
  public void getsPolicy() {
    CreatePolicyRequest request = new CreatePolicyRequest();
    request.setPolicyName(TestEntities.TEST_POLICY_NAME);
    CreatePolicyResult createResult = iamClient.createPolicy(request);
    
    GetPolicyResult result = iamClient.getPolicy(new GetPolicyRequest()
      .withPolicyArn(createResult.getPolicy().getArn()));
    assertThat(result.getPolicy().getPolicyName()).isEqualTo(TestEntities.TEST_POLICY_NAME);
  }
  
  @Test
  public void attachesPolicyToRole() {
    iamClient.attachRolePolicy(new AttachRolePolicyRequest()
        .withPolicyArn(createArn(TestEntities.TEST_POLICY_NAME))
        .withRoleName(TestEntities.TEST_ROLE));
    
    ListAttachedRolePoliciesResult result = iamClient.listAttachedRolePolicies(
      new ListAttachedRolePoliciesRequest().withRoleName(TestEntities.TEST_ROLE));
    
    assertThat(result.getAttachedPolicies()).containsExactly(new AttachedPolicy()
      .withPolicyArn(createArn(TestEntities.TEST_POLICY_NAME))
      .withPolicyName(TestEntities.TEST_POLICY_NAME));
  }
  
  @Test
  public void attachesPolicyToGroup() {
    iamClient.attachGroupPolicy(new AttachGroupPolicyRequest()
        .withPolicyArn(createArn(TestEntities.TEST_POLICY_NAME))
        .withGroupName(TestEntities.TEST_GROUP));
    
    ListAttachedGroupPoliciesResult result = iamClient.listAttachedGroupPolicies(
      new ListAttachedGroupPoliciesRequest().withGroupName(TestEntities.TEST_GROUP));
    
    assertThat(result.getAttachedPolicies()).containsExactly(new AttachedPolicy()
      .withPolicyArn(createArn(TestEntities.TEST_POLICY_NAME))
      .withPolicyName(TestEntities.TEST_POLICY_NAME));
  }
  
  @Test
  public void detachesPolicyFromGroup() {
    iamClient.attachGroupPolicy(new AttachGroupPolicyRequest()
        .withPolicyArn(createArn(TestEntities.TEST_POLICY_NAME))
        .withGroupName(TestEntities.TEST_GROUP));
    
    iamClient.detachGroupPolicy(new DetachGroupPolicyRequest()
      .withPolicyArn(createArn(TestEntities.TEST_POLICY_NAME))
      .withGroupName(TestEntities.TEST_GROUP));
    
    ListAttachedGroupPoliciesResult result = iamClient.listAttachedGroupPolicies(
      new ListAttachedGroupPoliciesRequest().withGroupName(TestEntities.TEST_GROUP));
    
    assertThat(result.getAttachedPolicies()).isEmpty();
  }
  
  @Test
  public void detachesPolicyFromRole() {
    iamClient.attachRolePolicy(new AttachRolePolicyRequest()
        .withPolicyArn(createArn(TestEntities.TEST_POLICY_NAME))
        .withRoleName(TestEntities.TEST_ROLE));
    
    iamClient.detachRolePolicy(new DetachRolePolicyRequest()
      .withPolicyArn(createArn(TestEntities.TEST_POLICY_NAME))
      .withRoleName(TestEntities.TEST_ROLE));
    
    ListAttachedRolePoliciesResult result = iamClient.listAttachedRolePolicies(
      new ListAttachedRolePoliciesRequest().withRoleName(TestEntities.TEST_GROUP));
    
    assertThat(result.getAttachedPolicies()).isEmpty();
  }
  
  @Test
  public void retrievesGroupAttachments() {
    iamClient.attachRolePolicy(new AttachRolePolicyRequest()
        .withPolicyArn(createArn(TestEntities.TEST_POLICY_NAME))
        .withRoleName(TestEntities.TEST_ROLE));
    
    iamClient.attachGroupPolicy(new AttachGroupPolicyRequest()
        .withPolicyArn(createArn(TestEntities.TEST_POLICY_NAME))
        .withGroupName(TestEntities.TEST_GROUP));
    
    ListEntitiesForPolicyResult result = iamClient.listEntitiesForPolicy(new ListEntitiesForPolicyRequest()
      .withPolicyArn(createArn(TestEntities.TEST_POLICY_NAME))
      .withEntityFilter(EntityType.Group));
    
    PolicyGroup expected = new PolicyGroup()
        .withGroupName(TestEntities.TEST_GROUP);
    
    assertThat(result.getPolicyRoles()).isEmpty();
    assertThat(result.getPolicyGroups()).containsExactly(expected);
  }
  
  @Test
  public void retrievesRoleAttachments() {
    iamClient.attachRolePolicy(new AttachRolePolicyRequest()
        .withPolicyArn(createArn(TestEntities.TEST_POLICY_NAME))
        .withRoleName(TestEntities.TEST_ROLE));
    
    iamClient.attachGroupPolicy(new AttachGroupPolicyRequest()
        .withPolicyArn(createArn(TestEntities.TEST_POLICY_NAME))
        .withGroupName(TestEntities.TEST_GROUP));
    
    ListEntitiesForPolicyResult result = iamClient.listEntitiesForPolicy(new ListEntitiesForPolicyRequest()
      .withPolicyArn(createArn(TestEntities.TEST_POLICY_NAME))
      .withEntityFilter(EntityType.Role));
    
    PolicyRole expected = new PolicyRole()
        .withRoleName(TestEntities.TEST_ROLE);
    
    assertThat(result.getPolicyGroups()).isEmpty();
    assertThat(result.getPolicyRoles()).containsExactly(expected);
  }
  
  @Test
  public void retrievesAllAttachments() {
    iamClient.attachRolePolicy(new AttachRolePolicyRequest()
        .withPolicyArn(createArn(TestEntities.TEST_POLICY_NAME))
        .withRoleName(TestEntities.TEST_ROLE));
    
    iamClient.attachGroupPolicy(new AttachGroupPolicyRequest()
        .withPolicyArn(createArn(TestEntities.TEST_POLICY_NAME))
        .withGroupName(TestEntities.TEST_GROUP));
    
    ListEntitiesForPolicyResult result = iamClient.listEntitiesForPolicy(new ListEntitiesForPolicyRequest()
      .withPolicyArn(createArn(TestEntities.TEST_POLICY_NAME)));
    
    PolicyGroup expectedGroup = new PolicyGroup()
        .withGroupName(TestEntities.TEST_GROUP);
    
    PolicyRole expectedRole = new PolicyRole()
        .withRoleName(TestEntities.TEST_ROLE);
    
    assertThat(result.getPolicyRoles()).containsExactly(expectedRole);
    assertThat(result.getPolicyGroups()).containsExactly(expectedGroup);
  }
  
  private static String createArn(String policyName) {
    return "arn:aws:iam::" + TestEntities.TEST_POLICY_ACCOUNT + ":policy/" 
        + TestEntities.TEST_POLICY_NAME;
  }
}
