package org.skyluc.spark.test

import org.apache.spark.SparkConf
import org.apache.spark.streaming.Seconds
import org.apache.spark.streaming.StreamingContext

object TestCheckpoint {

  def fizzBuzz(i: Int) : String = {
    i match {
      case a if (a % 15 == 0) =>
        "Fizz Buzz"
      case a if (a % 5 == 0) =>
        "Buzz"
      case a if (a % 3 == 0) =>
        "Fizz"
      case a =>
        a.toString
    }
  }
  
  def main(args: Array[String]): Unit = {

    val conf = new SparkConf()
      .setAppName("TestCheckpoint")
      .setMaster("local[2]")

    val ssc = new StreamingContext(conf, Seconds(5))

    val lines = ssc.socketTextStream("localhost", 2222)

    val fb = lines.map(_.toInt).map(fizzBuzz(_))
    
    val stats = fb.map(s => (s, 1)).reduceByKey(_ + _)
    
    stats.foreachRDD{(rdd, time) =>
      val results: List[String] = rdd.collectAsMap().map(t => s"${t._1} - ${t._2}")(collection.breakOut)
      println(s"""=================
Result for $time
${results.sorted.mkString("\n")}
============================""")
    }

    ssc.start()


    ssc.awaitTermination()

  }

}