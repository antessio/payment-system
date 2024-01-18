#!/bin/sh

docker-compose -f ./docker-compose-core.yml -f ../common/docker/rabbitmq.yml up -d