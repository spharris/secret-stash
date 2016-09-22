package io.github.spharris.stash.service.aws;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;

class ConditionDeserializer extends
    JsonDeserializer<ImmutableMap<String, ImmutableMultimap<String, String>>> {

  ImmutableMultimapDeserializer multimapDeserializer = new ImmutableMultimapDeserializer();
  
  @Override
  public ImmutableMap<String, ImmutableMultimap<String, String>> deserialize(JsonParser jp,
      DeserializationContext ctxt) throws IOException, JsonProcessingException {
    expect(jp, JsonToken.START_OBJECT);
    ImmutableMap.Builder<String, ImmutableMultimap<String, String>> builder =
        ImmutableMap.builder();
    while(jp.nextToken() != JsonToken.END_OBJECT) {
      expect(jp, JsonToken.FIELD_NAME);
      String key = jp.getValueAsString();
      jp.nextToken();
      
      builder.put(key, multimapDeserializer.deserialize(jp, ctxt));
    }
    
    return builder.build();
  }

  private void expect(JsonParser jp, JsonToken token) throws IOException {
    if (jp.getCurrentToken() != token) {
      throw new JsonMappingException(jp, "Expecting " + token + ", found " + jp.getCurrentToken(),
        jp.getCurrentLocation());
    }
  }
}
