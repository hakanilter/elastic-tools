package com.datapyro.elastic.tool

import com.datapyro.spark.{SparkElasticBackupJob, SparkElasticRestoreJob}

object BackupTool extends App {

  // get commands
  if (args.length != 2) {
    showError()
  }
  val command = args(0)
  val path = args(1)

  // options
  val options = Map(
    "master" -> Option(System.getProperty("spark.master")).getOrElse("local[*]") ,
    "host" -> Option(System.getProperty("elastic.host")).getOrElse(""),
    "port" -> Option(System.getProperty("elastic.port")).getOrElse("9200"),
    "index" -> Option(System.getProperty("elastic.index")).getOrElse(""),
    "idField" -> Option(System.getProperty("elastic.id")).getOrElse("id"),
    "partitions" -> Option(System.getProperty("elastic.partitions")).getOrElse("10"),
    "path" -> Option(path).getOrElse("")
  )

  // validate options
  val invalidOptions = options("host") == "" || options("index") == "" || options("path") == ""   
  if (invalidOptions) {
    showError()
  }

  // run command
  command match {
    case "backup" => SparkElasticBackupJob.run(options)
    case "restore" => SparkElasticRestoreJob.run(options)
    case _ => showError()
  }

  def showError() = {
    println("Invalid options, try:")
    println("\tbackup <path> --Dhost=localhost -Dindex=index1/type1 ...")
    println("\trestore <path> --Dhost=localhost -Dindex=index1/type1 ...")
    System.exit(-1)
  }

}
