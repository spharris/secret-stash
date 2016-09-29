package io.github.spharris.stash.service.testing;

import static com.google.common.truth.Truth.assertThat;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.google.common.io.CharStreams;

@RunWith(JUnit4.class)
public class FakeS3ClientTest {

  final static String TEST_BUCKET = "test-bucket";
  final static String TEST_FILE = "test-file";
  final static String TEST_DATA = "test-data";
  final static InputStream TEST_STREAM =
      new ByteArrayInputStream(TEST_DATA.getBytes(StandardCharsets.UTF_8));
  
  final AmazonS3 client = new FakeS3Client();
  
  @Test
  public void putReturnsMetadata() {
    PutObjectResult result = client.putObject(TEST_BUCKET, TEST_FILE, TEST_DATA);
    
    assertThat(result).isNotNull();
    assertThat(result.getMetadata()).isNotNull();
  }
  
  @Test
  public void getsObject() throws Exception {
    client.putObject(TEST_BUCKET, TEST_FILE, TEST_DATA);
    
    String result = CharStreams.toString(new InputStreamReader(
      client.getObject(TEST_BUCKET, TEST_FILE).getObjectContent(), StandardCharsets.UTF_8));
    
    assertThat(result).isEqualTo(TEST_DATA);
  }
  
  @Test
  public void deletesObject() throws Exception {
    client.putObject(TEST_BUCKET, TEST_FILE, TEST_DATA);
    client.deleteObject(TEST_BUCKET, TEST_FILE);
    
    assertThat(client.getObject(TEST_BUCKET, TEST_FILE)).isNull();
  }
  
  @Test
  public void listsObjects() throws Exception {
    int objectCount = 10;
    addObjects(10);
    
    ListObjectsV2Result result = client.listObjectsV2(TEST_BUCKET);
    
    assertThat(result.getBucketName()).isEqualTo(TEST_BUCKET);
    assertThat(result.getKeyCount()).isEqualTo(objectCount);
    assertThat(result.getObjectSummaries()).hasSize(objectCount);
    
    for (int i = 0; i < objectCount; i++) {
      S3ObjectSummary summary = result.getObjectSummaries().get(i);
      assertThat(result.getBucketName()).isEqualTo(TEST_BUCKET);
      assertThat(summary.getKey()).isEqualTo(String.valueOf(i));
    }
  }
  
  @Test
  public void listsInAlphabeticalOrder() throws Exception {
    int objectCount = 10;
    for (int i = objectCount - 1; i >= 0; i--) {
      client.putObject(TEST_BUCKET, String.valueOf(i), String.valueOf(i));
    }
    
    ListObjectsV2Result result = client.listObjectsV2(TEST_BUCKET);
    
    assertThat(result.getBucketName()).isEqualTo(TEST_BUCKET);
    assertThat(result.getKeyCount()).isEqualTo(objectCount);
    assertThat(result.getObjectSummaries()).hasSize(objectCount);
    
    for (int i = 0; i < objectCount; i++) {
      S3ObjectSummary summary = result.getObjectSummaries().get(i);
      assertThat(result.getBucketName()).isEqualTo(TEST_BUCKET);
      assertThat(summary.getKey()).isEqualTo(String.valueOf(i));
    }
  }
  
  @Test
  public void listsObjectsWithMultipleBuckets() throws Exception {
    String otherBucket = "other-bucket";
    String otherKey = "other-key";
    String otherData = "other-data";
    client.putObject(TEST_BUCKET, TEST_FILE, TEST_DATA);
    client.putObject(otherBucket, otherKey, otherData);
    
    ListObjectsV2Result result = client.listObjectsV2(otherBucket);
    
    assertThat(result.getBucketName()).isEqualTo(otherBucket);
    assertThat(result.getObjectSummaries()).hasSize(1);
    assertThat(result.getObjectSummaries().get(0).getKey()).isEqualTo(otherKey);
  }
  
  @Test
  public void listsObjectsWithPrefix() throws Exception {
    int objectCount = 10;
    String firstPrefix = "first/";
    String secondPrefix = "second/";
    addObjectsWithPrefix(objectCount, firstPrefix);
    addObjectsWithPrefix(objectCount, secondPrefix);
    
    ListObjectsV2Result result = client.listObjectsV2(TEST_BUCKET, firstPrefix);
    
    assertThat(result.getBucketName()).isEqualTo(TEST_BUCKET);
    assertThat(result.getKeyCount()).isEqualTo(objectCount);
    assertThat(result.getObjectSummaries()).hasSize(objectCount);
    
    for (int i = 0; i < objectCount; i++) {
      S3ObjectSummary summary = result.getObjectSummaries().get(i);
      assertThat(summary.getKey()).isEqualTo(firstPrefix + i);
    }
  }
  
  @Test
  public void listsObjectsWithDelimeter() throws Exception {
    int objectCount = 10;
    String firstPrefix = "first/";
    String secondPrefix = "first/bar/";
    String thirdPrefix = "first/baz/";
    addObjectsWithPrefix(objectCount, firstPrefix);
    addObjectsWithPrefix(objectCount, secondPrefix);
    addObjectsWithPrefix(objectCount, thirdPrefix);
    
    ListObjectsV2Request request = new ListObjectsV2Request();
    request.setBucketName(TEST_BUCKET);
    request.setPrefix(firstPrefix);
    request.setDelimiter("/");
    
    ListObjectsV2Result result = client.listObjectsV2(request);
    
    assertThat(result.getBucketName()).isEqualTo(TEST_BUCKET);
    assertThat(result.getKeyCount()).isEqualTo(objectCount);
    assertThat(result.getObjectSummaries()).hasSize(objectCount);
    assertThat(result.getCommonPrefixes()).containsExactly(secondPrefix, thirdPrefix);
    
    for (int i = 0; i < objectCount; i++) {
      S3ObjectSummary summary = result.getObjectSummaries().get(i);
      assertThat(summary.getKey()).isEqualTo(firstPrefix + i);
    }
  }
  
  @Test
  public void putsBucketPolicy() {
    String policy = "policy";
    
    client.setBucketPolicy(TEST_BUCKET, policy);
    
    assertThat(client.getBucketPolicy(TEST_BUCKET).getPolicyText()).isEqualTo(policy);
  }
  
  @Test
  public void returnsNullPolicy() {
    client.getBucketPolicy(TEST_BUCKET);
    
    assertThat(client.getBucketPolicy(TEST_BUCKET).getPolicyText()).isNull();
  }
  
  @Test
  public void deletesBucketPolicy() {
    String policy = "policy";
    
    client.setBucketPolicy(TEST_BUCKET, policy);
    client.deleteBucketPolicy(TEST_BUCKET);
    
    assertThat(client.getBucketPolicy(TEST_BUCKET).getPolicyText()).isNull();
  }
  
  private void addObjects(int count) {
    for (int i = 0; i < count; i++) {
      client.putObject(TEST_BUCKET, String.valueOf(i), String.valueOf(i));
    }
  }
  
  private void addObjectsWithPrefix(int count, String prefix) {
    for (int i = 0; i < count; i++) {
      client.putObject(TEST_BUCKET, prefix + String.valueOf(i), String.valueOf(i));
    }
  }
}
