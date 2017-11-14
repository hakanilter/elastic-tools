package com.datapyro.spark

import org.apache.spark.sql.SparkSession
import org.elasticsearch.spark.sql.EsSparkSQL

object SparkElasticImportJob {

  def run(options: Map[String, String]) = {
    val start = System.currentTimeMillis()
    println("Starting restore " + options)

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

    // load parquet
    val df = spark.read.parquet(options("path"))
      .repartition(options.getOrElse("partitions", "10").toInt)

    // save to es
    EsSparkSQL.saveToEs(df, options("index"), elasticOptions)

    println("Restore completed in " + (System.currentTimeMillis() - start) + " ms")
  }

}
