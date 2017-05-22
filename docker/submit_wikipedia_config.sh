#!/bin/bash
#
# Copyright © 2016 Christopher Matta (chris.matta@gmail.com)
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#


CONNECT_HOST=localhost

if [[ $1 ]];then
    CONNECT_HOST=$1
fi

HEADER="Content-Type: application/json"
DATA=$( cat << EOF
{
  "name": "wikipedia",
  "config": {
      "producer.interceptor.class": "io.confluent.monitoring.clients.interceptor.MonitoringProducerInterceptor",
      "connector.class": "com.github.cjmatta.kafka.connect.irc.IrcSourceConnector",
      "irc.server": "irc.wikimedia.org",
      "kafka.topic": "wikipedia",
      "irc.channels": "#en.wikipedia,#en.wiktionary",
      "tasks.max": "2"
  }
}
EOF
)

echo "curl -X POST -H \"${HEADER}\" --data \"${DATA}\" http://${CONNECT_HOST}:8083/connectors"
curl -X POST -H "${HEADER}" --data "${DATA}" http://${CONNECT_HOST}:8083/connectors
echo
