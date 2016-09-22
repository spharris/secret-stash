package io.github.spharris.stash.service.aws;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.google.common.collect.ImmutableMultimap;

/**
 * Deserializer for a statement's "Principal" field.
 */
class PrincipalDeserializer extends JsonDeserializer<ImmutableMultimap<String, String>> {

  ImmutableMultimapDeserializer multimapDeserializer = new ImmutableMultimapDeserializer();
  
  @Override
  public ImmutableMultimap<String, String> deserialize(JsonParser jp, DeserializationContext ctxt)
      throws IOException, JsonProcessingException {
    if (jp.getCurrentToken() == JsonToken.VALUE_STRING && jp.getValueAsString().equals("*")) {
      return ImmutableMultimap.of("AWS", "*");
    }
    
    return multimapDeserializer.deserialize(jp,  ctxt);
  }
}
