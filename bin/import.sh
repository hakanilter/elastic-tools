#!/bin/bash
cd "$(dirname "$0")" || exit
source ./env.sh

PARTITIONS="1024"
INDEX=$1
if [[ -z "${INDEX}" ]]; then
  echo "ERROR! Required an index name as a parameter"
  exit 1
fi

# Import
echo "Import (ElasticSearch <- Storage) is running for [${INDEX}]"
./elastic-tool import "path=${TEMP_FOLDER}/${INDEX}" "format=parquet" "partitions=${PARTITIONS}" "es.index=${INDEX}" "es.nodes=${ELASTIC_NODES}" "es.port=${ELASTIC_PORT}"
