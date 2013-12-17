#!/bin/sh -x

LOG4J_CONF="conf/log4j.properties"
NODE_CONF="conf/node$1.properties"
RABBITMQ_CONF="conf/rabbitmq.properties"

java -cp *.jar -Dlog4j.configuration=$LOG4J_CONF -Dcl.inria.tscrypto.node.config=$NODE_CONF -Dcl.inria.tscrypto.rabbitmq.config=$RABBITMQ_CONF cl.inria.tscrypto.node.NodeController
