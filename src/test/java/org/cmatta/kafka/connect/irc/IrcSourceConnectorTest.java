package org.cmatta.kafka.connect.irc;

import org.apache.kafka.common.config.ConfigDef;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
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
