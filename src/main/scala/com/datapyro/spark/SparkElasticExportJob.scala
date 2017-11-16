package com.datapyro.spark

import org.apache.spark.sql.{SaveMode, SparkSession}

object SparkElasticExportJob {

  def run(options: Map[String, String]) = {
    val start = System.currentTimeMillis()
    println("Starting export job with options: " + options)

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
