package org.cmatta.kafka.connect.irc.util;

import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaBuilder;
import org.apache.kafka.connect.data.Struct;

/**
 * IrcMessageCreator
 * Created by chris on 10/14/16.
 */
public class IrcMessageCreator {
  public static final Schema messageSchema;
  public static final Schema messageKeySchema;

  static {
    messageSchema = SchemaBuilder.struct()
        .name("org.cmatta.kafka.connect.irc.message")
        .doc("Basic IRC message.")
        .field("Channel", SchemaBuilder.string().doc("The channel to which the message was sent.").required().build())
        .field("Sender", SchemaBuilder.string().doc("The nick of the person who sent the message.").required().build())
        .field("Login", SchemaBuilder.string().doc("The login of the person who sent the message.").required().build())
        .field("Hostname", SchemaBuilder.string().doc("The hostname of the person who sent the message.").required().build())
        .field("Message", SchemaBuilder.string().doc("The actual message sent to the channel.").required().build());

    messageKeySchema = SchemaBuilder.struct()
        .name("org.cmatta.kafka.connect.irc.messagekey")
        .doc("Key for IRC messages")
        .field("Channel", SchemaBuilder.string().doc("IRC Channel message came from.").required().build());
  }

  public static void create(String channel, String sender, String login, String hostname, String message, Struct struct) {
    struct
        .put("Channel", channel)
        .put("Sender", sender)
        .put("Login", login)
        .put("Hostname", hostname)
        .put("Message", message);
  }

  public static void createKey(String channel, Struct struct) {
    struct
        .put("Channel", channel);
  }
}
