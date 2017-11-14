package com.datapyro.spark

import org.apache.spark.sql.{SaveMode, SparkSession}

object SparkElasticExportJob {

  def run(options: Map[String, String]) = {
    val start = System.currentTimeMillis()
    println("Starting backup " + options)

    // initialize context
    val spark = SparkSession.builder
      .master(Option(System.getProperty("spark.master")).getOrElse("local[*]"))
      .appName(getClass.getSimpleName)
      .getOrCreate()

    // read index data
    val defaultOptions = Map(
      "es.nodes.wan.only" -> "true",
      "pushdown" -> "true",
      "es.mapping.id" -> "id"
    )
    val elasticOptions = defaultOptions ++ options.filter(_._1.startsWith("es."))

    // save as parquet
    spark.read
      .format("org.elasticsearch.spark.sql")
      .options(options)
      .load(options("index"))
      .repartition(options.getOrElse("partitions", "10").toInt)
      .write
      .mode(SaveMode.Overwrite)
      .parquet(options("path"))

    println("Backup completed in " + (System.currentTimeMillis() - start) + " ms")
  }

}
