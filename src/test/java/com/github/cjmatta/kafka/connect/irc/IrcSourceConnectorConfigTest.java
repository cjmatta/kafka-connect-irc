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

import org.apache.kafka.common.config.types.Password;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IrcSourceConnectorConfigTest {
  private IrcSourceConnectorConfig config;
  private Map<String, String> connProps;

  @Before
  public void setup() {
    connProps = new HashMap<>();
    connProps.put(IrcSourceConnectorConfig.IRC_SERVER_CONF, "irc.wikimedia.org");
    connProps.put(IrcSourceConnectorConfig.IRC_CHANNELS_CONF, "#en.wikipedia,#es.wikipedia");
    connProps.put(IrcSourceConnectorConfig.KAFKA_TOPIC_CONF, "wikipedia");
    connProps.put(IrcSourceConnectorConfig.IRC_PASSWORD_CONF, "p4ssw0rd");
    config = new IrcSourceConnectorConfig(connProps);
  }

  @Test
  public void testGetChannels() {
    List<String> channels = config.getIrcChannels();
    assertEquals(channels.size(), 2);
    assertEquals(channels.get(0), "#en.wikipedia");
    assertEquals(channels.get(1), "#es.wikipedia");
  }

  @Test
  public void testGetPassword() {
    Password password = config.getIrcPassword();
    assertEquals("p4ssw0rd", password.value());
  }

  @Test
  public void doc() {
    System.out.println(IrcSourceConnectorConfig.conf().toRst());
  }
}