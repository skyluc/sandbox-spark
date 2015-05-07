package org.skyluc

import org.apache.spark._
import org.apache.spark.streaming._
import org.apache.spark.streaming.StreamingContext._
import org.apache.http.HttpRequest
import org.apache.http.message.BasicHttpEntityEnclosingRequest
import org.apache.http.entity.StringEntity
import java.net.HttpURLConnection
import java.net.URL
import java.io.OutputStream
import java.io.BufferedOutputStream
import scala.collection.mutable.HashMap

object Main {

  implicit object hashMapAccumulatorParam extends AccumulableParam[HashMap[String, Int], (String, Int)] {
    def zero(m: HashMap[String, Int]) = HashMap()
    def addInPlace(m1: HashMap[String, Int], m2: HashMap[String, Int]) = {
      m2.foreach { p =>
        m1.get(p._1) match {
          case Some(v) =>
            m1 += ((p._1, (v + p._2)))
          case None =>
            m1 += p
        }
      }

      m1
    }

    def addAccumulator(r: HashMap[String, Int], t: (String, Int)): HashMap[String, Int] = {
      r += t
    }
  }
  
  def updateFunction(newValues: Seq[Int], runningCount: Option[Int]): Option[Int] = {
    val newCount = newValues.sum + (runningCount getOrElse 0)
    Some(newCount)
  }
  
  def main(args: Array[String]) {
    // Create a local StreamingContext with two working thread and batch interval of 1 second.
    // The master requires 2 cores to prevent from a starvation scenario.

    val conf = new SparkConf().setMaster("local[2]").setAppName("NetworkWordCount")
    val sc = new SparkContext(conf)
    val ssc = new StreamingContext(sc, Seconds(5))
    ssc.checkpoint("/home/luc/tmp/tmpSpark/checkpoint")

    // Create a DStream that will connect to hostname:port, like localhost:9999
    val lines = ssc.socketTextStream("localhost", 2222)

    // Split each line into words
    val words = lines.flatMap(_.split(" "))

    import org.apache.spark.streaming.StreamingContext._
    // Count each word in each batch
    val pairs = words.map(word => (word, 1))
    val wordCounts = pairs.reduceByKey(_ + _)

    val state = wordCounts.updateStateByKey(updateFunction)

    // Print the first ten elements of each RDD generated in this DStream to the console
    state.foreachRDD { rdd =>
      val data = rdd.collect().map(t => s"${t._2} ${t._1}").mkString("\n")
      sendData(data)
    }
    
    ssc.start() // Start the computation
    ssc.awaitTermination() // Wait for the computation to terminate
  }

  def sendData(data: String) {
    val connection: HttpURLConnection = new URL("http://localhost:9000/postStats").openConnection().asInstanceOf[HttpURLConnection]
    connection.setDoOutput(true)
    connection.setRequestMethod("POST")
    connection.setRequestProperty("Content-Type", "text/plain; charset=utf-8")
    val out = new BufferedOutputStream(connection.getOutputStream());
    println(data)
    out.write(data.getBytes)
    out.flush()

    connection.connect()
    println(s"=======> ${connection.getResponseCode}")
    connection.disconnect()
  }

}
