package com.spoddutur

import com.spoddutur.util.{ApplicationProperties, LightningUtil}
import org.apache.log4j.{Level, Logger}
import org.apache.spark._
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming._
import org.apache.spark.streaming.StreamingContext._
import org.apache.spark.streaming.scheduler.{StreamingListener, StreamingListenerBatchCompleted, StreamingListenerBatchStarted}
import org.viz.lightning.Visualization

/**
  * Created by sruthi on 01/07/17.
  */
object MainApp {

  def main(args: Array[String]): Unit = {

    // uncomment these 2 lines if you want to see only your logs
    // Logger.getLogger("org").setLevel(Level.OFF)
    // Logger.getLogger("akka").setLevel(Level.OFF)

    ApplicationProperties.parse(args.toList)

    val spark = SparkSession
      .builder
      .master(ApplicationProperties.sparkMaster)
      .appName(ApplicationProperties.appName)
        .config("spark.driver.allowMultipleContexts", true)
      .getOrCreate()

    import spark.implicits._

    val conf = spark.sparkContext.getConf
    val ssc = new StreamingContext(conf, Seconds(ApplicationProperties.batchInterval))

    // Create a DStream that will connect to hostname:port, like localhost:9999
    val lines = ssc.socketTextStream(ApplicationProperties.socketStreamHost, ApplicationProperties.socketStreamPort)

    // Split each line into words
    val words = lines.flatMap(_.split(" "))

    // Count each word in each batch
    val pairs = words.map(word => (word, 1))
    val wordCounts = pairs.reduceByKey(_ + _)

    // Print the first ten elements of each RDD generated in this DStream to the console
    wordCounts.print()

    ssc.addStreamingListener(new SparkMonitoringListener)
    ssc.start()             // Start the computation
    ssc.awaitTermination()  // Wait for the computation to terminate
  }
}

case class SparkMonitoringListener() extends StreamingListener {

  // Publish stats to Lightning-Viz
  override def onBatchCompleted(batchCompleted: StreamingListenerBatchCompleted): Unit = {
    val start = batchCompleted.batchInfo.processingStartTime.get
    val end = batchCompleted.batchInfo.processingEndTime.get
    val batchTime = batchCompleted.batchInfo.batchTime
    val numRecords = batchCompleted.batchInfo.numRecords
    println("batch finished", start, end, end-start, batchTime.toString(), numRecords)
    LightningUtil.update(end-start, numRecords)
  }
}
