package io.github.spharris.stash.server;

import java.util.Optional;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.google.common.collect.ImmutableMap;
import com.google.inject.AbstractModule;
import com.google.inject.Key;

import io.github.spharris.stash.server.Annotations.Port;
import io.github.spharris.stash.service.Annotations.BucketOfSecrets;
import io.github.spharris.stash.service.Annotations.PolicyPath;
import io.github.spharris.stash.service.Annotations.PolicyPrefix;

class StashFlagsModule extends AbstractModule {

  private static final ImmutableMap<String, String> DEFAULTS =
      ImmutableMap.<String, String>builder()
        .put("port", "3000")
        .put("policy_prefix", "Stash+")
        .put("policy_path", "/secret-stash/")
        .build();
  
  private CommandLine flags;
  
  StashFlagsModule(String[] args) throws ParseException {
    flags = createFlags(args);
  }
  
  @Override
  protected void configure() {
    bind(Key.get(Integer.class, Port.class)).toInstance(Integer.valueOf(getFlagValue("port")));
    bind(Key.get(String.class, BucketOfSecrets.class)).toInstance(
      flags.getOptionValue("bucket_name"));
    bind(Key.get(String.class, PolicyPath.class)).toInstance(getFlagValue("policy_path"));
    bind(Key.get(String.class, PolicyPrefix.class)).toInstance(getFlagValue("policy_prefix"));
  }
  
  private String getFlagValue(String flagName) {
    return Optional.ofNullable(flags.getOptionValue(flagName)).orElse(DEFAULTS.get(flagName));
  }
  
  private static CommandLine createFlags(String[] args) throws ParseException {
    Options helpDef = new Options()
        .addOption(Option.builder("help")
          .longOpt("help")
          .desc("Print this message")
          .build());

    Options flagDef = new Options()
        .addOption(Option.builder("help")
          .longOpt("help")
          .desc("Print this message")
          .build())
        .addOption(Option.builder("port")
          .longOpt("port")
          .desc("The HTTP port to run the server on")
          .hasArg()
          .argName("port")
          .build())
        .addOption(Option.builder("bucket_name")
          .longOpt("bucket_name")
          .desc("The name of the AWS S3 bucket to store secrets in")
          .required()
          .hasArg()
          .argName("bucket")
          .build())
        .addOption(Option.builder("policy_prefix")
          .longOpt("policy_prefix")
          .desc("The prefix to prepend to policy names generated by Secret Stash")
          .hasArg()
          .argName("prefix")
          .build())
        .addOption(Option.builder("policy_path")
          .longOpt("policy_path")
          .desc("The AWS IAM path for policies generated by Secret Stash")
          .hasArg()
          .argName("path")
          .build());
    
    CommandLine helpFlag = new DefaultParser().parse(helpDef, args, true);
    if (helpFlag.hasOption("help")) {
      new HelpFormatter().printHelp("secret-stash", flagDef, true);
      System.exit(0);
    }
    
    return new DefaultParser().parse(flagDef, args);
  }
}
