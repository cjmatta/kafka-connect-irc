#!/bin/bash

docker exec docker_kafka_1 kafka-console-consumer --bootstrap-server kafka:9092 --topic wikipedia --from-beginning --new-consumer