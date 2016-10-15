package org.cmatta.kafka.connect.irc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Joiner;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigException;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.source.SourceConnector;
import org.apache.kafka.connect.util.ConnectorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IrcSourceConnector extends SourceConnector {
  private static Logger log = LoggerFactory.getLogger(IrcSourceConnector.class);
  private IrcSourceConnectorConfig config;
  private Map<String, String> originalProps;

  @Override
  public String version() {
    return VersionUtil.getVersion();
  }

  @Override
  public void start(Map<String, String> props) {
    try {
      originalProps = props;
      config = new IrcSourceConnectorConfig(props);
    } catch (ConfigException e) {
      throw new ConfigException("IrcConnector couldn't start because of an error in the configuration: ", e);
    }
  }

  @Override
  public Class<? extends Task> taskClass() {
    return IrcSourceTask.class;
  }

  @Override
  public List<Map<String, String>> taskConfigs(int maxTasks) {
    List<String> channels = config.getIrcChannels();
    // Determine the smaller number: number of channels or maxTasks
    int numGroups = Math.min(channels.size(), maxTasks);
    // Group the channels
    List<List<String>> channelsGrouped = ConnectorUtils.groupPartitions(channels, numGroups);
    List<Map<String, String>> taskConfigs = new ArrayList<>();
    for(List<String> taskChannels: channelsGrouped) {
      Map<String, String> taskProps = new HashMap<>(originalProps);
      taskProps.put(IrcSourceTaskConfig.IRC_CHANNELS_CONF, Joiner.on(",").join(taskChannels));
      taskConfigs.add(taskProps);
    }
    return taskConfigs;
  }

  @Override
  public void stop() {
    //TODO: Do things that are necessary to stop your connector.
  }

  @Override
  public ConfigDef config() {
    return IrcSourceConnectorConfig.conf();
  }
}
