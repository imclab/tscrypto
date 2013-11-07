#!/bin/bash -x

CONF_DIR=src/main/resources/conf
LOG4J_CONF=$CONF_DIR/log4j.properties
SD_CONF=$CONF_DIR/sd.properties
RABBITMQ_CONF=$CONF_DIR/rabbitmq.properties

java -Dlog4j.configuration=$LOG4J_CONF -Dcl.inria.tscrypto.sd.config=$SD_CONF -Dcl.inria.tscrypto.rabbitmq.config=$RABBITMQ_CONF -cp target/*.jar cl.niclabs.cb.jcrypto.Backend
