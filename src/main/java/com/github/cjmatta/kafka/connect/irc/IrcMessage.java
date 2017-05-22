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

import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaBuilder;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.data.Timestamp;

import java.util.Date;

/**
 * IrcMessage
 * Created by chris on 10/14/16.
 */
public class IrcMessage extends Struct {
  private static final String CREATED_AT = "createdat";
  private static final String CHANNEL = "channel";
  private static final String SENDER = "sender";
  private static final String MESSAGE = "message";

  final public static Schema SCHEMA = SchemaBuilder.struct()
        .name("com.github.cjmatta.kafka.connect.irc.Message")
        .doc("Basic IRC message.")
        .field(CREATED_AT, Timestamp.builder().doc("When this message was received.").required().build())
        .field(CHANNEL, SchemaBuilder.string().doc("The channel to which the message was sent.").required().build())
        .field(SENDER, IrcUser.SCHEMA)
        .field(MESSAGE, SchemaBuilder.string().doc("The actual message sent to the channel.").required().build());

  public IrcMessage(Date createdAt, String channel, IrcUser user, String message) {
    super(SCHEMA);
    this
        .put(CREATED_AT, createdAt)
        .put(CHANNEL, channel)
        .put(SENDER, user)
        .put(MESSAGE, message);
  }

  @Override
  public String toString() {
    return String.format("IRC Message @ %s \n\t %s \n\t Channel: %s \n\t Message: %s",
        this.get(CREATED_AT), this.get(SENDER), this.get(CHANNEL), this.get(MESSAGE));
  }
}
