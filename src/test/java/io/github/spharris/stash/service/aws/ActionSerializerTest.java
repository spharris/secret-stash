package io.github.spharris.stash.service.aws;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.amazonaws.auth.policy.actions.S3Actions;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

@RunWith(JUnit4.class)
public class ActionSerializerTest {
  
  final ObjectMapper mapper = new ObjectMapper()
      .registerModule(new SimpleModule().addSerializer(
        S3Actions.class, new ActionsSerializer()));
  
  @Test
  public void serializesProperly() throws Exception {
    String result = mapper.writeValueAsString(S3Actions.AllS3Actions);
    String expected = "\"s3:*\"";
    
    assertThat(result).isEqualTo(expected);
  }
}
