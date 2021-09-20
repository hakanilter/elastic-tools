#!/bin/bash

SOURCE_HOST="http://localhost"
SOURCE_PORT="9200"
TARGET_HOST="http://localhost"
TARGET_PORT="9200"
COMPRESSION="snappy"

SOURCE_INDEX=$1
if [[ -z "${SOURCE_INDEX}" ]]; then
  echo "ERROR! Required a source index name as a parameter"
  exit 1
fi

TARGET_INDEX=$2
if [[ -z "${TARGET_INDEX}" ]]; then
  echo "ERROR! Required a target index name as a parameter"
  exit 1
fi


# Export
echo "Export (ElasticSearch -> Storage) is running for [${SOURCE_INDEX}]"
./elastic-tool export "path=${TEMP_FOLDER}/${SOURCE_INDEX}" "compression=${COMPRESSION}" "es.index=${SOURCE_INDEX}" "es.nodes=${SOURCE_HOST}" "es.port=${SOURCE_PORT}"

# Import
echo "Import (ElasticSearch <- Storage) is running for [${TARGET_INDEX}]"
./elastic-tool import "path=${TEMP_FOLDER}/${TARGET_INDEX}" "format=parquet" "partitions=${PARTITIONS}" "es.index=${TARGET_INDEX}" "es.nodes=${TARGET_HOST}" "es.port=${TARGET_PORT}"

echo "Done!"
