package io.github.spharris.stash.service.aws;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.amazonaws.auth.policy.Action;
import com.amazonaws.auth.policy.actions.IdentityManagementActions;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.guava.GuavaModule;

@RunWith(JUnit4.class)
public class ActionDeserializerTest {

  final ObjectMapper mapper = new ObjectMapper()
      .registerModule(new SimpleModule().addDeserializer(
        IdentityManagementActions.class, new ActionDeserializer<>(IdentityManagementActions.class)))
      .registerModule(new GuavaModule());

  @Test
  public void deserializesAll() throws Exception {
    String action = "\"iam:*\"";
    
    IdentityManagementActions result = mapper.readValue(action, IdentityManagementActions.class);
    IdentityManagementActions expected = IdentityManagementActions.AllIdentityManagementActions;
    
    assertThat(result).isEqualTo(expected);
  }
  
  @Test
  public void deserializesProperty() throws Exception {
    String action = "\"iam:CreateGroup\"";
    
    IdentityManagementActions result = mapper.readValue(action, IdentityManagementActions.class);
    IdentityManagementActions expected = IdentityManagementActions.CreateGroup;
    
    assertThat(result).isEqualTo(expected);
  }
}

