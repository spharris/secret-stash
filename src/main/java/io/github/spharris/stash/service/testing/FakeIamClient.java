package io.github.spharris.stash.service.testing;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.AmazonWebServiceRequest;
import com.amazonaws.ResponseMetadata;
import com.amazonaws.regions.Region;
import com.amazonaws.services.identitymanagement.AmazonIdentityManagement;
import com.amazonaws.services.identitymanagement.model.*;
import com.amazonaws.services.identitymanagement.waiters.AmazonIdentityManagementWaiters;
import com.google.common.base.Splitter;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multimap;

public class FakeIamClient implements AmazonIdentityManagement {

  public static final String ARN_PREFIX = "arn:aws:iam::" + TestEntities.TEST_POLICY_ACCOUNT
      + ":policy";
  
  // Maps groups/roles to attached policies
  private final Multimap<String, String> policyAssignments = HashMultimap.create();
  private final Map<String, Policy> policies = new HashMap<>();
  
  @Override
  public void setEndpoint(String endpoint) {}

  @Override
  public void setRegion(Region region) {}

  @Override
  public AddClientIDToOpenIDConnectProviderResult addClientIDToOpenIDConnectProvider(
      AddClientIDToOpenIDConnectProviderRequest addClientIDToOpenIDConnectProviderRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public AddRoleToInstanceProfileResult addRoleToInstanceProfile(
      AddRoleToInstanceProfileRequest addRoleToInstanceProfileRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public AddUserToGroupResult addUserToGroup(AddUserToGroupRequest addUserToGroupRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public AttachGroupPolicyResult attachGroupPolicy(
      AttachGroupPolicyRequest request) {
    policyAssignments.put(request.getGroupName(), request.getPolicyArn());
    
    return new AttachGroupPolicyResult();
  }

  @Override
  public AttachRolePolicyResult attachRolePolicy(AttachRolePolicyRequest request) {
    policyAssignments.put(request.getRoleName(), request.getPolicyArn());
    
    return new AttachRolePolicyResult();
  }

  @Override
  public AttachUserPolicyResult attachUserPolicy(AttachUserPolicyRequest attachUserPolicyRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public ChangePasswordResult changePassword(ChangePasswordRequest changePasswordRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public CreateAccessKeyResult createAccessKey(CreateAccessKeyRequest createAccessKeyRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public CreateAccessKeyResult createAccessKey() {
    throw new UnsupportedOperationException();
  }

  @Override
  public CreateAccountAliasResult createAccountAlias(
      CreateAccountAliasRequest createAccountAliasRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public CreateGroupResult createGroup(CreateGroupRequest createGroupRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public CreateInstanceProfileResult createInstanceProfile(
      CreateInstanceProfileRequest createInstanceProfileRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public CreateLoginProfileResult createLoginProfile(
      CreateLoginProfileRequest createLoginProfileRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public CreateOpenIDConnectProviderResult createOpenIDConnectProvider(
      CreateOpenIDConnectProviderRequest createOpenIDConnectProviderRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public CreatePolicyResult createPolicy(CreatePolicyRequest request) {
    Policy p = new Policy();
    if (request.getPath() != null) {
      p.setPath(request.getPath());
    } else {
      p.setPath("/");
    }
    
    p.setArn(ARN_PREFIX + p.getPath() + request.getPolicyName());
    p.setPolicyName(request.getPolicyName());
    policies.put(p.getArn(), p);
    
    return new CreatePolicyResult()
        .withPolicy(p);
  }

  @Override
  public CreatePolicyVersionResult createPolicyVersion(
      CreatePolicyVersionRequest createPolicyVersionRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public CreateRoleResult createRole(CreateRoleRequest createRoleRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public CreateSAMLProviderResult createSAMLProvider(
      CreateSAMLProviderRequest createSAMLProviderRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public CreateUserResult createUser(CreateUserRequest createUserRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public CreateVirtualMFADeviceResult createVirtualMFADevice(
      CreateVirtualMFADeviceRequest createVirtualMFADeviceRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public DeactivateMFADeviceResult deactivateMFADevice(
      DeactivateMFADeviceRequest deactivateMFADeviceRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public DeleteAccessKeyResult deleteAccessKey(DeleteAccessKeyRequest deleteAccessKeyRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public DeleteAccountAliasResult deleteAccountAlias(
      DeleteAccountAliasRequest deleteAccountAliasRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public DeleteAccountPasswordPolicyResult deleteAccountPasswordPolicy(
      DeleteAccountPasswordPolicyRequest deleteAccountPasswordPolicyRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public DeleteAccountPasswordPolicyResult deleteAccountPasswordPolicy() {
    throw new UnsupportedOperationException();
  }

  @Override
  public DeleteGroupResult deleteGroup(DeleteGroupRequest deleteGroupRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public DeleteGroupPolicyResult deleteGroupPolicy(
      DeleteGroupPolicyRequest deleteGroupPolicyRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public DeleteInstanceProfileResult deleteInstanceProfile(
      DeleteInstanceProfileRequest deleteInstanceProfileRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public DeleteLoginProfileResult deleteLoginProfile(
      DeleteLoginProfileRequest deleteLoginProfileRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public DeleteOpenIDConnectProviderResult deleteOpenIDConnectProvider(
      DeleteOpenIDConnectProviderRequest deleteOpenIDConnectProviderRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public DeletePolicyResult deletePolicy(DeletePolicyRequest deletePolicyRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public DeletePolicyVersionResult deletePolicyVersion(
      DeletePolicyVersionRequest deletePolicyVersionRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public DeleteRoleResult deleteRole(DeleteRoleRequest deleteRoleRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public DeleteRolePolicyResult deleteRolePolicy(DeleteRolePolicyRequest deleteRolePolicyRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public DeleteSAMLProviderResult deleteSAMLProvider(
      DeleteSAMLProviderRequest deleteSAMLProviderRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public DeleteSSHPublicKeyResult deleteSSHPublicKey(
      DeleteSSHPublicKeyRequest deleteSSHPublicKeyRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public DeleteServerCertificateResult deleteServerCertificate(
      DeleteServerCertificateRequest deleteServerCertificateRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public DeleteSigningCertificateResult deleteSigningCertificate(
      DeleteSigningCertificateRequest deleteSigningCertificateRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public DeleteUserResult deleteUser(DeleteUserRequest deleteUserRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public DeleteUserPolicyResult deleteUserPolicy(DeleteUserPolicyRequest deleteUserPolicyRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public DeleteVirtualMFADeviceResult deleteVirtualMFADevice(
      DeleteVirtualMFADeviceRequest deleteVirtualMFADeviceRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public DetachGroupPolicyResult detachGroupPolicy(
      DetachGroupPolicyRequest request) {
    policyAssignments.remove(request.getGroupName(), request.getPolicyArn());
    
    return new DetachGroupPolicyResult();
  }

  @Override
  public DetachRolePolicyResult detachRolePolicy(DetachRolePolicyRequest request) {
    policyAssignments.remove(request.getRoleName(), request.getPolicyArn());
    
    return new DetachRolePolicyResult();
  }

  @Override
  public DetachUserPolicyResult detachUserPolicy(DetachUserPolicyRequest detachUserPolicyRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public EnableMFADeviceResult enableMFADevice(EnableMFADeviceRequest enableMFADeviceRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public GenerateCredentialReportResult generateCredentialReport(
      GenerateCredentialReportRequest generateCredentialReportRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public GenerateCredentialReportResult generateCredentialReport() {
    throw new UnsupportedOperationException();
  }

  @Override
  public GetAccessKeyLastUsedResult getAccessKeyLastUsed(
      GetAccessKeyLastUsedRequest getAccessKeyLastUsedRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public GetAccountAuthorizationDetailsResult getAccountAuthorizationDetails(
      GetAccountAuthorizationDetailsRequest getAccountAuthorizationDetailsRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public GetAccountAuthorizationDetailsResult getAccountAuthorizationDetails() {
    throw new UnsupportedOperationException();
  }

  @Override
  public GetAccountPasswordPolicyResult getAccountPasswordPolicy(
      GetAccountPasswordPolicyRequest getAccountPasswordPolicyRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public GetAccountPasswordPolicyResult getAccountPasswordPolicy() {
    throw new UnsupportedOperationException();
  }

  @Override
  public GetAccountSummaryResult getAccountSummary(
      GetAccountSummaryRequest getAccountSummaryRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public GetAccountSummaryResult getAccountSummary() {
    throw new UnsupportedOperationException();
  }

  @Override
  public GetContextKeysForCustomPolicyResult getContextKeysForCustomPolicy(
      GetContextKeysForCustomPolicyRequest getContextKeysForCustomPolicyRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public GetContextKeysForPrincipalPolicyResult getContextKeysForPrincipalPolicy(
      GetContextKeysForPrincipalPolicyRequest getContextKeysForPrincipalPolicyRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public GetCredentialReportResult getCredentialReport(
      GetCredentialReportRequest getCredentialReportRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public GetCredentialReportResult getCredentialReport() {
    throw new UnsupportedOperationException();
  }

  @Override
  public GetGroupResult getGroup(GetGroupRequest getGroupRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public GetGroupPolicyResult getGroupPolicy(GetGroupPolicyRequest getGroupPolicyRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public GetInstanceProfileResult getInstanceProfile(
      GetInstanceProfileRequest getInstanceProfileRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public GetLoginProfileResult getLoginProfile(GetLoginProfileRequest getLoginProfileRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public GetOpenIDConnectProviderResult getOpenIDConnectProvider(
      GetOpenIDConnectProviderRequest getOpenIDConnectProviderRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public GetPolicyResult getPolicy(GetPolicyRequest request) {
    return new GetPolicyResult()
        .withPolicy(policies.get(request.getPolicyArn()));
  }

  @Override
  public GetPolicyVersionResult getPolicyVersion(GetPolicyVersionRequest getPolicyVersionRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public GetRoleResult getRole(GetRoleRequest getRoleRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public GetRolePolicyResult getRolePolicy(GetRolePolicyRequest getRolePolicyRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public GetSAMLProviderResult getSAMLProvider(GetSAMLProviderRequest getSAMLProviderRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public GetSSHPublicKeyResult getSSHPublicKey(GetSSHPublicKeyRequest getSSHPublicKeyRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public GetServerCertificateResult getServerCertificate(
      GetServerCertificateRequest getServerCertificateRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public GetUserResult getUser(GetUserRequest getUserRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public GetUserResult getUser() {
    throw new UnsupportedOperationException();
  }

  @Override
  public GetUserPolicyResult getUserPolicy(GetUserPolicyRequest getUserPolicyRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public ListAccessKeysResult listAccessKeys(ListAccessKeysRequest listAccessKeysRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public ListAccessKeysResult listAccessKeys() {
    throw new UnsupportedOperationException();
  }

  @Override
  public ListAccountAliasesResult listAccountAliases(
      ListAccountAliasesRequest listAccountAliasesRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public ListAccountAliasesResult listAccountAliases() {
    throw new UnsupportedOperationException();
  }

  @Override
  public ListAttachedGroupPoliciesResult listAttachedGroupPolicies(
      ListAttachedGroupPoliciesRequest request) {
    ListAttachedGroupPoliciesResult result = new ListAttachedGroupPoliciesResult();
    
    ImmutableList.Builder<AttachedPolicy> builder = ImmutableList.builder();
    for (String p : policyAssignments.get(request.getGroupName())) {
      builder.add(new AttachedPolicy()
        .withPolicyArn(p)
        .withPolicyName(Iterables.getLast(Splitter.on("/").splitToList(p))));
    }
    
    result.setAttachedPolicies(builder.build());
    return result;
  }

  @Override
  public ListAttachedRolePoliciesResult listAttachedRolePolicies(
      ListAttachedRolePoliciesRequest request) {
    ListAttachedRolePoliciesResult result = new ListAttachedRolePoliciesResult();
    
    ImmutableList.Builder<AttachedPolicy> builder = ImmutableList.builder();
    for (String p : policyAssignments.get(request.getRoleName())) {
      builder.add(new AttachedPolicy()
        .withPolicyArn(p)
        .withPolicyName(Iterables.getLast(Splitter.on("/").splitToList(p))));
    }
    
    result.setAttachedPolicies(builder.build());
    return result;
  }

  @Override
  public ListAttachedUserPoliciesResult listAttachedUserPolicies(
      ListAttachedUserPoliciesRequest listAttachedUserPoliciesRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public ListEntitiesForPolicyResult listEntitiesForPolicy(
      ListEntitiesForPolicyRequest listEntitiesForPolicyRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public ListGroupPoliciesResult listGroupPolicies(
      ListGroupPoliciesRequest listGroupPoliciesRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public ListGroupsResult listGroups(ListGroupsRequest listGroupsRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public ListGroupsResult listGroups() {
    throw new UnsupportedOperationException();
  }

  @Override
  public ListGroupsForUserResult listGroupsForUser(
      ListGroupsForUserRequest listGroupsForUserRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public ListInstanceProfilesResult listInstanceProfiles(
      ListInstanceProfilesRequest listInstanceProfilesRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public ListInstanceProfilesResult listInstanceProfiles() {
    throw new UnsupportedOperationException();
  }

  @Override
  public ListInstanceProfilesForRoleResult listInstanceProfilesForRole(
      ListInstanceProfilesForRoleRequest listInstanceProfilesForRoleRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public ListMFADevicesResult listMFADevices(ListMFADevicesRequest listMFADevicesRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public ListMFADevicesResult listMFADevices() {
    throw new UnsupportedOperationException();
  }

  @Override
  public ListOpenIDConnectProvidersResult listOpenIDConnectProviders(
      ListOpenIDConnectProvidersRequest listOpenIDConnectProvidersRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public ListOpenIDConnectProvidersResult listOpenIDConnectProviders() {
    throw new UnsupportedOperationException();
  }

  @Override
  public ListPoliciesResult listPolicies(ListPoliciesRequest listPoliciesRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public ListPoliciesResult listPolicies() {
    throw new UnsupportedOperationException();
  }

  @Override
  public ListPolicyVersionsResult listPolicyVersions(
      ListPolicyVersionsRequest listPolicyVersionsRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public ListRolePoliciesResult listRolePolicies(ListRolePoliciesRequest listRolePoliciesRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public ListRolesResult listRoles(ListRolesRequest listRolesRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public ListRolesResult listRoles() {
    throw new UnsupportedOperationException();
  }

  @Override
  public ListSAMLProvidersResult listSAMLProviders(
      ListSAMLProvidersRequest listSAMLProvidersRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public ListSAMLProvidersResult listSAMLProviders() {
    throw new UnsupportedOperationException();
  }

  @Override
  public ListSSHPublicKeysResult listSSHPublicKeys(
      ListSSHPublicKeysRequest listSSHPublicKeysRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public ListSSHPublicKeysResult listSSHPublicKeys() {
    throw new UnsupportedOperationException();
  }

  @Override
  public ListServerCertificatesResult listServerCertificates(
      ListServerCertificatesRequest listServerCertificatesRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public ListServerCertificatesResult listServerCertificates() {
    throw new UnsupportedOperationException();
  }

  @Override
  public ListSigningCertificatesResult listSigningCertificates(
      ListSigningCertificatesRequest listSigningCertificatesRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public ListSigningCertificatesResult listSigningCertificates() {
    throw new UnsupportedOperationException();
  }

  @Override
  public ListUserPoliciesResult listUserPolicies(ListUserPoliciesRequest listUserPoliciesRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public ListUsersResult listUsers(ListUsersRequest listUsersRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public ListUsersResult listUsers() {
    throw new UnsupportedOperationException();
  }

  @Override
  public ListVirtualMFADevicesResult listVirtualMFADevices(
      ListVirtualMFADevicesRequest listVirtualMFADevicesRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public ListVirtualMFADevicesResult listVirtualMFADevices() {
    throw new UnsupportedOperationException();
  }

  @Override
  public PutGroupPolicyResult putGroupPolicy(PutGroupPolicyRequest putGroupPolicyRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public PutRolePolicyResult putRolePolicy(PutRolePolicyRequest putRolePolicyRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public PutUserPolicyResult putUserPolicy(PutUserPolicyRequest putUserPolicyRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public RemoveClientIDFromOpenIDConnectProviderResult removeClientIDFromOpenIDConnectProvider(
      RemoveClientIDFromOpenIDConnectProviderRequest removeClientIDFromOpenIDConnectProviderRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public RemoveRoleFromInstanceProfileResult removeRoleFromInstanceProfile(
      RemoveRoleFromInstanceProfileRequest removeRoleFromInstanceProfileRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public RemoveUserFromGroupResult removeUserFromGroup(
      RemoveUserFromGroupRequest removeUserFromGroupRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public ResyncMFADeviceResult resyncMFADevice(ResyncMFADeviceRequest resyncMFADeviceRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public SetDefaultPolicyVersionResult setDefaultPolicyVersion(
      SetDefaultPolicyVersionRequest setDefaultPolicyVersionRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public SimulateCustomPolicyResult simulateCustomPolicy(
      SimulateCustomPolicyRequest simulateCustomPolicyRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public SimulatePrincipalPolicyResult simulatePrincipalPolicy(
      SimulatePrincipalPolicyRequest simulatePrincipalPolicyRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public UpdateAccessKeyResult updateAccessKey(UpdateAccessKeyRequest updateAccessKeyRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public UpdateAccountPasswordPolicyResult updateAccountPasswordPolicy(
      UpdateAccountPasswordPolicyRequest updateAccountPasswordPolicyRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public UpdateAssumeRolePolicyResult updateAssumeRolePolicy(
      UpdateAssumeRolePolicyRequest updateAssumeRolePolicyRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public UpdateGroupResult updateGroup(UpdateGroupRequest updateGroupRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public UpdateLoginProfileResult updateLoginProfile(
      UpdateLoginProfileRequest updateLoginProfileRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public UpdateOpenIDConnectProviderThumbprintResult updateOpenIDConnectProviderThumbprint(
      UpdateOpenIDConnectProviderThumbprintRequest updateOpenIDConnectProviderThumbprintRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public UpdateSAMLProviderResult updateSAMLProvider(
      UpdateSAMLProviderRequest updateSAMLProviderRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public UpdateSSHPublicKeyResult updateSSHPublicKey(
      UpdateSSHPublicKeyRequest updateSSHPublicKeyRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public UpdateServerCertificateResult updateServerCertificate(
      UpdateServerCertificateRequest updateServerCertificateRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public UpdateSigningCertificateResult updateSigningCertificate(
      UpdateSigningCertificateRequest updateSigningCertificateRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public UpdateUserResult updateUser(UpdateUserRequest updateUserRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public UploadSSHPublicKeyResult uploadSSHPublicKey(
      UploadSSHPublicKeyRequest uploadSSHPublicKeyRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public UploadServerCertificateResult uploadServerCertificate(
      UploadServerCertificateRequest uploadServerCertificateRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public UploadSigningCertificateResult uploadSigningCertificate(
      UploadSigningCertificateRequest uploadSigningCertificateRequest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void shutdown() {
    // TODO Auto-generated method stub

  }

  @Override
  public ResponseMetadata getCachedResponseMetadata(AmazonWebServiceRequest request) {
    throw new UnsupportedOperationException();
  }

  @Override
  public AmazonIdentityManagementWaiters waiters() {
    throw new UnsupportedOperationException();
  }

}
