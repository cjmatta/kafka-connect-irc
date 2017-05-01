package com.github.cjmatta.kafka.connect.irc.util;

import java.security.SecureRandom;

/**
 * Created by chris on 9/27/16.
 */
public class KafkaBotNameGenerator {
  static final String ABC = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
  static final SecureRandom rand = new SecureRandom();

  public static String generateBotName(String prefix) {
    StringBuilder sb = new StringBuilder(6);

    for (int i = 0; i < 6; i++) {
      sb.append(ABC.charAt(rand.nextInt(ABC.length())));
    }

    return prefix + "_" + sb.toString();
  }
}
