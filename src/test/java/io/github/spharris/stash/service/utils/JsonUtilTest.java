package io.github.spharris.stash.service.utils;

import static com.google.common.truth.Truth.assertThat;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.google.common.io.ByteStreams;
import com.google.inject.Guice;

import io.github.spharris.stash.service.testing.TestServiceModule;

public class JsonUtilTest {

  @Inject JsonUtil json;
  
  @Rule public ExpectedException thrown = ExpectedException.none();
  
  @Before
  public void createInjector() {
    Guice.createInjector(new TestServiceModule()).injectMembers(this);
  }
  
  @Test
  public void writeToString() {
    TestClass data = new TestClass("data");
    
    String result = json.toString(data);
    String expected = "{\"data\":\"data\"}";
    
    assertThat(result).isEqualTo(expected);
  }
  
  @Test
  public void writeToStringThrowsRuntimeException() {
    thrown.expect(RuntimeException.class);
    
    json.toString(new BadClass());
  }
  
  @Test
  public void writeToInputStream() throws Exception {
    TestClass data = new TestClass("data");
    
    InputStream result = json.toInputStream(data);
    InputStream expected = new ByteArrayInputStream(
      "{\"data\":\"data\"}".getBytes(StandardCharsets.UTF_8));
    
    assertThat(ByteStreams.toByteArray(result)).isEqualTo(ByteStreams.toByteArray(expected));
  }
  
  @Test
  public void writeToInputStreamThrowsRuntimeException() {
    thrown.expect(RuntimeException.class);
    
    json.toInputStream(new BadClass());
  }
  
  @Test
  public void readFromString() {
    String data = "{\"data\":\"data\"}";

    TestClass result = json.fromString(data, TestClass.class);
    TestClass expected = new TestClass("data");
    
    assertThat(result).isEqualTo(expected);
  }
  
  @Test
  public void readFromStringThrowsRuntimeException() {
    thrown.expect(RuntimeException.class);
    
    json.fromString("{\"data\":\"data}", TestClass.class);
  }
  
  @Test
  public void readFromStream() {
    InputStream data = new ByteArrayInputStream(
      "{\"data\":\"data\"}".getBytes(StandardCharsets.UTF_8));

    TestClass result = json.fromInputStream(data, TestClass.class);
    TestClass expected = new TestClass("data");
    
    assertThat(result).isEqualTo(expected);
  }
  
  @Test
  public void readFromStreamThrowsRuntimeException() {
    thrown.expect(RuntimeException.class);
    
    json.fromInputStream(
      new ByteArrayInputStream("{\"data\":\"data}".getBytes(StandardCharsets.UTF_8)),
      TestClass.class);
  }
  
  
  @SuppressWarnings("unused")
  private static class TestClass {
    
    private String data;
    
    TestClass() {}
    
    TestClass(String data) {
      this.data = data;
    }
    
    public String getData() {
      return data;
    }

    public void setData(String data) {
      this.data = data;
    }
    
    @Override
    public boolean equals(Object other) {
      return ((TestClass) other).data.equals(data);
    }
  }
  
  private static class BadClass {}
}
