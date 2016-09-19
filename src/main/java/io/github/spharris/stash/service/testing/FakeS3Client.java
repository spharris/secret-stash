package io.github.spharris.stash.service.testing;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.AmazonWebServiceRequest;
import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.S3ClientOptions;
import com.amazonaws.services.s3.S3ResponseMetadata;
import com.amazonaws.services.s3.model.AbortMultipartUploadRequest;
import com.amazonaws.services.s3.model.AccessControlList;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.BucketAccelerateConfiguration;
import com.amazonaws.services.s3.model.BucketCrossOriginConfiguration;
import com.amazonaws.services.s3.model.BucketLifecycleConfiguration;
import com.amazonaws.services.s3.model.BucketLoggingConfiguration;
import com.amazonaws.services.s3.model.BucketNotificationConfiguration;
import com.amazonaws.services.s3.model.BucketPolicy;
import com.amazonaws.services.s3.model.BucketReplicationConfiguration;
import com.amazonaws.services.s3.model.BucketTaggingConfiguration;
import com.amazonaws.services.s3.model.BucketVersioningConfiguration;
import com.amazonaws.services.s3.model.BucketWebsiteConfiguration;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.CompleteMultipartUploadRequest;
import com.amazonaws.services.s3.model.CompleteMultipartUploadResult;
import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.amazonaws.services.s3.model.CopyObjectResult;
import com.amazonaws.services.s3.model.CopyPartRequest;
import com.amazonaws.services.s3.model.CopyPartResult;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.DeleteBucketCrossOriginConfigurationRequest;
import com.amazonaws.services.s3.model.DeleteBucketLifecycleConfigurationRequest;
import com.amazonaws.services.s3.model.DeleteBucketPolicyRequest;
import com.amazonaws.services.s3.model.DeleteBucketReplicationConfigurationRequest;
import com.amazonaws.services.s3.model.DeleteBucketRequest;
import com.amazonaws.services.s3.model.DeleteBucketTaggingConfigurationRequest;
import com.amazonaws.services.s3.model.DeleteBucketWebsiteConfigurationRequest;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.DeleteObjectsResult;
import com.amazonaws.services.s3.model.DeleteVersionRequest;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.GetBucketAccelerateConfigurationRequest;
import com.amazonaws.services.s3.model.GetBucketAclRequest;
import com.amazonaws.services.s3.model.GetBucketCrossOriginConfigurationRequest;
import com.amazonaws.services.s3.model.GetBucketLifecycleConfigurationRequest;
import com.amazonaws.services.s3.model.GetBucketLocationRequest;
import com.amazonaws.services.s3.model.GetBucketLoggingConfigurationRequest;
import com.amazonaws.services.s3.model.GetBucketNotificationConfigurationRequest;
import com.amazonaws.services.s3.model.GetBucketPolicyRequest;
import com.amazonaws.services.s3.model.GetBucketReplicationConfigurationRequest;
import com.amazonaws.services.s3.model.GetBucketTaggingConfigurationRequest;
import com.amazonaws.services.s3.model.GetBucketVersioningConfigurationRequest;
import com.amazonaws.services.s3.model.GetBucketWebsiteConfigurationRequest;
import com.amazonaws.services.s3.model.GetObjectAclRequest;
import com.amazonaws.services.s3.model.GetObjectMetadataRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.GetS3AccountOwnerRequest;
import com.amazonaws.services.s3.model.HeadBucketRequest;
import com.amazonaws.services.s3.model.HeadBucketResult;
import com.amazonaws.services.s3.model.InitiateMultipartUploadRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadResult;
import com.amazonaws.services.s3.model.ListBucketsRequest;
import com.amazonaws.services.s3.model.ListMultipartUploadsRequest;
import com.amazonaws.services.s3.model.ListNextBatchOfObjectsRequest;
import com.amazonaws.services.s3.model.ListNextBatchOfVersionsRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.ListPartsRequest;
import com.amazonaws.services.s3.model.ListVersionsRequest;
import com.amazonaws.services.s3.model.MultipartUploadListing;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.Owner;
import com.amazonaws.services.s3.model.PartListing;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.Region;
import com.amazonaws.services.s3.model.RestoreObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.SetBucketAccelerateConfigurationRequest;
import com.amazonaws.services.s3.model.SetBucketAclRequest;
import com.amazonaws.services.s3.model.SetBucketCrossOriginConfigurationRequest;
import com.amazonaws.services.s3.model.SetBucketLifecycleConfigurationRequest;
import com.amazonaws.services.s3.model.SetBucketLoggingConfigurationRequest;
import com.amazonaws.services.s3.model.SetBucketNotificationConfigurationRequest;
import com.amazonaws.services.s3.model.SetBucketPolicyRequest;
import com.amazonaws.services.s3.model.SetBucketReplicationConfigurationRequest;
import com.amazonaws.services.s3.model.SetBucketTaggingConfigurationRequest;
import com.amazonaws.services.s3.model.SetBucketVersioningConfigurationRequest;
import com.amazonaws.services.s3.model.SetBucketWebsiteConfigurationRequest;
import com.amazonaws.services.s3.model.SetObjectAclRequest;
import com.amazonaws.services.s3.model.StorageClass;
import com.amazonaws.services.s3.model.UploadPartRequest;
import com.amazonaws.services.s3.model.UploadPartResult;
import com.amazonaws.services.s3.model.VersionListing;
import com.amazonaws.services.s3.waiters.AmazonS3Waiters;
import com.google.common.io.ByteStreams;

/**
 * Fake S3 client used for testing. Implements a VERY limited subset of all S3 functionality 
 */
public class FakeS3Client implements AmazonS3 {

  private Map<String, S3Object> data;
  
  public FakeS3Client() {
    data = new HashMap<>();
  }
  
  @Override
  public void abortMultipartUpload(AbortMultipartUploadRequest arg0)
      throws AmazonClientException, AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void changeObjectStorageClass(String arg0, String arg1, StorageClass arg2)
      throws AmazonClientException, AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  public CompleteMultipartUploadResult completeMultipartUpload(CompleteMultipartUploadRequest arg0)
      throws AmazonClientException, AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  public CopyObjectResult copyObject(CopyObjectRequest arg0)
      throws AmazonClientException, AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  public CopyObjectResult copyObject(String arg0, String arg1, String arg2, String arg3)
      throws AmazonClientException, AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  public CopyPartResult copyPart(CopyPartRequest arg0)
      throws AmazonClientException, AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  public Bucket createBucket(CreateBucketRequest arg0)
      throws AmazonClientException, AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  public Bucket createBucket(String arg0) throws AmazonClientException, AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  public Bucket createBucket(String arg0, Region arg1)
      throws AmazonClientException, AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  public Bucket createBucket(String arg0, String arg1)
      throws AmazonClientException, AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void deleteBucket(DeleteBucketRequest arg0)
      throws AmazonClientException, AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void deleteBucket(String arg0) throws AmazonClientException, AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void deleteBucketCrossOriginConfiguration(String arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void deleteBucketCrossOriginConfiguration(
      DeleteBucketCrossOriginConfigurationRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void deleteBucketLifecycleConfiguration(String arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void deleteBucketLifecycleConfiguration(DeleteBucketLifecycleConfigurationRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void deleteBucketPolicy(String arg0) throws AmazonClientException, AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void deleteBucketPolicy(DeleteBucketPolicyRequest arg0)
      throws AmazonClientException, AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void deleteBucketReplicationConfiguration(String arg0)
      throws AmazonServiceException, AmazonClientException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void deleteBucketReplicationConfiguration(DeleteBucketReplicationConfigurationRequest arg0)
      throws AmazonServiceException, AmazonClientException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void deleteBucketTaggingConfiguration(String arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void deleteBucketTaggingConfiguration(DeleteBucketTaggingConfigurationRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void deleteBucketWebsiteConfiguration(String arg0)
      throws AmazonClientException, AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void deleteBucketWebsiteConfiguration(DeleteBucketWebsiteConfigurationRequest arg0)
      throws AmazonClientException, AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void deleteObject(DeleteObjectRequest arg0)
      throws AmazonClientException, AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void deleteObject(String arg0, String arg1)
      throws AmazonClientException, AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  public DeleteObjectsResult deleteObjects(DeleteObjectsRequest arg0)
      throws AmazonClientException, AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void deleteVersion(DeleteVersionRequest arg0)
      throws AmazonClientException, AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void deleteVersion(String arg0, String arg1, String arg2)
      throws AmazonClientException, AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void disableRequesterPays(String arg0)
      throws AmazonServiceException, AmazonClientException {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean doesBucketExist(String arg0) throws AmazonClientException, AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean doesObjectExist(String arg0, String arg1)
      throws AmazonServiceException, AmazonClientException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void enableRequesterPays(String arg0)
      throws AmazonServiceException, AmazonClientException {
    throw new UnsupportedOperationException();
  }

  @Override
  public URL generatePresignedUrl(GeneratePresignedUrlRequest arg0) throws AmazonClientException {
    throw new UnsupportedOperationException();
  }

  @Override
  public URL generatePresignedUrl(String arg0, String arg1, Date arg2)
      throws AmazonClientException {
    throw new UnsupportedOperationException();
  }

  @Override
  public URL generatePresignedUrl(String arg0, String arg1, Date arg2, HttpMethod arg3)
      throws AmazonClientException {
    throw new UnsupportedOperationException();
  }

  @Override
  public BucketAccelerateConfiguration getBucketAccelerateConfiguration(String arg0)
      throws AmazonServiceException, AmazonClientException {
    throw new UnsupportedOperationException();
  }

  @Override
  public BucketAccelerateConfiguration getBucketAccelerateConfiguration(
      GetBucketAccelerateConfigurationRequest arg0)
      throws AmazonServiceException, AmazonClientException {
    throw new UnsupportedOperationException();
  }

  @Override
  public AccessControlList getBucketAcl(String arg0)
      throws AmazonClientException, AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  public AccessControlList getBucketAcl(GetBucketAclRequest arg0)
      throws AmazonClientException, AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  public BucketCrossOriginConfiguration getBucketCrossOriginConfiguration(String arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public BucketCrossOriginConfiguration getBucketCrossOriginConfiguration(
      GetBucketCrossOriginConfigurationRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public BucketLifecycleConfiguration getBucketLifecycleConfiguration(String arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public BucketLifecycleConfiguration getBucketLifecycleConfiguration(
      GetBucketLifecycleConfigurationRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public String getBucketLocation(String arg0)
      throws AmazonClientException, AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  public String getBucketLocation(GetBucketLocationRequest arg0)
      throws AmazonClientException, AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  public BucketLoggingConfiguration getBucketLoggingConfiguration(String arg0)
      throws AmazonClientException, AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  public BucketLoggingConfiguration getBucketLoggingConfiguration(
      GetBucketLoggingConfigurationRequest arg0)
      throws AmazonClientException, AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  public BucketNotificationConfiguration getBucketNotificationConfiguration(String arg0)
      throws AmazonClientException, AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  public BucketNotificationConfiguration getBucketNotificationConfiguration(
      GetBucketNotificationConfigurationRequest arg0)
      throws AmazonClientException, AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  public BucketPolicy getBucketPolicy(String arg0)
      throws AmazonClientException, AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  public BucketPolicy getBucketPolicy(GetBucketPolicyRequest arg0)
      throws AmazonClientException, AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  public BucketReplicationConfiguration getBucketReplicationConfiguration(String arg0)
      throws AmazonServiceException, AmazonClientException {
    throw new UnsupportedOperationException();
  }

  @Override
  public BucketReplicationConfiguration getBucketReplicationConfiguration(
      GetBucketReplicationConfigurationRequest arg0)
      throws AmazonServiceException, AmazonClientException {
    throw new UnsupportedOperationException();
  }

  @Override
  public BucketTaggingConfiguration getBucketTaggingConfiguration(String arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public BucketTaggingConfiguration getBucketTaggingConfiguration(
      GetBucketTaggingConfigurationRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public BucketVersioningConfiguration getBucketVersioningConfiguration(String arg0)
      throws AmazonClientException, AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  public BucketVersioningConfiguration getBucketVersioningConfiguration(
      GetBucketVersioningConfigurationRequest arg0)
      throws AmazonClientException, AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  public BucketWebsiteConfiguration getBucketWebsiteConfiguration(String arg0)
      throws AmazonClientException, AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  public BucketWebsiteConfiguration getBucketWebsiteConfiguration(
      GetBucketWebsiteConfigurationRequest arg0)
      throws AmazonClientException, AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  public S3ResponseMetadata getCachedResponseMetadata(AmazonWebServiceRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public S3Object getObject(GetObjectRequest request)
      throws AmazonClientException, AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  public S3Object getObject(String bucketName, String key)
      throws AmazonClientException, AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  public ObjectMetadata getObject(GetObjectRequest request, File destinationFile)
      throws AmazonClientException, AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  public AccessControlList getObjectAcl(GetObjectAclRequest arg0)
      throws AmazonClientException, AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  public AccessControlList getObjectAcl(String arg0, String arg1)
      throws AmazonClientException, AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  public AccessControlList getObjectAcl(String arg0, String arg1, String arg2)
      throws AmazonClientException, AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  public String getObjectAsString(String arg0, String arg1)
      throws AmazonServiceException, AmazonClientException {
    throw new UnsupportedOperationException();
  }

  @Override
  public ObjectMetadata getObjectMetadata(GetObjectMetadataRequest request)
      throws AmazonClientException, AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  public ObjectMetadata getObjectMetadata(String bucketName, String key)
      throws AmazonClientException, AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  public Region getRegion() {
    throw new UnsupportedOperationException();
  }

  @Override
  public String getRegionName() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Owner getS3AccountOwner() throws AmazonClientException, AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  public Owner getS3AccountOwner(GetS3AccountOwnerRequest arg0)
      throws AmazonClientException, AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  public URL getUrl(String arg0, String arg1) {
    throw new UnsupportedOperationException();
  }

  @Override
  public HeadBucketResult headBucket(HeadBucketRequest arg0)
      throws AmazonClientException, AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  public InitiateMultipartUploadResult initiateMultipartUpload(InitiateMultipartUploadRequest arg0)
      throws AmazonClientException, AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isRequesterPaysEnabled(String arg0)
      throws AmazonServiceException, AmazonClientException {
    throw new UnsupportedOperationException();
  }

  @Override
  public List<Bucket> listBuckets() throws AmazonClientException, AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  public List<Bucket> listBuckets(ListBucketsRequest arg0)
      throws AmazonClientException, AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  public MultipartUploadListing listMultipartUploads(ListMultipartUploadsRequest arg0)
      throws AmazonClientException, AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  public ObjectListing listNextBatchOfObjects(ObjectListing arg0)
      throws AmazonClientException, AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  public ObjectListing listNextBatchOfObjects(ListNextBatchOfObjectsRequest arg0)
      throws AmazonClientException, AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  public VersionListing listNextBatchOfVersions(VersionListing arg0)
      throws AmazonClientException, AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  public VersionListing listNextBatchOfVersions(ListNextBatchOfVersionsRequest arg0)
      throws AmazonClientException, AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  public ObjectListing listObjects(String arg0)
      throws AmazonClientException, AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  public ObjectListing listObjects(ListObjectsRequest arg0)
      throws AmazonClientException, AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  public ObjectListing listObjects(String arg0, String arg1)
      throws AmazonClientException, AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  public ListObjectsV2Result listObjectsV2(String arg0)
      throws AmazonClientException, AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  public ListObjectsV2Result listObjectsV2(ListObjectsV2Request arg0)
      throws AmazonClientException, AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  public ListObjectsV2Result listObjectsV2(String arg0, String arg1)
      throws AmazonClientException, AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  public PartListing listParts(ListPartsRequest arg0)
      throws AmazonClientException, AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  public VersionListing listVersions(ListVersionsRequest arg0)
      throws AmazonClientException, AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  public VersionListing listVersions(String arg0, String arg1)
      throws AmazonClientException, AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  public VersionListing listVersions(String arg0,
      String arg1,
      String arg2,
      String arg3,
      String arg4,
      Integer arg5) throws AmazonClientException, AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  @SuppressWarnings("resource")
  public PutObjectResult putObject(PutObjectRequest request)
      throws AmazonClientException, AmazonServiceException {
    S3Object object = new S3Object();
    object.setBucketName(checkNotNull(request.getBucketName()));
    object.setKey(checkNotNull(request.getKey()));
    object.setObjectMetadata(checkNotNull(request.getMetadata()));
    
    try {
      object.setObjectContent(new ByteArrayInputStream(
        ByteStreams.toByteArray(request.getInputStream())));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    
    data.put(makePath(request.getBucketName(), request.getKey()), object);
    
    ObjectMetadata meta = request.getMetadata();
    PutObjectResult result = new PutObjectResult();
    result.setMetadata(meta);
    
    return result;
  }

  @Override
  public PutObjectResult putObject(String bucketName, String key, File file)
      throws AmazonClientException, AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  public PutObjectResult putObject(String bucketName, String key, String content)
      throws AmazonServiceException, AmazonClientException {
    return putObject(new PutObjectRequest(bucketName, key,
      new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)), new ObjectMetadata()));
  }

  @Override
  public PutObjectResult putObject(String bucketName, String key, InputStream input,
      ObjectMetadata metadata)
      throws AmazonClientException, AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void restoreObject(RestoreObjectRequest arg0) throws AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void restoreObject(String arg0, String arg1, int arg2) throws AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setBucketAccelerateConfiguration(SetBucketAccelerateConfigurationRequest arg0)
      throws AmazonServiceException, AmazonClientException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setBucketAccelerateConfiguration(String arg0, BucketAccelerateConfiguration arg1)
      throws AmazonServiceException, AmazonClientException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setBucketAcl(SetBucketAclRequest arg0)
      throws AmazonClientException, AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setBucketAcl(String arg0, AccessControlList arg1)
      throws AmazonClientException, AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setBucketAcl(String arg0, CannedAccessControlList arg1)
      throws AmazonClientException, AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setBucketCrossOriginConfiguration(SetBucketCrossOriginConfigurationRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setBucketCrossOriginConfiguration(String arg0, BucketCrossOriginConfiguration arg1) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setBucketLifecycleConfiguration(SetBucketLifecycleConfigurationRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setBucketLifecycleConfiguration(String arg0, BucketLifecycleConfiguration arg1) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setBucketLoggingConfiguration(SetBucketLoggingConfigurationRequest arg0)
      throws AmazonClientException, AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setBucketNotificationConfiguration(SetBucketNotificationConfigurationRequest arg0)
      throws AmazonClientException, AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setBucketNotificationConfiguration(String arg0, BucketNotificationConfiguration arg1)
      throws AmazonClientException, AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setBucketPolicy(SetBucketPolicyRequest arg0)
      throws AmazonClientException, AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setBucketPolicy(String arg0, String arg1)
      throws AmazonClientException, AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setBucketReplicationConfiguration(SetBucketReplicationConfigurationRequest arg0)
      throws AmazonServiceException, AmazonClientException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setBucketReplicationConfiguration(String arg0, BucketReplicationConfiguration arg1)
      throws AmazonServiceException, AmazonClientException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setBucketTaggingConfiguration(SetBucketTaggingConfigurationRequest arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setBucketTaggingConfiguration(String arg0, BucketTaggingConfiguration arg1) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setBucketVersioningConfiguration(SetBucketVersioningConfigurationRequest arg0)
      throws AmazonClientException, AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setBucketWebsiteConfiguration(SetBucketWebsiteConfigurationRequest arg0)
      throws AmazonClientException, AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setBucketWebsiteConfiguration(String arg0, BucketWebsiteConfiguration arg1)
      throws AmazonClientException, AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setEndpoint(String arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setObjectAcl(SetObjectAclRequest arg0)
      throws AmazonClientException, AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setObjectAcl(String arg0, String arg1, AccessControlList arg2)
      throws AmazonClientException, AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setObjectAcl(String arg0, String arg1, CannedAccessControlList arg2)
      throws AmazonClientException, AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setObjectAcl(String arg0, String arg1, String arg2, AccessControlList arg3)
      throws AmazonClientException, AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setObjectAcl(String arg0, String arg1, String arg2, CannedAccessControlList arg3)
      throws AmazonClientException, AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setObjectRedirectLocation(String arg0, String arg1, String arg2)
      throws AmazonClientException, AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setRegion(com.amazonaws.regions.Region arg0) throws IllegalArgumentException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setS3ClientOptions(S3ClientOptions arg0) {
    throw new UnsupportedOperationException();
  }

  @Override
  public UploadPartResult uploadPart(UploadPartRequest arg0)
      throws AmazonClientException, AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  public AmazonS3Waiters waiters() {
    throw new UnsupportedOperationException();
  }

  private static String makePath(String bucket, String key) {
    return String.format("%s:%s", bucket, key);
  }
}