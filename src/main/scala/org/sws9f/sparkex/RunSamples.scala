package org.sws9f.sparkex

import org.apache.spark.{Logging, SparkContext, SparkConf}
import org.apache.spark.SparkContext._
import org.apache.spark.sql.{SQLContext,DataFrame}
import com.datastax.spark.connector._

import scala.collection.mutable.ListBuffer
import com.typesafe.config.ConfigFactory
import scala.collection.JavaConversions._

import org.apache.spark.sql.hive.HiveContext
import org.apache.spark.sql.expressions._
import org.apache.spark.sql.functions._

import org.slf4j.{Logger,LoggerFactory}

object RunSamples {
  val logger = LoggerFactory.getLogger(this.getClass)
  lazy val confSpark : SparkConf = ToolSpark.getSparkConfiguration
  lazy val sc : SparkContext = new SparkContext(confSpark)
  lazy val sqlContext = new HiveContext(sc)
  
  def main(args: Array[String]) {
    // import sqlContext.implicits._
    // var dfSrcTrans = step01Trans
    // dfSrcTrans.show
    // var df02U = step02UidInterval
    // var df03S = step03Store
    sc.stop
  }
  
  
//   def step04UidGma : DataFrame = {
//     var df04Ua = sqlContext.sql("""
//     |    SELECT
//     |      t2u.c1 AS c1, t2u.c2 AS c2, t2u.c3 AS c3, t2u.sid AS sid, t2u.uid AS uid,
//     |      t2u.avg_diff AS v1_gap_mean,
//     |      t2u.n_trans - 1 AS n_interval,
//     |      POW(t3s.raw_gap_mean, 2) / t3s.raw_gap_var   AS v1_k,
//     |      (POW(t3s.v1_mu, 2) / t3s.v1_ss) + 2          AS v1_alpha,
//     |      t3s.v1_ss / (t3s.v1_mu * (POW(t3s.v1_mu, 2) + t3s.v1_ss)) AS v1_theta
//     |    FROM df02U t2u
//     |    LEFT OUTER JOIN df03S t3s
//     |      ON t2u.c1 = t3s.c1 AND t2u.c2 = t3s.c2 AND t2u.c3 = t3s.c3 AND t2u.sid = t3s.sid
//     |""".stripMargin)
//
//     df04Ua.registerTempTable("df04Ua")
//
//     val df04Ub = sqlContext.sql("""
//     |  SELECT
//     |    tk.c1, tk.c2, tk.c3, tk.sid, tk.uid,
//     |    v1_k, v1_alpha, v1_theta, v1_gap_mean,
//     |    n_interval / (v1_k * n_interval + v1_alpha - 1) AS v1_w1,
//     |    1 / (v1_k * n_interval + v1_alpha - 1) AS v1_w2
//     |  FROM df04Ua tk
//     |""".stripMargin)
//
//     df04Ub.registerTempTable("df04Ub")
//
//
//     val df04Uc = sqlContext.sql("""
//     |SELECT
//     |  c1, c2, c3, sid, uid,
//     |  COALESCE( v1_k * (v1_w1 * v1_gap_mean + v1_w2 / v1_theta), v1_k / ((v1_alpha - 1) * v1_theta) ) AS v1_BE
//     |FROM df04Ub
//     |""".stripMargin)
//     df04Uc.registerTempTable("df04Uc")
//
//
//     // df04U.cache
//     // df04U.registerTempTable("df04U")
//     return df04Uc
//   }
//
//   def step03Store : DataFrame = {
//     val df03S = sqlContext.sql("""
//     |SELECT
//     | t1.c1 AS c1,
//     | t1.c2 AS c2,
//     | t1.c3 AS c3,
//     | t1.sid AS sid,
//     | t1.raw_gap_mean AS raw_gap_mean,
//     | t1.raw_gap_var AS raw_gap_var,
//     | t2.v1_mu AS v1_mu,
//     | t2.v1_ss AS v1_ss
//     |FROM (
//     |  SELECT
//     |    c1, c2, c3, sid,
//     |    AVG(diff) AS raw_gap_mean,
//     |    VARIANCE(diff) AS raw_gap_var
//     |  FROM src_trans
//     |  GROUP BY c1, c2, c3, sid
//     |) t1
//     |LEFT OUTER JOIN (
//     |  SELECT c1, c2, c3, sid,
//     |    AVG(avg_diff) AS v1_mu,
//     |    VARIANCE(avg_diff) AS v1_ss
//     |  FROM df02U
//     |  GROUP BY c1, c2, c3, sid
//     |) t2
//     |""".stripMargin)
//
//     df03S.cache
//     df03S.registerTempTable("df03S")
//     return df03S
//   }
//
//   def step02UidInterval : DataFrame = {
//     val df02U = sqlContext.sql("""
//     |SELECT
//     |  c1, c2, c3, sid, uid,
//     |  COUNT(date) AS n_trans,
//     |  SUM(payment) AS payment,
//     |  MIN(date) AS min_date,
//     |  AVG(diff) AS avg_diff
//     |FROM src_trans
//     |GROUP BY c1, c2, c3, sid, uid
//     |""".stripMargin)
//
//     df02U.registerTempTable("df02U")
//     df02U.cache
//     return df02U
//   }
//
//   def step01Trans : DataFrame = {
//     val rddSrcTranx = sc.cassandraTable[Tranx]("ks1", "tranx").where("date < ?", "2014-03-01")
// //    val sqlObj = sqlContext
// //    import sqlObj.implicits._
//     import sqlContext.implicits._
//     val dfSrcTranx = rddSrcTranx.toDF
//     dfSrcTranx.registerTempTable("tmp_tranx")
//
//     val dfTranx1A = sqlContext.sql("""
//     |SELECT
//     |  c1, c2, c3, sid, uid, date, payment,
//     |  LAG(date,1)  OVER (PARTITION BY uid, c1, c2, c3, sid ORDER BY date) AS lead_date,
//     |  LEAD(date,1) OVER (PARTITION BY uid, c1, c2, c3, sid ORDER BY date) AS next_date
//     |FROM tmp_tranx
//     |""".stripMargin)
//
//     dfTranx1A.registerTempTable("tmp_tranx1a")
//
//     val dfTranx1B = sqlContext.sql("""
//     |SELECT
//     |  c1, c2, c3, sid, uid, date, payment, lead_date, next_date,
//     |  DATEDIFF(date, lead_date) AS diff
//     |FROM tmp_tranx1a
//     |""".stripMargin)
//
//     dfTranx1B.registerTempTable("src_trans")
//     dfTranx1B.cache
//     return dfTranx1B
//   }
//
  
}
