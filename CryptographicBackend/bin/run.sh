#!/bin/bash -x

CONF_DIR=conf
LIBRARY_PATH="../lib/"
LOG4J_CONF=$CONF_DIR/log4j.properties
SD_CONF=$CONF_DIR/sd.properties
RABBITMQ_CONF=$CONF_DIR/rabbitmq.properties

java -Djava.library.path=$LIBRARY_PATH -Dlog4j.configuration=$LOG4J_CONF -Dcl.niclabs.tscrypto.sd.config=$SD_CONF -Dcl.niclabs.tscrypto.rabbitmq.config=$RABBITMQ_CONF -cp *.jar cl.niclabs.cb.tscrypto.Backend
