/**
 * Copyright © 2016 Christopher Matta (chris.matta@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.cjmatta.kafka.connect.irc;

import com.github.cjmatta.kafka.connect.irc.util.KafkaBotNameGenerator;
import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigDef.Type;
import org.apache.kafka.common.config.ConfigDef.Importance;
import org.apache.kafka.common.config.types.Password;

import java.util.List;
import java.util.Map;


public class IrcSourceConnectorConfig extends AbstractConfig {

  public static final String IRC_SERVER_CONF = "irc.server";
  private static final String IRC_SERVER_DOC = "The IRC Server to connect to.";
  public static final String IRC_SERVER_PORT_CONF = "irc.server.port";
  private static final String IRC_SERVER_PORT_DOC = "The port of the IRC server to connect to. If not included defaults to 6697";
  public static final String IRC_CHANNELS_CONF = "irc.channels";
  private static final String IRC_CHANNELS_DOC = "Comma separated list of IRC channels.";
  public static final String IRC_BOT_NAME = "irc.bot.name";
  private static final String IRC_BOT_DOC = "The name of the IRC bot in the channel, defaults to KafkaConnectBot.";
  public static final String IRC_PASSWORD_CONF = "irc.password";
  private static final String IRC_PASSWORD_DOC = "Password for connecting to the IRC server";
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
        .define(IRC_BOT_NAME, Type.STRING, KafkaBotNameGenerator.generateBotName("KafkaConnectBot"), Importance.LOW, IRC_BOT_DOC)
        .define(IRC_PASSWORD_CONF, Type.PASSWORD, "", Importance.LOW, IRC_PASSWORD_DOC)
        .define(IRC_CHANNELS_CONF, Type.LIST, Importance.HIGH, IRC_CHANNELS_DOC)
        .define(KAFKA_TOPIC_CONF, Type.STRING, Importance.HIGH, KAFKA_TOPIC_DOC);
  }

  public String getIrcServer(){
    return this.getString(IRC_SERVER_CONF);
  }

  public int getIrcServerPort() { return this.getInt(IRC_SERVER_PORT_CONF); }

  public String getIrcBotName() { return this.getString(IRC_BOT_NAME); }

  public Password getIrcPassword() { return this.getPassword(IRC_PASSWORD_CONF); }

  public List<String> getIrcChannels() {
    return this.getList(IRC_CHANNELS_CONF);
  }

  public String getKafkaTopic() { return this.getString(KAFKA_TOPIC_CONF); }
}
