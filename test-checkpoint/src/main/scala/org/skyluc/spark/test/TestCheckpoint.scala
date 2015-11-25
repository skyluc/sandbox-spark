package org.skyluc.spark.test

import org.apache.spark.SparkConf
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.Seconds
import scala.util.Try
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object TestCheckpoint {
  
  def createContext(): StreamingContext = {
    val conf = new SparkConf()
      .setAppName("TestCheckpoint")
      .setMaster("local[2]")
      .set("spark.mesos.executor.home", "blah")
      .set("spark.executor.uri", "hdfs://10.10.1.138:8020/var/spark/spark-1.5.2-bin-hadoop2.6.tgz")
      .set("spark.mesos.coarse", "true")
      .set("spark.streaming.receiver.writeAheadLog.enable", "true")

    val ssc = new StreamingContext(conf, Seconds(1))
    ssc.checkpoint("/home/luc/tmp/tmp/checkpoint")
    
    val lines = ssc.socketTextStream("localhost", 8181)

    val ints = lines.flatMap { line => Try(Integer.parseInt(line)).toOption }

    val counts = ints.flatMap { i => List(("c", 1), ("s", i)) }.reduceByKey(_ + _)

    val overtime = counts.updateStateByKey { (vs: Seq[Int], s: Option[Int]) =>
      if (vs.isEmpty) {
        s
      } else {
        val sum = vs.sum
        s.map(_ + sum).orElse(Some(sum))
      }
    }

    overtime.print()
    
    overtime.checkpoint(Seconds(10))

    ssc
  }

  def main(args: Array[String]): Unit = {
    
    val ssc = StreamingContext.getOrCreate("/home/luc/tmp/tmp/checkpoint", createContext _)

    ssc.start()

    Future {
      while (Console.readLine() != "done") {

      }
      ssc.stop(true)
    }

    ssc.awaitTermination()
    System.exit(0)

  }

}