package org.cmatta.kafka.connect.irc;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigDef.Type;
import org.apache.kafka.common.config.ConfigDef.Importance;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class IrcSourceConnectorConfig extends AbstractConfig {

  public static final String IRC_SERVER_CONF = "irc.server";
  private static final String IRC_SERVER_DOC = "The IRC Server to connect to.";
  public static final String IRC_SERVER_PORT_CONF = "irc.server.port";
  private static final String IRC_SERVER_PORT_DOC = "The port of the IRC server to connect to. If not included defaults to 6697";
  public static final String IRC_CHANNELS_CONF = "irc.channels";
  private static final String IRC_CHANNELS_DOC = "Comma separated list of IRC channels.";
  public static final String KAFKA_TOPIC_CONF = "kafka.topic";
  private static final String KAFKA_TOPIC_DOC = "Topic to save IRC messages to.";

  public IrcSourceConnectorConfig(ConfigDef config, Map<String, String> parsedConfig) {
    super(config, parsedConfig);
  }

  public IrcSourceConnectorConfig(Map<String, String> parsedConfig) {
    this(conf(), parsedConfig);
  }

  public static ConfigDef conf() {
    return new ConfigDef()
        .define(IRC_SERVER_CONF, Type.STRING, Importance.HIGH, IRC_SERVER_DOC)
        .define(IRC_SERVER_PORT_CONF, Type.INT, 6667, Importance.LOW, IRC_SERVER_PORT_DOC)
        .define(IRC_CHANNELS_CONF, Type.STRING, Importance.HIGH, IRC_CHANNELS_DOC)
        .define(KAFKA_TOPIC_CONF, Type.STRING, Importance.HIGH, KAFKA_TOPIC_DOC);
  }

  public String getIrcServer(){
    return this.getString(IRC_SERVER_CONF);
  }

  public int getIrcServerPort() { return this.getInt(IRC_SERVER_PORT_CONF); }

  public List<String> getIrcChannels() {
    return Arrays.asList(this.getString(IRC_CHANNELS_CONF).split("\\s*,\\s*"));
  }

  public String getKafkaTopic() { return this.getString(KAFKA_TOPIC_CONF); }
}
