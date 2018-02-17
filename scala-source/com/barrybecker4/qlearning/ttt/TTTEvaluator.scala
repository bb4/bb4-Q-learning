package com.barrybecker4.qlearning.ttt

import com.barrybecker4.qlearning.common.{Evaluator, QTable}

class TTTEvaluator(table: QTable[Int], goldStandard: QTable[Int]) extends Evaluator[Int] {

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
