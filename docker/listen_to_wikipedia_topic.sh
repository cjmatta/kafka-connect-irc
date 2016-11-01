#!/bin/bash

docker exec docker_connect_1 kafka-avro-console-consumer --property schema.registry.url=http://schemaregistry:8081 \
    --property consumer.interceptor.classes=io.confluent.monitoring.clients.interceptor.MonitoringConsumerInterceptor --bootstrap-server kafka:9092 --topic wikipedia --from-beginning --new-consumer
