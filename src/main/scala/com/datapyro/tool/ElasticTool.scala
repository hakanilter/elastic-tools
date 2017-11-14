package com.datapyro.tool

import java.io.InputStream

import com.datapyro.spark.{SparkElasticExportJob, SparkElasticImportJob}

object ElasticTool extends App {

  if (args.length == 0) {
    showError()
  }

  val command = args(0)
  val options = args.map(arg => (arg.split("=")(0) -> arg.split("=")(1))).toMap

  // check mandatory options
  val mandatoryFields = Array("es.nodes", "es.index", "path")
  val errorCount = mandatoryFields.map(field => if (options.contains(field)) 0 else 1).sum
  if (errorCount > 0) {
    showError()
  }

  // run command
  command match {
    case "export" => SparkElasticExportJob.run(options)
    case "import" => SparkElasticImportJob.run(options)
    case _ => showError()
  }

  def showError() = {
    val stream: InputStream = getClass.getResourceAsStream("/usage.txt")
    val usage = scala.io.Source.fromInputStream(stream).getLines.mkString("\n")
    println("ERROR! Invalid options")
    println(usage)
    System.exit(-1)
  }

}
