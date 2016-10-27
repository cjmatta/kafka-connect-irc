package org.cmatta.kafka.connect.irc.util;

import java.security.SecureRandom;

/**
 * Created by chris on 9/27/16.
 */
public class RandomStringGenerator {
  static final String ABC = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
  static final SecureRandom rand = new SecureRandom();

  public static String random(int length) {
    StringBuilder sb = new StringBuilder(length);

    for (int i = 0; i < length; i++) {
      sb.append(ABC.charAt(rand.nextInt(ABC.length())));
    }

    return sb.toString();
  }
}
