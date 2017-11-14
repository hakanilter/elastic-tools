#!/bin/bash
source ./env.sh

COMMAND=$1
if [ -z "$COMMAND" ]
  then
    echo "No COMMAND argument supplied"
    exit
fi

INDEX=$2
if [ -z "$INDEX" ]
  then
    echo "No INDEX argument supplied"
    exit
fi

PATH=$3
if [ -z "$PATH" ]
  then
    echo "No PATH argument supplied"
    exit
fi

java \
    ${OPTIONS} \
    -Dspark.master=${SPARK_MASTER} \
    -cp ${LIB_PATH} \
    com.datapyro.elastic.tool.ElasticTool \
    export \
    "path=${PATH}" \
    "es.index=${INDEX}" \
    "es.node=${ELASTIC_NODES}" \
    "es.port=${ELASTIC_PORT}"
