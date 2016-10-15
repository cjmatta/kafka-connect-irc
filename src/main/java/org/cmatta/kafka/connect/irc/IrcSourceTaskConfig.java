/**
 * Copyright 2015 Confluent Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 **/

package org.cmatta.kafka.connect.irc;


import org.apache.kafka.common.config.ConfigDef;

import java.util.Map;

public class IrcSourceTaskConfig extends IrcSourceConnectorConfig {

  public static String CHANNELS_CONFIG = "channels";
  private static String CHANNELS_DOC = "List of channels to listen in";

  static ConfigDef config = conf()
      .define(CHANNELS_CONFIG, ConfigDef.Type.LIST, ConfigDef.Importance.HIGH, CHANNELS_DOC);

  public IrcSourceTaskConfig(Map<String, String> props) {
    super(props);
  }

  public String taskChannels() {
    return this.getString(CHANNELS_CONFIG);
  }
}