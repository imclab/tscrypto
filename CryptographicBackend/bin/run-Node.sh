#!/bin/sh -x

LOG4J_CONF="conf/log4j.properties"
NODE_CONF="conf/node$1.properties"
RABBITMQ_CONF="conf/rabbitmq.properties"

java -cp *.jar -Dlog4j.configuration=$LOG4J_CONF -Dcl.niclabs.tscrypto.node.config=$NODE_CONF -Dcl.niclabs.tscrypto.rabbitmq.config=$RABBITMQ_CONF cl.niclabs.tscrypto.node.NodeController
