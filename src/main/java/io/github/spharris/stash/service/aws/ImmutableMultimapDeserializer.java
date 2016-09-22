package io.github.spharris.stash.service.aws;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;

public class ImmutableMultimapDeserializer extends
    JsonDeserializer<ImmutableMultimap<String, String>>{

  @Override
  public ImmutableMultimap<String, String> deserialize(JsonParser jp, DeserializationContext ctxt)
      throws IOException, JsonProcessingException {
    expect(jp, JsonToken.START_OBJECT);
    ImmutableMultimap.Builder<String, String> builder = ImmutableMultimap.builder();
    while (jp.nextToken() != JsonToken.END_OBJECT) {
      expect(jp, JsonToken.FIELD_NAME);

      String key = jp.getValueAsString();
      jp.nextToken();

      builder.putAll(key, parseValue(jp));
    }

    return builder.build();
  }

  private ImmutableList<String> parseValue(JsonParser jp) throws IOException {
    if (jp.getCurrentToken() == JsonToken.START_ARRAY) {
      ImmutableList.Builder<String> builder = ImmutableList.builder();
      while (jp.nextToken() != JsonToken.END_ARRAY) {
        expect(jp, JsonToken.VALUE_STRING);
        builder.add(jp.getValueAsString());
      }

      return builder.build();
    } 

    expect(jp, JsonToken.VALUE_STRING);
    return ImmutableList.of(jp.getValueAsString());
  }

  private void expect(JsonParser jp, JsonToken token) throws IOException {
    if (jp.getCurrentToken() != token) {
      throw new JsonMappingException(jp, "Expecting " + token + ", found " + jp.getCurrentToken(),
        jp.getCurrentLocation());
    }
  }
}
