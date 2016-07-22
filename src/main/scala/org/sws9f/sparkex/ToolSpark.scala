package org.sws9f.sparkex

import org.apache.spark.{SparkConf, SparkContext}
import com.typesafe.config.ConfigFactory
import scala.collection.JavaConversions._

import org.slf4j.{Logger,LoggerFactory}

object ToolSpark {
  val logger = LoggerFactory.getLogger(this.getClass)
  lazy val confSpark : SparkConf = getSparkConfiguration
  lazy val sc : SparkContext = { new SparkContext(confSpark) }
  
  def getSparkConfiguration : SparkConf = {
    val confSpark = new SparkConf().setAppName("NesCalculator")
    val confApp = ConfigFactory.load()
    confApp.getObject("spark").foreach (
      {
        case (k, v) => { 
          logger.debug("spark configuration : " +  k + "=" + v.unwrapped )
          confSpark.set(k, v.unwrapped.toString)
        }
      }
    )
    if (confSpark.getOption("spark.master") == None) confSpark.set("spark.master", "local[4]")

    return confSpark
  }
}
