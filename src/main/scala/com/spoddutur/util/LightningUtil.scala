package com.spoddutur.util

import org.viz.lightning.{Lightning, Visualization}

import scala.util.Try

/**
  * Created by sruthi on 02/07/17.
  */
object LightningUtil {

  // init lightning-viz server
  val lgn = Lightning(host=ApplicationProperties.lightningServerUrl)

  // initialize visualization with 0 data
  var viz:Visualization = lgn.lineStreaming(
    series = Array.fill(2)(Array(0.0)),
    size = Array(1.0, 10.0))

  // posts new data to lightning-viz graph in a streaming way & plots the graph..
  def update(batchProcessingTime: Long, numRecords: Long) {
    val arr1 = Array(batchProcessingTime.toDouble)
    val arr2 = Array(numRecords.toDouble)
    Try(lgn.lineStreaming(series = Array(arr1, arr2), viz = viz, yaxis = "NumRecordsPerBatch and BatchProcessingTime", xaxis = "Batch Number"))
  }
}
