#!/bin/bash

CONNECT_HOST=localhost

if [[ $1 ]];then
    CONNECT_HOST=$1
fi

HEADER="Content-Type: application/json"
echo "curl -X DELETE -H \"${HEADER}\"  http://${CONNECT_HOST}:8083/connectors/wikipedia"
curl -X DELETE -H "${HEADER}"  http://${CONNECT_HOST}:8083/connectors/wikipedia
echo
