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

/**
 * Created by chris on 10/22/16.
 */
public class IrcUser extends Struct {
  private static final String NICK = "nick";
  private static final String LOGIN = "login";
  private static final String HOSTNAME = "hostname";

  final public static Schema SCHEMA = SchemaBuilder.struct()
      .name("com.github.cjmatta.kafka.connect.irc.User")
      .field(NICK, SchemaBuilder.string().doc("The nick of the person who sent the message.").required().build())
      .field(LOGIN, SchemaBuilder.string().doc("The login of the person who sent the message.").required().build())
      .field(HOSTNAME, SchemaBuilder.string().doc("The hostname of the person who sent the message.").required().build());

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
