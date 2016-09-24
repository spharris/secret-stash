package io.github.spharris.stash.service.testing;

import com.amazonaws.AmazonWebServiceRequest;
import com.amazonaws.ResponseMetadata;
import com.amazonaws.regions.Region;
import com.amazonaws.services.identitymanagement.AmazonIdentityManagement;
import com.amazonaws.services.identitymanagement.model.*;
import com.amazonaws.services.identitymanagement.waiters.AmazonIdentityManagementWaiters;

public class FakeIamClient implements AmazonIdentityManagement {

  @Override
  public AddClientIDToOpenIDConnectProviderResult addClientIDToOpenIDConnectProvider(
      AddClientIDToOpenIDConnectProviderRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public AddRoleToInstanceProfileResult addRoleToInstanceProfile(
      AddRoleToInstanceProfileRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public AddUserToGroupResult addUserToGroup(AddUserToGroupRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public AttachGroupPolicyResult attachGroupPolicy(AttachGroupPolicyRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public AttachRolePolicyResult attachRolePolicy(AttachRolePolicyRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public AttachUserPolicyResult attachUserPolicy(AttachUserPolicyRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public ChangePasswordResult changePassword(ChangePasswordRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public CreateAccessKeyResult createAccessKey() {
    throw new UnsupportedOperationException();
  }

  @Override
  public CreateAccessKeyResult createAccessKey(CreateAccessKeyRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public CreateAccountAliasResult createAccountAlias(CreateAccountAliasRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public CreateGroupResult createGroup(CreateGroupRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public CreateInstanceProfileResult createInstanceProfile(CreateInstanceProfileRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public CreateLoginProfileResult createLoginProfile(CreateLoginProfileRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public CreateOpenIDConnectProviderResult createOpenIDConnectProvider(
      CreateOpenIDConnectProviderRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public CreatePolicyResult createPolicy(CreatePolicyRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public CreatePolicyVersionResult createPolicyVersion(CreatePolicyVersionRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public CreateRoleResult createRole(CreateRoleRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public CreateSAMLProviderResult createSAMLProvider(CreateSAMLProviderRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public CreateUserResult createUser(CreateUserRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public CreateVirtualMFADeviceResult createVirtualMFADevice(CreateVirtualMFADeviceRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public DeactivateMFADeviceResult deactivateMFADevice(DeactivateMFADeviceRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public DeleteAccessKeyResult deleteAccessKey(DeleteAccessKeyRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public DeleteAccountAliasResult deleteAccountAlias(DeleteAccountAliasRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public DeleteAccountPasswordPolicyResult deleteAccountPasswordPolicy() {
    throw new UnsupportedOperationException();
  }

  @Override
  public DeleteAccountPasswordPolicyResult deleteAccountPasswordPolicy(
      DeleteAccountPasswordPolicyRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public DeleteGroupResult deleteGroup(DeleteGroupRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public DeleteGroupPolicyResult deleteGroupPolicy(DeleteGroupPolicyRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public DeleteInstanceProfileResult deleteInstanceProfile(DeleteInstanceProfileRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public DeleteLoginProfileResult deleteLoginProfile(DeleteLoginProfileRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public DeleteOpenIDConnectProviderResult deleteOpenIDConnectProvider(
      DeleteOpenIDConnectProviderRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public DeletePolicyResult deletePolicy(DeletePolicyRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public DeletePolicyVersionResult deletePolicyVersion(DeletePolicyVersionRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public DeleteRoleResult deleteRole(DeleteRoleRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public DeleteRolePolicyResult deleteRolePolicy(DeleteRolePolicyRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public DeleteSAMLProviderResult deleteSAMLProvider(DeleteSAMLProviderRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public DeleteSSHPublicKeyResult deleteSSHPublicKey(DeleteSSHPublicKeyRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public DeleteServerCertificateResult deleteServerCertificate(
      DeleteServerCertificateRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public DeleteSigningCertificateResult deleteSigningCertificate(
      DeleteSigningCertificateRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public DeleteUserResult deleteUser(DeleteUserRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public DeleteUserPolicyResult deleteUserPolicy(DeleteUserPolicyRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public DeleteVirtualMFADeviceResult deleteVirtualMFADevice(DeleteVirtualMFADeviceRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public DetachGroupPolicyResult detachGroupPolicy(DetachGroupPolicyRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public DetachRolePolicyResult detachRolePolicy(DetachRolePolicyRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public DetachUserPolicyResult detachUserPolicy(DetachUserPolicyRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public EnableMFADeviceResult enableMFADevice(EnableMFADeviceRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public GenerateCredentialReportResult generateCredentialReport() {
    throw new UnsupportedOperationException();
  }

  @Override
  public GenerateCredentialReportResult generateCredentialReport(
      GenerateCredentialReportRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public GetAccessKeyLastUsedResult getAccessKeyLastUsed(GetAccessKeyLastUsedRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public GetAccountAuthorizationDetailsResult getAccountAuthorizationDetails() {
    throw new UnsupportedOperationException();
  }

  @Override
  public GetAccountAuthorizationDetailsResult getAccountAuthorizationDetails(
      GetAccountAuthorizationDetailsRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public GetAccountPasswordPolicyResult getAccountPasswordPolicy() {
    throw new UnsupportedOperationException();
  }

  @Override
  public GetAccountPasswordPolicyResult getAccountPasswordPolicy(
      GetAccountPasswordPolicyRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public GetAccountSummaryResult getAccountSummary() {
    throw new UnsupportedOperationException();
  }

  @Override
  public GetAccountSummaryResult getAccountSummary(GetAccountSummaryRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public ResponseMetadata getCachedResponseMetadata(AmazonWebServiceRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public GetContextKeysForCustomPolicyResult getContextKeysForCustomPolicy(
      GetContextKeysForCustomPolicyRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public GetContextKeysForPrincipalPolicyResult getContextKeysForPrincipalPolicy(
      GetContextKeysForPrincipalPolicyRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public GetCredentialReportResult getCredentialReport() {
    throw new UnsupportedOperationException();
  }

  @Override
  public GetCredentialReportResult getCredentialReport(GetCredentialReportRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public GetGroupResult getGroup(GetGroupRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public GetGroupPolicyResult getGroupPolicy(GetGroupPolicyRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public GetInstanceProfileResult getInstanceProfile(GetInstanceProfileRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public GetLoginProfileResult getLoginProfile(GetLoginProfileRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public GetOpenIDConnectProviderResult getOpenIDConnectProvider(
      GetOpenIDConnectProviderRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public GetPolicyResult getPolicy(GetPolicyRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public GetPolicyVersionResult getPolicyVersion(GetPolicyVersionRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public GetRoleResult getRole(GetRoleRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public GetRolePolicyResult getRolePolicy(GetRolePolicyRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public GetSAMLProviderResult getSAMLProvider(GetSAMLProviderRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public GetSSHPublicKeyResult getSSHPublicKey(GetSSHPublicKeyRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public GetServerCertificateResult getServerCertificate(GetServerCertificateRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public GetUserResult getUser() {
    throw new UnsupportedOperationException();
  }

  @Override
  public GetUserResult getUser(GetUserRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public GetUserPolicyResult getUserPolicy(GetUserPolicyRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public ListAccessKeysResult listAccessKeys() {
    throw new UnsupportedOperationException();
  }

  @Override
  public ListAccessKeysResult listAccessKeys(ListAccessKeysRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public ListAccountAliasesResult listAccountAliases() {
    throw new UnsupportedOperationException();
  }

  @Override
  public ListAccountAliasesResult listAccountAliases(ListAccountAliasesRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public ListAttachedGroupPoliciesResult listAttachedGroupPolicies(
      ListAttachedGroupPoliciesRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public ListAttachedRolePoliciesResult listAttachedRolePolicies(
      ListAttachedRolePoliciesRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public ListAttachedUserPoliciesResult listAttachedUserPolicies(
      ListAttachedUserPoliciesRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public ListEntitiesForPolicyResult listEntitiesForPolicy(ListEntitiesForPolicyRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public ListGroupPoliciesResult listGroupPolicies(ListGroupPoliciesRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public ListGroupsResult listGroups() {
    throw new UnsupportedOperationException();
  }

  @Override
  public ListGroupsResult listGroups(ListGroupsRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public ListGroupsForUserResult listGroupsForUser(ListGroupsForUserRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public ListInstanceProfilesResult listInstanceProfiles() {
    throw new UnsupportedOperationException();
  }

  @Override
  public ListInstanceProfilesResult listInstanceProfiles(ListInstanceProfilesRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public ListInstanceProfilesForRoleResult listInstanceProfilesForRole(
      ListInstanceProfilesForRoleRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public ListMFADevicesResult listMFADevices() {
    throw new UnsupportedOperationException();
  }

  @Override
  public ListMFADevicesResult listMFADevices(ListMFADevicesRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public ListOpenIDConnectProvidersResult listOpenIDConnectProviders() {
    throw new UnsupportedOperationException();
  }

  @Override
  public ListOpenIDConnectProvidersResult listOpenIDConnectProviders(
      ListOpenIDConnectProvidersRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public ListPoliciesResult listPolicies() {
    throw new UnsupportedOperationException();
  }

  @Override
  public ListPoliciesResult listPolicies(ListPoliciesRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public ListPolicyVersionsResult listPolicyVersions(ListPolicyVersionsRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public ListRolePoliciesResult listRolePolicies(ListRolePoliciesRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public ListRolesResult listRoles() {
    throw new UnsupportedOperationException();
  }

  @Override
  public ListRolesResult listRoles(ListRolesRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public ListSAMLProvidersResult listSAMLProviders() {
    throw new UnsupportedOperationException();
  }

  @Override
  public ListSAMLProvidersResult listSAMLProviders(ListSAMLProvidersRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public ListSSHPublicKeysResult listSSHPublicKeys() {
    throw new UnsupportedOperationException();
  }

  @Override
  public ListSSHPublicKeysResult listSSHPublicKeys(ListSSHPublicKeysRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public ListServerCertificatesResult listServerCertificates() {
    throw new UnsupportedOperationException();
  }

  @Override
  public ListServerCertificatesResult listServerCertificates(ListServerCertificatesRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public ListSigningCertificatesResult listSigningCertificates() {
    throw new UnsupportedOperationException();
  }

  @Override
  public ListSigningCertificatesResult listSigningCertificates(
      ListSigningCertificatesRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public ListUserPoliciesResult listUserPolicies(ListUserPoliciesRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public ListUsersResult listUsers() {
    throw new UnsupportedOperationException();
  }

  @Override
  public ListUsersResult listUsers(ListUsersRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public ListVirtualMFADevicesResult listVirtualMFADevices() {
    throw new UnsupportedOperationException();
  }

  @Override
  public ListVirtualMFADevicesResult listVirtualMFADevices(ListVirtualMFADevicesRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public PutGroupPolicyResult putGroupPolicy(PutGroupPolicyRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public PutRolePolicyResult putRolePolicy(PutRolePolicyRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public PutUserPolicyResult putUserPolicy(PutUserPolicyRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public RemoveClientIDFromOpenIDConnectProviderResult removeClientIDFromOpenIDConnectProvider(
      RemoveClientIDFromOpenIDConnectProviderRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public RemoveRoleFromInstanceProfileResult removeRoleFromInstanceProfile(
      RemoveRoleFromInstanceProfileRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public RemoveUserFromGroupResult removeUserFromGroup(RemoveUserFromGroupRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public ResyncMFADeviceResult resyncMFADevice(ResyncMFADeviceRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public SetDefaultPolicyVersionResult setDefaultPolicyVersion(
      SetDefaultPolicyVersionRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setEndpoint(String arg0) {
    // TODO Auto-generated method stub

  }

  @Override
  public void setRegion(Region arg0) {
    // TODO Auto-generated method stub

  }

  @Override
  public void shutdown() {
    // TODO Auto-generated method stub

  }

  @Override
  public SimulateCustomPolicyResult simulateCustomPolicy(SimulateCustomPolicyRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public SimulatePrincipalPolicyResult simulatePrincipalPolicy(
      SimulatePrincipalPolicyRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public UpdateAccessKeyResult updateAccessKey(UpdateAccessKeyRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public UpdateAccountPasswordPolicyResult updateAccountPasswordPolicy(
      UpdateAccountPasswordPolicyRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public UpdateAssumeRolePolicyResult updateAssumeRolePolicy(UpdateAssumeRolePolicyRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public UpdateGroupResult updateGroup(UpdateGroupRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public UpdateLoginProfileResult updateLoginProfile(UpdateLoginProfileRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public UpdateOpenIDConnectProviderThumbprintResult updateOpenIDConnectProviderThumbprint(
      UpdateOpenIDConnectProviderThumbprintRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public UpdateSAMLProviderResult updateSAMLProvider(UpdateSAMLProviderRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public UpdateSSHPublicKeyResult updateSSHPublicKey(UpdateSSHPublicKeyRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public UpdateServerCertificateResult updateServerCertificate(
      UpdateServerCertificateRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public UpdateSigningCertificateResult updateSigningCertificate(
      UpdateSigningCertificateRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public UpdateUserResult updateUser(UpdateUserRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public UploadSSHPublicKeyResult uploadSSHPublicKey(UploadSSHPublicKeyRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public UploadServerCertificateResult uploadServerCertificate(
      UploadServerCertificateRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public UploadSigningCertificateResult uploadSigningCertificate(
      UploadSigningCertificateRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public AmazonIdentityManagementWaiters waiters() {
    throw new UnsupportedOperationException();
  }

}
