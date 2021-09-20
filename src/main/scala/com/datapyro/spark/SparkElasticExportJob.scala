package com.datapyro.spark

import com.datapyro.util.SparkHelper
import org.apache.spark.sql.SaveMode

object SparkElasticExportJob {

  def run(options: Map[String, String]): Unit = {
    val start = System.currentTimeMillis()
    println("Starting export job with options: " + options)

    val spark = SparkHelper.getSpark
    val elasticOptions = SparkHelper.getOptions(options)

    // prepare data frame
    println("elastic options: " + elasticOptions)
    val df = spark.read
      .format("org.elasticsearch.spark.sql")
      .options(elasticOptions)
      .load(options("es.index"))
      .repartition(options.getOrElse("partitions", "10").toInt)
      .write
      .mode(SaveMode.Overwrite)

    val result = if (options.contains("compression")) {
      df.option("compression", options("compression"))
    } else {
      df
    }

    options.getOrElse("format", "parquet") match {
      case "parquet" => result.parquet(options("path"))
      case "json" => result.json(options("path"))
      case "csv" => result.csv(options("path"))
    }

    println("Backup completed in " + (System.currentTimeMillis() - start) + " ms")
  }

}
