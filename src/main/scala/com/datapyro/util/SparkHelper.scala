package com.datapyro.util

import org.apache.spark.sql.SparkSession

object SparkHelper {

  def getSpark: SparkSession = {
    val master = Option(System.getProperty("spark.master")).getOrElse("local[*]")
    println("Using Spark master: " + master)
    SparkSession.builder
      .master(master)
      .appName(getClass.getSimpleName)
      .config("spark.hadoop.fs.file.impl", classOf[org.apache.hadoop.fs.LocalFileSystem].getName)
      .getOrCreate()
  }

  def getOptions(options: Map[String, String]): Map[String, String] = {
    val defaultOptions = Map(
      "es.nodes.wan.only" -> "true",
      "pushdown" -> "true",
      "es.mapping.id" -> "id"
    )
    defaultOptions ++ options.filter(_._1.startsWith("es."))
  }

}
