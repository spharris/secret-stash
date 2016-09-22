package io.github.spharris.stash.service.aws;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;

@RunWith(JUnit4.class)
public class ImmutableMultimapDeserializerTest {
  
  final ObjectMapper mapper = new ObjectMapper();
  
  @Test
  public void deserializesObjectWithArray() throws Exception {
    String data = "{\"principal\":{\"AWS\":[\"*\",\"test\"]}}";
    
    TestClass result = mapper.readValue(data, TestClass.class);
    ImmutableMultimap<String, String> expected = ImmutableMultimap.<String, String>builder()
        .putAll("AWS", ImmutableList.of("*", "test"))
        .build();
    
    assertThat(result.getPrincipal()).isEqualTo(expected);
  }
  
  @Test
  public void deserializesMultipleObjectWithArray() throws Exception {
    String data = "{\"principal\":{\"AWS\":[\"*\",\"test\"],"
        + "\"Federated\":[\"accounts.google.com\"]}}";
    
    TestClass result = mapper.readValue(data, TestClass.class);
    ImmutableMultimap<String, String> expected = ImmutableMultimap.<String, String>builder()
        .putAll("AWS", ImmutableList.of("*", "test"))
        .put("Federated", "accounts.google.com")
        .build();
    
    assertThat(result.getPrincipal()).isEqualTo(expected);
  }
  
  private static class TestClass {
    
    private ImmutableMultimap<String, String> principal;
    
    ImmutableMultimap<String, String> getPrincipal() {
      return principal;
    }

    @JsonDeserialize(using = ImmutableMultimapDeserializer.class)
    void setPrincipal(ImmutableMultimap<String, String> principal) {
      this.principal = principal;
    }
  }
}
