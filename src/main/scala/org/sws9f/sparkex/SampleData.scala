package org.sws9f.sparkex

import scala.collection.JavaConversions._

import org.slf4j.{Logger,LoggerFactory}

object SampleData {
  val logger = LoggerFactory.getLogger(this.getClass)
  
  case class Person(pid: Integer, name: String, sid: Integer, age: Integer)
  case class School(sid: Integer, name: String)
  
  lazy val persons = Seq(Person(1, "N_Andy", 21, 10), Person(2, "N_Joy", 21, 20), Person(3, "T_Vicky", 22, 33), Person(4, "T_Sky", 22, 58))
  lazy val schools = Seq(School(21, "Nantes"), School(22, "Taiwan"))
  
}
