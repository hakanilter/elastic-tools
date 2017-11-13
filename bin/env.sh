#!/bin/bash

# configuration
export SPARK_MASTER="local[*]"
export ELASTIC_HOST="localhost"
export ELASTIC_PORT="9200"
export LIB_PATH=../target/*-dist.jar
export OPTIONS="-Dspark.sql.warehouse.dir=/tmp/spark-warehouse"
