package com.datapyro.spark

import com.datapyro.util.SparkHelper
import org.elasticsearch.spark.sql.EsSparkSQL

object SparkElasticImportJob {

  def run(options: Map[String, String]): Unit = {
    val start = System.currentTimeMillis()
    println("Starting restore job with options: " + options)

    val spark = SparkHelper.getSpark
    val elasticOptions = SparkHelper.getOptions(options)

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
