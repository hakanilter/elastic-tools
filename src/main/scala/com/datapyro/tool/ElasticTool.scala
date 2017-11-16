package com.datapyro.tool

import java.io.InputStream

import com.datapyro.spark.{SparkElasticExportJob, SparkElasticImportJob}

object ElasticTool extends App {

  if (args.length == 0) {
    showError()
  }

  // read options
  val command = args(0)
  val options = args
      .filter(arg => arg.contains("="))
      .map(arg => arg.split("=")(0) -> arg.split("=")(1))
      .toMap

  // check mandatory options
  val mandatoryFields = Array("es.nodes", "es.index", "path")
  val errors = mandatoryFields
    .map(field => (field, !options.contains(field)))
    .filter(_._2)
  if (errors.length > 0) {
    errors.foreach(error => println("Required option not found: " + error._1))
    showError()
  }

  // check format
  val supportedFormats = Set("parquet", "json", "csv")
  if (options.contains("format") && !supportedFormats.contains(options("format"))) {
    println("Invalid format: " + options("format"))
    showError()
  }

  // run command
  command match {
    case "export" => SparkElasticExportJob.run(options)
    case "import" => SparkElasticImportJob.run(options)
    case _ => {
      println("Invalid command: " + command)
      showError()
    }
  }

  def showError() = {
    val stream: InputStream = getClass.getResourceAsStream("/usage.txt")
    val usage = scala.io.Source.fromInputStream(stream).getLines.mkString("\n")
    println("\nERROR! Please enter valid options")
    println(usage)
    System.exit(-1)
  }

}
