package io.github.spharris.stash.service.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Utility for reading/writing json that swallows mapping exceptions, turning them into runtime
 * exceptions
 */
public class JsonUtil {
  
  private final ObjectMapper mapper;
  
  @Inject
  public JsonUtil(ObjectMapper mapper) {
    this.mapper = mapper;
  }
  
  public String toString(Object object) {
    try {
      return mapper.writeValueAsString(object);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  
  public InputStream toInputStream(Object object) {
    try {
      return new ByteArrayInputStream(mapper.writeValueAsBytes(object));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  
  public <T> T fromString(String data, Class<T> clazz) {
    try {
      return mapper.readValue(data, clazz);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  
  public <T> T fromInputStream(InputStream data, Class<T> clazz) {
    try {
      return mapper.readValue(data, clazz);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
