package io.github.spharris.stash.service.aws;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.amazonaws.auth.policy.actions.S3Actions;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.guava.GuavaModule;

@RunWith(JUnit4.class)
public class S3ActionsDeserializerTest {

  final ObjectMapper mapper = new ObjectMapper()
      .registerModule(new SimpleModule().addDeserializer(
        S3Actions.class, new S3ActionsDeserializer()))
      .registerModule(new GuavaModule());

  @Test
  public void deserializesAll() throws Exception {
    String action = "\"s3:*\"";
    
    S3Actions result = mapper.readValue(action, S3Actions.class);
    S3Actions expected = S3Actions.AllS3Actions;
    
    assertThat(result).isEqualTo(expected);
  }
  
  @Test
  public void deserializesProperty() throws Exception {
    String action = "\"s3:GetObject\"";
    
    S3Actions result = mapper.readValue(action, S3Actions.class);
    S3Actions expected = S3Actions.GetObject;
    
    assertThat(result).isEqualTo(expected);
  }
}
