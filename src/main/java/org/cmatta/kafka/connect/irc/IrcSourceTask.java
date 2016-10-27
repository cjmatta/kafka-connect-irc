package org.cmatta.kafka.connect.irc;

import com.google.common.collect.ImmutableMap;
import org.apache.kafka.common.config.ConfigException;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.source.SourceTask;
import org.cmatta.kafka.connect.irc.util.RandomStringGenerator;
import org.schwering.irc.lib.IRCConnection;
import org.schwering.irc.lib.IRCEventAdapter;
import org.schwering.irc.lib.IRCUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class IrcSourceTask extends SourceTask {
  static final Logger log = LoggerFactory.getLogger(IrcSourceTask.class);

  private static final String TIMESTAMP_FIELD = "timestamp";
  private static final String CHANNEL_FIELD = "channel";
  private static final Schema KEY_SCHEMA = Schema.STRING_SCHEMA;

  IrcSourceTaskConfig config;

  private String ircServer;
  private List<String> channels;
  private int ircPort;

  private String topic;

  private BlockingQueue<SourceRecord> queue = null;

  private IRCConnection connection;

  @Override
  public String version() {
    return VersionUtil.getVersion();
  }

  @Override
  public void start(Map<String, String> props) {
    try {
      config = new IrcSourceTaskConfig(props);
      queue = new LinkedBlockingQueue<>();
      ircServer = config.getIrcServer();
      ircPort = config.getIrcServerPort();
      channels = config.getIrcChannels();
      topic = config.getKafkaTopic();

      String nick = "KafkaConnectBot_" + RandomStringGenerator.random(6);
      this.connection = new IRCConnection(ircServer, new int[]{ircPort}, "", nick, nick, nick);
      this.connection.addIRCEventListener(new IrcMessageEvent());
      this.connection.setEncoding("UTF-8");
      this.connection.setPong(true);
      this.connection.setColors(false);

      if(log.isInfoEnabled()) {
        log.info("Connecting to server: {}", config.getIrcServer());
      }
      try {
        this.connection.connect();
      } catch (IOException e) {
        throw new ConnectException("Unable to connect to server: " + this.ircServer);
      }

      for (String channel : config.getIrcChannels()) {
        if(log.isInfoEnabled()) {
          log.info("Joining channel: {}", channel);
        }
        try {
          this.connection.doJoin(channel);
        } catch (Exception e) {
          throw new ConnectException("Problem joining channel " + channel);
        }

      }
    } catch (ConfigException e) {
      throw new ConfigException("IrcSourceTask couldn't start due to configuration exception: ", e);
    }
  }

  @Override
  public List<SourceRecord> poll() throws InterruptedException {
    List<SourceRecord> records = new LinkedList<>();
//    Queue will block until there are items in it.
    if (queue.isEmpty()) records.add(queue.take());
    queue.drainTo(records);
    return records;
  }

  @Override
  public void stop() {
    //TODO: Do whatever is required to stop your task.
    for(String channel: this.channels){
      this.connection.doPart(channel);
    }

    this.connection.interrupt();

    try {
      this.connection.join();
    } catch (InterruptedException e) {
      throw new RuntimeException("Problem shutting down IRC connection: " + this.ircServer + ":" + this.ircPort);
    }

    if(this.connection.isAlive()) {
      throw new RuntimeException("Could not shut down IRC connection!");
    }

    this.queue.clear();

  }

  class IrcMessageEvent extends IRCEventAdapter {
    @Override
    public void onPrivmsg(String channel, IRCUser user, String message) {
//      Message date
      Date timestamp = new Date();

      IrcUser ircUser = new IrcUser(user.getNick(), user.getUsername(), user.getHost());

      IrcMessage ircMessage = new IrcMessage(
          timestamp,
          channel,
          ircUser,
          message);
// Since "resuming" isn't really a thing you can do with IRC these are simply empty maps.
      Map<String, ?> srcOffset = ImmutableMap.of();
      Map<String, ?> srcPartition = ImmutableMap.of();

      SourceRecord record = new SourceRecord(
          srcPartition,
          srcOffset,
          topic,
          KEY_SCHEMA,
          channel,
          IrcMessage.SCHEMA,
          ircMessage);

      queue.offer(record);

    }
  }
}