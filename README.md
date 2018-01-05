### Kafka Connect IRC source connector
A Kafka Connect source connector for Internet Relay Chat


#### Configuration Properties

|Property|Description|
|--------|-----------|
|irc.server | The IRC Server to connect to.|
|irc.server.port | The port of the IRC server to connect to. If not included defaults to 6697|
|irc.password | Password for connecting to the IRC server |
|irc.channels | Comma separated list of IRC channels.|
|irc.bot.name | The name of the IRC bot in the channel, defaults to KafkaConnectBot.|
|kafka.topic | Topic to save IRC messages to.|

#### Running in development

```
mvn clean package
export CLASSPATH="$(find target/ -type f -name '*.jar'| grep '\-package' | tr '\n' ':')"
$CONFLUENT_HOME/bin/connect-standalone $CONFLUENT_HOME/etc/schema-registry/connect-avro-standalone.properties config/kafka-connect-irc-source.properties
```

##### License
Copyright © 2016 Christopher Matta (chris.matta@gmail.com)

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
