package org.cmatta.kafka.connect.irc;

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
  public void doc() {
    System.out.println(IrcSourceConnectorConfig.conf().toRst());
  }
}