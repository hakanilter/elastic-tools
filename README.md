# Elastic Tools

Apache Spark based command line tools for ElasticSearch. It supports backup, restore features and diffrent data formats like Parquet, JSON and CSV.

- **Export** (ElasticSearch -> Storage)

	Export command reads data from given ElasticSearch index and writes to a storage with a batch Spark job.

- **Import** (ElasticSearch <- Storage)

	Import command reads data from a storage and writes to ElasticSearch.

