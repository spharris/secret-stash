package io.github.spharris.stash.service.testing;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

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
import com.amazonaws.services.s3.model.S3ObjectSummary;
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
import com.google.common.io.CharStreams;

/**
 * Fake S3 client used for testing. Implements a VERY limited subset of S3 functionality
 */
public class FakeS3Client implements AmazonS3 {

  private Map<String, S3Object> data;
  private Map<String, BucketPolicy> policies;
  
  public FakeS3Client() {
    data = new HashMap<>();
    policies = new HashMap<>();
  }
  
  @Override 
  public void abortMultipartUpload(AbortMultipartUploadRequest arg0)
      throws AmazonClientException, AmazonServiceException {
    throw new UnsupportedOperationException();
  }

  @Override
  @SuppressWarnings("deprecation")
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
  public void deleteBucketPolicy(String bucketName) throws AmazonClientException, AmazonServiceException {
    deleteBucketPolicy(new DeleteBucketPolicyRequest(bucketName));
  }

  @Override
  public void deleteBucketPolicy(DeleteBucketPolicyRequest request)
      throws AmazonClientException, AmazonServiceException {
    policies.remove(request.getBucketName());
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
  public void deleteObject(String bucket, String key)
      throws AmazonClientException, AmazonServiceException {
    data.remove(makePath(bucket, key));
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
  public BucketPolicy getBucketPolicy(String bucketName)
      throws AmazonClientException, AmazonServiceException {
    return getBucketPolicy(new GetBucketPolicyRequest(bucketName));
  }

  @Override
  public BucketPolicy getBucketPolicy(GetBucketPolicyRequest request)
      throws AmazonClientException, AmazonServiceException {
    return policies.getOrDefault(request.getBucketName(), new BucketPolicy());
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
    return data.get(makePath(request.getBucketName(), request.getKey()));
  }

  @Override
  public S3Object getObject(String bucketName, String key)
      throws AmazonClientException, AmazonServiceException {
    return getObject(new GetObjectRequest(bucketName, key));
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
  public String getObjectAsString(String bucketName, String key)
      throws AmazonServiceException, AmazonClientException {
    S3Object object = data.get(makePath(bucketName, key));
    
    try {
      return CharStreams.toString(
        new InputStreamReader(object.getObjectContent(), StandardCharsets.UTF_8));
    } catch (IOException e) {
      throw new AmazonServiceException(e.getMessage());
    }
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
  public ListObjectsV2Result listObjectsV2(String bucketName)
      throws AmazonClientException, AmazonServiceException {
    ListObjectsV2Request request = new ListObjectsV2Request();
    request.setBucketName(bucketName);
    return listObjectsV2(request);
  }

  @Override
  public ListObjectsV2Result listObjectsV2(ListObjectsV2Request request)
      throws AmazonClientException, AmazonServiceException {
    
    // If delimter is set, this may include objects that should not be included
    // in the final list
    List<S3ObjectSummary> unfilteredSummaries = data.values().stream()
        .filter((object) -> object.getBucketName().equals(request.getBucketName()))
        .filter((object) -> {
          return request.getPrefix() == null || object.getKey().startsWith(request.getPrefix());
        })
        .map(createObjectSummary)
        .sorted((left, right) -> { return left.getKey().compareTo(right.getKey()); })
        .collect(Collectors.toList());
    
    Set<String> commonPrefixes = new HashSet<>();
    List<S3ObjectSummary> summaries = new ArrayList<>();
    if (request.getPrefix() != null && request.getDelimiter() != null) {
      for (S3ObjectSummary summary : unfilteredSummaries) {
        String key = summary.getKey();
        String prefix = request.getPrefix();
        String delim = request.getDelimiter();
        
        int nextDelim = key.indexOf(delim, prefix.length());
        if (nextDelim == -1) {
          summaries.add(summary);
          continue;
        }
        
        commonPrefixes.add(key.substring(0, nextDelim + 1));
      }
    } else {
      summaries = unfilteredSummaries;
    }
    
    // For some reason you can't set object summaries on this thing
    ListObjectsV2Result result = mock(ListObjectsV2Result.class);
    when(result.getBucketName()).thenReturn(request.getBucketName());
    when(result.getKeyCount()).thenReturn(summaries.size());
    when(result.getObjectSummaries()).thenReturn(summaries);
    when(result.getCommonPrefixes()).thenReturn(
      commonPrefixes.stream().sorted().collect(Collectors.toList()));
    
    return result;
  }

  @Override
  public ListObjectsV2Result listObjectsV2(String bucketName, String prefix)
      throws AmazonClientException, AmazonServiceException {
    ListObjectsV2Request request = new ListObjectsV2Request();
    request.setBucketName(bucketName);
    request.setPrefix(prefix);
    return listObjectsV2(request);
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
     return putObject(new PutObjectRequest(bucketName, key, input, metadata));
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
  public void setBucketPolicy(SetBucketPolicyRequest request)
      throws AmazonClientException, AmazonServiceException {
    BucketPolicy policy = new BucketPolicy();
    policy.setPolicyText(request.getPolicyText());
    policies.put(request.getBucketName(), policy);
  }

  @Override
  public void setBucketPolicy(String bucketName, String policyText)
      throws AmazonClientException, AmazonServiceException {
    setBucketPolicy(new SetBucketPolicyRequest(bucketName, policyText));
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
  @SuppressWarnings("deprecation")
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
  
  private static final Function<S3Object, S3ObjectSummary> createObjectSummary =
      new Function<S3Object, S3ObjectSummary>() {

        @Override
        public S3ObjectSummary apply(S3Object t) {
          S3ObjectSummary summary = new S3ObjectSummary();
          summary.setBucketName(t.getBucketName());
          summary.setKey(t.getKey());

          return summary;
        }
  };
}
