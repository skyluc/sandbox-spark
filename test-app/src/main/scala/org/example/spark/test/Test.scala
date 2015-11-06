package org.example.spark.test

import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf

object Test {

  val appName = "Luc-test"

  def main(args: Array[String]) {
    val conf = new SparkConf()
                     .setAppName(appName)
//                     .setMaster(master)
                     .set("spark.mesos.executor.home", "bahh") // just has to be set ...
                     .set("spark.executor.uri", "hdfs://10.10.1.15:8020/var/spark/spark-1.5.2-bin-hadoop2.6.tgz")
    val sc = new SparkContext(conf)

    val rdd = sc.makeRDD(2 to 5)

    val sum = rdd.sum()

    println(s"---------- sum: $sum")

    sc.stop()
  }

}
