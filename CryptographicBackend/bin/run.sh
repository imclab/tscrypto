#!/bin/bash -x

CONF_DIR=conf
LOG4J_CONF=$CONF_DIR/log4j.properties
SD_CONF=$CONF_DIR/sd.properties
RABBITMQ_CONF=$CONF_DIR/rabbitmq.properties

java -Dlog4j.configuration=$LOG4J_CONF -Dcl.niclabs.tscrypto.sd.config=$SD_CONF -Dcl.niclabs.tscrypto.rabbitmq.config=$RABBITMQ_CONF -cp *.jar cl.niclabs.cb.tscrypto.Backend
