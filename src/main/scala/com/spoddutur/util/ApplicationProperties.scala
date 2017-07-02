package com.spoddutur.util

/**
  * Created by sruthi on 01/07/17.
  */

import com.typesafe.config.ConfigFactory

object ApplicationProperties {

  val conf = ConfigFactory.load
  val sparkMasterDef = conf.getString("sparkMaster")
  val socketStreamHostDef = conf.getString("socketStreamHost")
  val socketStreamPortDef = conf.getInt("socketStreamPort")
  val batchIntervalDef = conf.getInt("batchInterval")
  val lightningServerUrlDef = conf.getString("lightningServerUrl")
  val appNameDef = conf.getString("appName")

  var batchInterval = batchIntervalDef
  var socketStreamPort = socketStreamPortDef
  var socketStreamHost = socketStreamHostDef
  var sparkMaster = sparkMasterDef
  var lightningServerUrl = lightningServerUrlDef
  var appName = appNameDef

  def main(args: Array[String]): Unit = {
    parse("-n Sruthi -ls some-lightning-server".split(" ").toList)
    print(sparkMaster, appName, lightningServerUrl, socketStreamPort, batchInterval)
  }

  val usage =
    s"""
This is a Spark Streaming application which receives data from SocketStream and does word count.
You can monitor batch size and batch processing time by real-time graph that's rendered using
Lightning graph server. So, this application needs lightningServerUrl and SocketStreamHost
and Port from where to listen to..
Usage: spark-submit realtime-spark-monitoring-with-lightning*.jar [options]

  Options:
  -h, --help
  -m, --master <master_url>                    spark://host:port, mesos://host:port, yarn, or local.
  -n, --name <name>                            A name of your application.
  -ssh, --socketStreamHost <hostname>          Default: $socketStreamHost
  -ssp, --socketStreamPort <port>              Default: $socketStreamPort
  -bi, --batchInterval <batch interval in ms>  Default: $batchIntervalDef
  -ls, --lightningServerUrl <hostname>  T      Default: $lightningServerUrlDef
  """

  def parse(list: List[String]): this.type = {

    list match {
      case Nil => this
      case ("--master" | "-m") :: value :: tail => {
        sparkMaster = value
        parse(tail)
      }
      case ("--name" | "-n") :: value :: tail => {
        appName = value
        parse(tail)
      }
      case ("--socketStreamHost" | "-ssh") :: value :: tail => {
        socketStreamHost = value
        parse(tail)
      }
      case ("--socketStreamPort" | "-ssp") :: value :: tail => {
        socketStreamPort = value.toInt
        parse(tail)
      }
      case ("--batchInterval" | "-bi") :: value :: tail => {
        batchInterval = value.toInt
        parse(tail)
      }
      case ("--lightningServerUrl" | "-ls") :: value :: tail => {
        lightningServerUrl = value
        parse(tail)
      }
      case ("--help" | "-h") :: tail => {
        printUsage(0)
      }
      case _ => {
        printUsage(1)
      }
    }
  }

  def printUsage(exitNumber: Int) = {
    println(usage)
    sys.exit(status = exitNumber)
  }
}

