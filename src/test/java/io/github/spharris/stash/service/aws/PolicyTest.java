package io.github.spharris.stash.service.aws;

import static com.google.common.truth.Truth.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.amazonaws.auth.policy.Action;
import com.amazonaws.auth.policy.Statement.Effect;
import com.amazonaws.auth.policy.actions.S3Actions;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;

@RunWith(JUnit4.class)
public class PolicyTest {

  // http://docs.aws.amazon.com/AmazonS3/latest/dev/example-bucket-policies.html
  
  final ObjectMapper mapper = new ObjectMapper()
      .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
      .registerModule(new GuavaModule())
      .registerModule(new SimpleModule()
        .addSerializer(Action.class, new ActionSerializer())
        .addDeserializer(S3Actions.class, new S3ActionsDeserializer()));
  
  @Test
  public void multipleConditions() throws Exception {
    Policy result = mapper.readValue(
      getClass().getClassLoader().getResourceAsStream("policies/multiple_conditions.json"),
      Policy.class);
    
    Policy expected = Policy.builder()
        .setId("S3PolicyId1")
        .setStatements(Statement.builder()
          .setId("IPAllow")
          .setEffect(Effect.Allow)
          .setPrincipals(ImmutableMultimap.of("AWS", "*"))
          .setActions(S3Actions.AllS3Actions)
          .setResources("arn:aws:s3:::examplebucket/*")
          .setConditions(ImmutableMap.of(
            "IpAddress", ImmutableMultimap.of("aws:SourceIp", "54.240.143.0/24"),
            "NotIpAddress", ImmutableMultimap.of("aws:SourceIp", "54.240.143.188/32")))
          .build())
        .build();
    
    assertThat(result).isEqualTo(expected);
  }
  
  @Test
  public void baseCase() throws Exception {
    Policy result = mapper.readValue(
      getClass().getClassLoader().getResourceAsStream("policies/base_case.json"),
      Policy.class);
    
    Policy expected = Policy.builder()
        .setStatements(Statement.builder()
          .setId("AddCannedAcl")
          .setEffect(Effect.Allow)
          .setPrincipals(ImmutableMultimap.<String, String>builder()
            .putAll("AWS", "arn:aws:iam::111122223333:root","arn:aws:iam::444455556666:root")
            .build())
          .setActions(S3Actions.PutObject, S3Actions.SetObjectAcl)
          .setResources("arn:aws:s3:::examplebucket/*")
          .setConditions(ImmutableMap.of(
            "StringEquals", ImmutableMultimap.of("s3:x-amz-acl", "public-read")))
          .build())
        .build();
    
    assertThat(result).isEqualTo(expected);
  }
}
