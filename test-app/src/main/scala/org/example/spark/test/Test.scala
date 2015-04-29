package org.example.spark.test

import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf

object Test {

  val appName = "Luc-test"
  val master = "mesos://master:5050"

  def main(args: Array[String]) {
    val conf = new SparkConf()
                     .setAppName(appName)
                     .setMaster(master)
                     .set("spark.mesos.executor.home", "bahh") // just has to be set ...
                     .set("spark.executor.uri", "http://s3.amazonaws.com/spark-test-luc/releases/spark-1.3.1-typesafe-001-bin-2.4.0.tgz")
    val sc = new SparkContext(conf)

    val rdd = sc.makeRDD(2 to 5)

    val sum = rdd.sum()

    println(s"---------- sum: $sum")

    sc.stop()
  }

}
