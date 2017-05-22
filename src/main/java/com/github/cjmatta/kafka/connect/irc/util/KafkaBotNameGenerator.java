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
