package com.barrybecker4.qlearning.common


/**
  * Evaluates by taking the root mean squared error between this table and a provided gold standard.
  */
class RmsEvaluator[T](table: QTable[T], goldStandard: QTable[T]) extends Evaluator[T] {

  def evaluate(): Double = {
    var sumsq: Double = 0
    var ct = 0
    for (e <- table.table) {
      for (m <- e._2.keys) {
        val v: Double = e._2(m) - goldStandard.table(e._1)(m)
        sumsq += v * v
        ct += 1
      }
    }
    Math.sqrt(sumsq / ct)
  }
}
