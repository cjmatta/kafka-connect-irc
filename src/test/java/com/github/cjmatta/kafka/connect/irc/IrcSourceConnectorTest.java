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
