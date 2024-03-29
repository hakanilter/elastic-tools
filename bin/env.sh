#!/bin/bash

# configuration
export SPARK_MASTER="local[*]"
export ELASTIC_NODES="https://localhost"
export ELASTIC_PORT="9200"
export LIB_PATH=../target/*-dist.jar
export OPTIONS="-Dspark.master=${SPARK_MASTER} -Dspark.sql.warehouse.dir=/tmp/spark-warehouse -Des.nodes.wan.only=true -Des.mapping.date.rich=false"
export TEMP_FOLDER="/tmp/elastic"
