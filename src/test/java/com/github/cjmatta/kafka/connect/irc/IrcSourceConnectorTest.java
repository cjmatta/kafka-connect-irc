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

import org.junit.Before;

import java.util.HashMap;
import java.util.Map;

public class IrcSourceConnectorTest {
  private IrcSourceConnector connector;
  private Map<String, String> connProps;

  @Before
  public void setup() {
    connector = new IrcSourceConnector();
    connProps = new HashMap<>();
    connProps.put(IrcSourceConnectorConfig.IRC_SERVER_CONF, "irc.wikimedia.org");
    connProps.put(IrcSourceConnectorConfig.IRC_CHANNELS_CONF, "#en.wikipedia");
  }
}
