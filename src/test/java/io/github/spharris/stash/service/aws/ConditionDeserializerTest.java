package io.github.spharris.stash.service.aws;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;

@RunWith(JUnit4.class)
public class ConditionDeserializerTest {

  final ObjectMapper mapper = new ObjectMapper();
  
  @Test
  public void singleCondition() throws Exception {
    String data = "{\"condition\":{\"date\":{\"greaterThan\":\"1\"}}}";
    
    TestClass result = mapper.readValue(data, TestClass.class);
    ImmutableMap<String, ImmutableMultimap<String, String>> expected = ImmutableMap.of("date",
      ImmutableMultimap.<String, String>builder()
        .put("greaterThan", "1")
        .build());
    
    assertThat(result.getCondition()).isEqualTo(expected);
  }
  
  @Test
  public void singleArrayCondition() throws Exception {
    String data = "{\"condition\":{\"date\":{\"greaterThan\":[\"1\"]}}}";
    
    TestClass result = mapper.readValue(data, TestClass.class);
    ImmutableMap<String, ImmutableMultimap<String, String>> expected = ImmutableMap.of("date",
      ImmutableMultimap.<String, String>builder()
        .put("greaterThan", "1")
        .build());
    
    assertThat(result.getCondition()).isEqualTo(expected);
  }
  
  @Test
  public void singleArrayListCondition() throws Exception {
    String data = "{\"condition\":{\"date\":{\"greaterThan\":[\"1\",\"2\"]}}}";
    
    TestClass result = mapper.readValue(data, TestClass.class);
    ImmutableMap<String, ImmutableMultimap<String, String>> expected = ImmutableMap.of("date",
      ImmutableMultimap.<String, String>builder()
        .putAll("greaterThan", "1", "2")
        .build());
    
    assertThat(result.getCondition()).isEqualTo(expected);
  }
  
  @Test
  public void mixedArrayListCondition() throws Exception {
    String data = "{\"condition\":{\"date\":{\"greaterThan\":[\"1\",\"2\"]},"
        + "\"fate\":{\"lessThan\":\"3\"}}}";
    
    TestClass result = mapper.readValue(data, TestClass.class);
    ImmutableMap<String, ImmutableMultimap<String, String>> expected = ImmutableMap.of("date",
      ImmutableMultimap.<String, String>builder()
        .putAll("greaterThan", "1", "2")
        .build(),
      "fate", ImmutableMultimap.<String, String>builder()
        .put("lessThan", "3")
        .build());
    
    assertThat(result.getCondition()).isEqualTo(expected);
  }
  
  private static class TestClass {
    
    private ImmutableMap<String, ImmutableMultimap<String, String>> condition;
    
    ImmutableMap<String, ImmutableMultimap<String, String>> getCondition() {
      return condition;
    }
    
    @JsonDeserialize(using = ConditionDeserializer.class)
    void setCondition(ImmutableMap<String, ImmutableMultimap<String, String>> condition) {
      this.condition = condition;
    }
  }
}
