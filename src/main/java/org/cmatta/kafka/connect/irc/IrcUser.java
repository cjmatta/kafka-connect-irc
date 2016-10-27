package org.cmatta.kafka.connect.irc.util;

import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaBuilder;
import org.apache.kafka.connect.data.Struct;

/**
 * Created by chris on 10/22/16.
 */
public class IrcUser extends Struct {
  final private static String NICK = "nick";
  final private static String LOGIN = "login";
  final private static String HOSTNAME = "hostname";

  final public static Schema SCHEMA = SchemaBuilder.struct()
      .name("org.cmatta.kafka.connect.irc.user")
      .field(NICK, SchemaBuilder.string().doc("The nick of the person who sent the message.").required().build())
      .field("login", SchemaBuilder.string().doc("The login of the person who sent the message.").required().build())
      .field("hostname", SchemaBuilder.string().doc("The hostname of the person who sent the message.").required().build());

  public IrcUser(String nick, String login, String hostname) {
    super(SCHEMA);
    this
        .put(NICK, nick)
        .put(LOGIN, login)
        .put(HOSTNAME, hostname);
  }

  @Override
  public String toString() {
    return String.format("Nick: %s, Login: %s, Hostname: %s", this.get(NICK), this.get(LOGIN), this.get(HOSTNAME));
  }
}
