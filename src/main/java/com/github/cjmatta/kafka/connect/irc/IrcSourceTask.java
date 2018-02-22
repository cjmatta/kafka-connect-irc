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

import com.google.common.collect.ImmutableMap;
import org.apache.kafka.common.config.ConfigException;
import org.apache.kafka.common.config.types.Password;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.source.SourceTask;
import org.schwering.irc.lib.IRCConnection;
import org.schwering.irc.lib.IRCEventAdapter;
import org.schwering.irc.lib.IRCUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.transform.Source;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class IrcSourceTask extends SourceTask {
  static final Logger log = LoggerFactory.getLogger(IrcSourceTask.class);

  private static final String TIMESTAMP_FIELD = "timestamp";
  private static final String CHANNEL_FIELD = "channel";
  private static final Schema KEY_SCHEMA = Schema.STRING_SCHEMA;

  IrcSourceTaskConfig config;

  private String ircServer;
  private List<String> channels;
  private int ircPort;
  private String ircBotName;
  private Password ircPassword;

  private final AtomicBoolean running = new AtomicBoolean(false);

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
      queue = new LinkedBlockingQueue<SourceRecord>();
      ircServer = config.getIrcServer();
      ircPort = config.getIrcServerPort();
      ircBotName = config.getIrcBotName();
      ircPassword = config.getIrcPassword();
      channels = config.getIrcChannels();
      topic = config.getKafkaTopic();

      connection = new IRCConnection(ircServer, new int[]{ircPort}, ircPassword.value(), ircBotName, ircBotName, ircBotName);
      connection.addIRCEventListener(new IrcMessageEvent());
      connection.setEncoding("UTF-8");
      connection.setPong(true);
      connection.setColors(false);

      connectAndJoin();

      log.debug("Set running state to true");
      running.set(true);
    } catch (ConfigException e) {
      throw new ConfigException("IrcSourceTask couldn't start due to configuration exception: ", e);
    }
  }

  @Override
  public List<SourceRecord> poll() throws InterruptedException {
    List<SourceRecord> records = new LinkedList<>();

    while(running.get()) {
//      Check to see if the IRC connection has disconnected
      if (!connection.isConnected()) {
        connectAndJoin();
      }

      // Poll for new records but only for a max amount of time!
      SourceRecord record = queue.poll(1L, TimeUnit.SECONDS);
      if (record == null) {
        // the queue was empty, so continue looping ...
        log.debug("Empty queue, looping...");
        continue;
      }

      records.add(record);
      queue.drainTo(records);
      log.debug("Returning " + records.size() + " records.");
      return records;
    }

    return records;
  }

  @Override
  public void stop() {
    if (log.isDebugEnabled()) log.debug("Set running state to false.");
    running.set(false);

    if(connection.isConnected()) {
      for (String channel : this.channels) {
        if (log.isDebugEnabled()) log.debug("Leaving channel: " + channel);
        this.connection.doPart(channel);
      }
      if (log.isDebugEnabled()) log.debug("Shutting down IRC connection to server...");
      this.connection.interrupt();
      if (log.isDebugEnabled()) log.debug("IRC server disconnect complete.");
    }

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

//
  private void connectAndJoin() throws ConnectException {
    if(connection.isConnected()) {
      if(log.isInfoEnabled()) log.info("Already connected!");
      return;
    }
    if(log.isInfoEnabled()) log.info("Connecting to server: {}", config.getIrcServer());
    try {
      connection.connect();
    } catch (IOException e) {
      throw new ConnectException("Unable to connect to server: " + this.ircServer);
    }

    for (String channel : config.getIrcChannels()) {
      if(log.isInfoEnabled()) log.info("Joining channel: {}", channel);
      try {
        connection.doJoin(channel);
      } catch (Exception e) {
        throw new ConnectException("Problem joining channel " + channel);
      }

    }

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