package org.cmatta.kafka.connect.irc;

import com.google.common.collect.ImmutableMap;
import org.apache.kafka.common.config.ConfigException;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.source.SourceTask;
import org.cmatta.kafka.connect.irc.util.IrcMessageCreator;
import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class IrcSourceTask extends SourceTask {
  static final Logger log = LoggerFactory.getLogger(IrcSourceTask.class);
  IrcSourceTaskConfig config;
  IrcBot ircBot = new IrcBot();

  @Override
  public String version() {
    return VersionUtil.getVersion();
  }

  @Override
  public void start(Map<String, String> props) {
    //TODO: Do things here that are required to start your task. This could be open a connection to a database, etc.
    try {
      config = new IrcSourceTaskConfig(props);
      ircBot.connect(config.getIrcServer(), config.getIrcServerPort());
      for (String channel : config.getIrcChannels()) {
        ircBot.joinChannel(channel);
      }
    } catch (ConfigException e) {
      throw new ConfigException("IrcSourceTask couldn't start due to configuration exception: ", e);
    } catch (NickAlreadyInUseException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (IrcException e) {
      e.printStackTrace();
    }
  }

  @Override
  public List<SourceRecord> poll() throws InterruptedException {
    List<SourceRecord> records = new ArrayList<>(256);

    Map<String, ?> sourcePartition = ImmutableMap.of();
    Map<String, ?> sourceOffset = ImmutableMap.of();

    while (records.isEmpty()) {
      int size = ircBot.messageQueue.size();

      for (int i = 0; i < size; i++) {
        Struct record = this.ircBot.messageQueue.poll();

        if (null == record) {
          break;
        }

        Struct recordKey = new Struct(IrcMessageCreator.messageKeySchema);
        IrcMessageCreator.createKey(record.getString("Channel"), recordKey);

        SourceRecord sourceRecord = new SourceRecord(sourcePartition,
            sourceOffset,
            this.config.getKafkaTopic(),
            IrcMessageCreator.messageKeySchema,
            recordKey,
            IrcMessageCreator.messageSchema,
            record);

        records.add(sourceRecord);
      }

      if (records.isEmpty()) {
        Thread.sleep(100);
      }
    }

    return records;
  }

  @Override
  public void stop() {
    //TODO: Do whatever is required to stop your task.
    ircBot.disconnect();
  }
}