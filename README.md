# Elastic Tools

Apache Spark based command line tools for ElasticSearch. It supports backup, restore features and diffrent data formats like Parquet, JSON and CSV.

- **Export** (ElasticSearch -> Storage)

	Export command reads data from given ElasticSearch index and writes to a storage with a batch Spark job.
	
    | Option | Description |
    | --- | --- |
    | path | Path of the storage system (required) |
    | es.index |  Name of the ElasticSearch index (required) | 
    | es.nodes |  ElasticSearch hostname (required) | 
    | es.port |  ElasticSearch port number (default: 9200) | 
    | es.mapping.id |  Name of the id field in given dataset (default: id) | 
    | format |  The storage format, parquet, json or csv (default: parquet) | 
    | partitions |  Number of partitions to be used during the write operation (default: 10) |
    | compression | Supported compression formats are gzip, bzip2, snappy (default: none) |  

Example:
    ./elastic-tool export "es.index=test/doc" "es.nodes=localhost" "path=/tmp/elastic"    

- **Import** (ElasticSearch <- Storage)

	Import command reads data from a storage and writes to ElasticSearch.

    | Option | Description |
    | --- | --- |
    | path | Path of the storage system (required) |
    | es.index |  Name of the ElasticSearch index (required) | 
    | es.nodes |  ElasticSearch hostname (required) | 
    | es.port |  ElasticSearch port number (default: 9200) | 
    | es.mapping.id |  Name of the id field in given dataset (default: id) | 
    | format |  The storage format, parquet or json (default: parquet) | 
    | partitions |  Number of partitions to be used during the write operation (default: 10) | 
    | compression | Supported compression formats are gzip, bzip2, snappy (default: none) |
