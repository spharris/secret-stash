package io.github.spharris.stash.service.testing;

import static com.google.common.truth.Truth.assertThat;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.google.common.io.CharStreams;

@RunWith(JUnit4.class)
public class FakeS3ClientTest {

  final static String TEST_BUCKET = "test-bucket";
  final static String TEST_FILE = "test-file";
  final static String TEST_DATA = "test-data";
  final static InputStream TEST_STREAM =
      new ByteArrayInputStream(TEST_DATA.getBytes(StandardCharsets.UTF_8));
  
  AmazonS3 client;
  
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
  
  @Test
  public void getsObject() throws Exception {
    client.putObject(TEST_BUCKET, TEST_FILE, TEST_DATA);
    
    String result = CharStreams.toString(new InputStreamReader(
      client.getObject(TEST_BUCKET, TEST_FILE).getObjectContent()));
    
    assertThat(result).isEqualTo(TEST_DATA);
  }
}
