package com.datapyro.spark

import org.apache.spark.sql.SparkSession
import org.elasticsearch.spark.sql.EsSparkSQL

object SparkElasticRestoreJob {

  def run(elasticConfig: Map[String, String]) = {
    val start = System.currentTimeMillis()
    println("Starting restore " + elasticConfig)

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

    // load parquet
    val df = spark.read.parquet(elasticConfig("path"))
      .repartition(elasticConfig.getOrElse("partitions", "10").toInt)

    // save to es
    EsSparkSQL.saveToEs(df, elasticConfig("index"), options)

    println("Restore completed in " + (System.currentTimeMillis() - start) + " ms")
  }

}
