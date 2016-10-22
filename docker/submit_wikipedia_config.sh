#!/bin/bash

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
      "connector.class": "org.cmatta.kafka.connect.irc.IrcSourceConnector",
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
