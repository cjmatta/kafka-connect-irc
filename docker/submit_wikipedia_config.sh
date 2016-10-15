#!/bin/bash

CONNECT_HOST=localhost
HEADER="Content-Type: application/json"
DATA=$( cat << EOF
{
  "name": "wikipedia",
  "config": {
      "connector.class": "org.cmatta.kafka.connect.irc.IrcSourceConnector",
      "irc.server": "irc.wikimedia.org",
      "kafka.topic": "wikipedia",
      "irc.channels": "#en.wikipedia",
      "tasks.max": "1"
  }
}
EOF
)

echo "curl -X POST -H \"${HEADER}\" --data \"${DATA}\" http://${CONNECT_HOST}:8083/connectors"
curl -X POST -H "${HEADER}" --data "${DATA}" http://${CONNECT_HOST}:8083/connectors