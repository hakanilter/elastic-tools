package com.datapyro.spark

import org.apache.spark.sql.{SaveMode, SparkSession}

object SparkElasticBackupJob {

  def run(elasticConfig: Map[String, String]) = {
    val start = System.currentTimeMillis()
    println("Starting backup " + elasticConfig)

    // initialize context
    val spark = SparkSession.builder
      .master(elasticConfig.getOrElse("master", "local[*]"))
      .appName(getClass.getSimpleName)
      .getOrCreate()

    // read index data
    val options = Map(
      "es.nodes" -> elasticConfig.getOrElse("host", "localhost"),
      "es.port" -> elasticConfig.getOrElse("port", "9200"),
      "es.mapping.id" -> elasticConfig.getOrElse("idField", "id"),
      "es.nodes.wan.only" -> "true",
      "pushdown" -> "true"
    )

    // save as parquet
    spark.read
      .format("org.elasticsearch.spark.sql")
      .options(options)
      .load(elasticConfig("index"))
      .repartition(elasticConfig.getOrElse("partitions", "10").toInt)
      .write
      .mode(SaveMode.Overwrite)
      .parquet(elasticConfig("path"))

    println("Backup completed in " + (System.currentTimeMillis() - start) + " ms")
  }

}
