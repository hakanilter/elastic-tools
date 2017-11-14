#!/bin/bash
source ./env.sh

INDEX=$1
if [ -z "$INDEX" ]
  then
    echo "No INDEX argument supplied"
    exit
fi

PATH=$2
if [ -z "$PATH" ]
  then
    echo "No PATH argument supplied"
    exit
fi

java \
    ${OPTIONS} \
    -Dspark.master=${SPARK_MASTER} \
    -Delastic.host=${ELASTIC_HOST} \
    -Delastic.port=${ELASTIC_PORT} \
    -Delastic.index=${INDEX} \
    -cp ${LIB_PATH} \
    com.datapyro.elastic.tool.BackupTool restore ${PATH}
