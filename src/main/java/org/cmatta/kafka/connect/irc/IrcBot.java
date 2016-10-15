package org.cmatta.kafka.connect.irc;

import org.apache.kafka.connect.data.Struct;
import org.cmatta.kafka.connect.irc.util.IrcMessageCreator;
import org.cmatta.kafka.connect.irc.util.RandomStringGenerator;
import org.jibble.pircbot.PircBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * IrcBot
 * This implementation will put received messageQueue in it's messageQueue queue
 */
public class IrcBot extends PircBot {
  static final Logger log = LoggerFactory.getLogger(PircBot.class);
//  Queue for holding messageQueue
  public final ConcurrentLinkedDeque<Struct> messageQueue = new ConcurrentLinkedDeque<>();


  public IrcBot() {
    RandomStringGenerator rand = new RandomStringGenerator();
    String name = "KafkaConnectBot_" + rand.random(6);
    this.setName(name);
  }


  public void onMessage(String channel, String sender, String login, String hostname, String message) {
    log.debug("Received message!");
    Struct messageStruct = new Struct(IrcMessageCreator.messageSchema);
    IrcMessageCreator.create(channel, sender, login, hostname, message, messageStruct);
    messageQueue.add(messageStruct);
  }
}
