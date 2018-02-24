package com.barrybecker4.qlearning.frozenlake

import com.barrybecker4.qlearning.common.{QLearner, QTable}
import com.barrybecker4.qlearning.frozenlake.Direction.Direction
import org.scalatest.FunSuite
import scala.util.Random


class LakeEvaluatorSuite extends FunSuite {


  test(s"evaluate Lake: eps = 0.6, runs = 1000 ") {
    assertResult(0.2375) { doEval(0.6, 1000 ) }
  }

  test(s"evaluate Lake: eps = 0.6, runs = 10000 ") {
    assertResult(0.26799999999999996) { doEval(0.6, 10000 ) }
  }

  test(s"evaluate Lake: eps = 1.0, runs = 1000 ") {
    assertResult(0.07999999999999999) { doEval(1.0, 1000 ) }
  }

  test("build data for eps = 0 to 1 step 0.05, and runs = 100, 200, ... 51,200") {
    var zmap = Map[(Double, Int), Float]()
    val numRunsSeq = Seq(50, 100, 200, 400, 800, 1600, 3200, 6400) // 12800, 25600, 51200, 6400
    val epsSeq = for (i <- 0 to 20) yield 0.05 * i

    for (numRuns <- numRunsSeq) {
      for (eps <- epsSeq) {
        val accuracy = (1.0 - doEval(eps, numRuns)).toFloat
        println(numRuns + " e=" + eps + " accuracy=" + accuracy)
        zmap += (eps, numRuns) -> accuracy
      }
      println("calculated numRuns = " + numRuns)
    }

    // generate a csv file contents that can be imported into Plotly to show a surface
    // x = eps, y = numRuns z = accuracy = 1 - error
    val headers = Seq("eps, numRows") ++ (for (eps <- epsSeq) yield s"z[${eps.toFloat}]")
    println(headers.mkString(", "))

    for (idx <- 0 until Math.max(numRunsSeq.length, epsSeq.length)) {
      val epsValue = if (idx < epsSeq.length) epsSeq(idx).toFloat else ""
      val numRowsValue = if (idx < numRunsSeq.length) numRunsSeq(idx) else ""
      print(epsValue + ", " + numRowsValue + ", ")
      if (idx < numRunsSeq.length)
        println((for (eps <- epsSeq) yield zmap(eps, numRunsSeq(idx))).mkString(", "))
      else println
    }
  }

  private def doEval(eps: Double, numRuns: Int): Double = {
    val rnd = new Random(1L)
    val trials = 20
    var sumError: Double = 0

    for (i <- 1 to trials) {
      val lake = Lake(windFrequency = 0.06, rnd = rnd)
      val table =
        new QTable[Direction](new LakeState(lake), Some(lake.initialTable()), epsilon = eps, rnd)
      val learner = new QLearner[Direction](learningRate = 0.5f, futureRewardDiscount = 0.95f)
      learner.learn(table, numEpisodes = numRuns)

      val evaluator = new LakeEvaluator(table, numRuns = 100, maxMoves = 500)
      sumError += evaluator.evaluate()
      //println(evaluator.getReport)
    }

    sumError / trials
  }

}
