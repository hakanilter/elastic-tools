#!/bin/bash
cd "$(dirname "$0")" || exit
source ./env.sh

COMPRESSION="snappy"
INDEX=$1
if [[ -z "${INDEX}" ]]; then
  echo "ERROR! Required an index name as a parameter"
  exit 1
fi

# Export
echo "Export (ElasticSearch -> Storage) is running for [${INDEX}]"
./elastic-tool export "path=${TEMP_FOLDER}/${INDEX}" "compression=${COMPRESSION}" "es.index=${INDEX}" "es.nodes=${ELASTIC_NODES}" "es.port=${ELASTIC_PORT}"
