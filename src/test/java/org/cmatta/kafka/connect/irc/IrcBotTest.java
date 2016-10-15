package org.cmatta.kafka.connect.irc;

import org.jibble.pircbot.IrcException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;

/**
 * Created by chris on 10/14/16.
 */
public class IrcBotTest {
  IrcBot ircBot;

  @Before
  public void setUp() throws IOException, IrcException {
    ircBot = new IrcBot();
    try {
      ircBot.connect("irc.wikimedia.org");
    } catch (IOException e) {
      e.printStackTrace();
    } catch (IrcException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testIrcBotMessageQueueIsEmpty() {
    assertTrue(ircBot.messageQueue.isEmpty());
  }
}
