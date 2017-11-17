# Elastic Tools

**Elastic Tools** is an [Apache Spark](https://github.com/apache/spark) based command line tools designed to scalable export / import support for ElasticSearch. 
  
It supports different storage types like File System, HDFS, AWS S3, Azure Blob Storage and storage formats like Parquet, JSON and CSV. 

**Supported features:**

- **Export** (ElasticSearch -> Storage)

	Export command reads data from given ElasticSearch index(es) and writes to a storage with a batch Spark job.
	
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
    
    ```
    $ ./elastic-tool export "es.index=test/doc" "es.nodes=localhost" "path=/tmp/elastic"
    Starting export job with options: Map(es.index -> test/doc, es.nodes -> localhost, path -> /tmp/elastic)
    2017-11-17 00:17:39 WARN  SparkContext:66 - Support for Scala 2.10 is deprecated as of Spark 2.1.0
    2017-11-17 00:17:39 WARN  NativeCodeLoader:62 - Unable to load native-hadoop library for your platform... using builtin-java classes where applicable
    elastic options: Map(es.index -> test/doc, es.nodes.wan.only -> true, es.mapping.id -> id, pushdown -> true, es.nodes -> localhost)
    [Stage 1:>                                                         (0 + 4) / 10]
    Backup completed in 36862 ms    
    ```            

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
    | partitions |  Number of partitions to be used during the read operation (default: 10) | 
    | compression | Supported compression formats are gzip, bzip2, snappy (default: none) |

    Example:
            
    ```
    $ ./elastic-tool import "es.index=test/doc" "es.nodes=localhost" "path=/tmp/elastic"
    Starting restore job with options: Map(es.index -> test/doc, es.nodes -> localhost, path -> /tmp/elastic)
    2017-11-17 00:19:09 WARN  SparkContext:66 - Support for Scala 2.10 is deprecated as of Spark 2.1.0
    2017-11-17 00:19:09 WARN  NativeCodeLoader:62 - Unable to load native-hadoop library for your platform... using builtin-java classes where applicable
    elastic options: Map(es.index -> test/doc, es.nodes.wan.only -> true, es.mapping.id -> id, pushdown -> true, es.nodes -> localhost)
    Restore completed in 67540 ms
    ```
    