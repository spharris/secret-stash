package io.github.spharris.stash.service.testing;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectResult;

@RunWith(JUnit4.class)
public class FakeS3ClientTest {

  final static String TEST_BUCKET = "test-bucket";
  final static String TEST_FILE = "test-file";
  final static String TEST_DATA = "test-data";
  
  FakeS3Client client;
  
  @Before
  public void initializeClient() {
    client = new FakeS3Client();
  }
  
  @Test
  public void putReturnsMetadata() {
    PutObjectResult result = client.putObject(TEST_BUCKET, TEST_FILE, TEST_DATA);
    
    assertThat(result).isNotNull();
    assertThat(result.getMetadata()).isNotNull();
  }
}
