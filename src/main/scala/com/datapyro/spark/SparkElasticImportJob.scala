package com.datapyro.spark

import org.apache.spark.sql.SparkSession
import org.elasticsearch.spark.sql.EsSparkSQL

object SparkElasticImportJob {

  def run(options: Map[String, String]) = {
    val start = System.currentTimeMillis()
    println("Starting restore job with options: " + options)

    // initialize context
    val master = Option(System.getProperty("spark.master")).getOrElse("local[*]")
    val spark = SparkSession.builder
      .master(master)
      .appName(getClass.getSimpleName)
      .getOrCreate()

    // read index data
    val defaultOptions = Map(
      "es.nodes.wan.only" -> "true",
      "pushdown" -> "true",
      "es.mapping.id" -> "id"
    )
    val elasticOptions = defaultOptions ++ options.filter(_._1.startsWith("es."))

    // prepare data frame
    println("elastic options: " + elasticOptions)

    val format = options.getOrElse("format", "parquet")
    val input = if (format == "json") {
      spark.read.json(options("path"))
    } else if (format == "csv") {
      spark.read.csv(options("path"))
    } else {
      spark.read.parquet(options("path"))
    }

    val df = input.repartition(options.getOrElse("partitions", "10").toInt)

    // save to es
    EsSparkSQL.saveToEs(df, options("es.index"), elasticOptions)

    println("Restore completed in " + (System.currentTimeMillis() - start) + " ms")
  }

}
