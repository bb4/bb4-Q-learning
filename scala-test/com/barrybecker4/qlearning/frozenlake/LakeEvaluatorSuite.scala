package com.barrybecker4.qlearning.frozenlake

import com.barrybecker4.qlearning.common.{QLearner, QTable}
import com.barrybecker4.qlearning.frozenlake.Direction.Direction
import org.scalatest.FunSuite
import scala.util.Random
import LakeEvaluatorSuite._
import Lake._


object LakeEvaluatorSuite {
  val RND = new Random(1L)

  val LEARNING_RATE = 0.6f
  val FUTURE_REWARD_DISCOUNT = 0.9f
}

class LakeEvaluatorSuite extends FunSuite {


  test(s"evaluate windy Lake: eps = 0.6, runs = 1000 ") {
    assertResult(0.1125f) { doEval(0.6, 1000, lake = LARGE_WINDY_7x10_LAKE) }
  }
  test(s"evaluate calm Lake: eps = 0.6, runs = 1000 ") {
    assertResult(0.04575f) { doEval(0.6, 1000, lake = LARGE_CALM_7x10_LAKE) }
  }

  test(s"evaluate Lake: eps = 0.6, runs = 1000 ") {
    assertResult(0.1025f) { doEval(0.6, 1000 ) }
  }

  test(s"evaluate Lake: eps = 0.6, runs = 10000 ") {
    assertResult(0.09675f) { doEval(0.6, 10000 ) }
  }

  test(s"evaluate Lake: eps = 1.0, runs = 1000 ") {
    assertResult(0.16425f) { doEval(1.0, 1000 ) }
  }

  /*
  test("build data for eps = 0 to 1 step 0.05, and runs = 100, 200, ... 51,200") {

    println("lake = \n" + LARGE_WINDY_7x10_LAKE.toString())
    var zmap = Map[(Double, Int), Float]()
    val numRunsSeq = Seq(50, 100, 200, 400, 800, 1200, 1600, 2000) // 6400, 12800, 25600, 51200, 6400
    val epsSeq = for (i <- 0 to 20) yield 0.05 * i

    for (numRuns <- numRunsSeq) {
      for (eps <- epsSeq) {
        val accuracy = (1.0 - doEval(eps, numRuns, lake = LARGE_WINDY_7x10_LAKE)).toFloat
        println(numRuns + " e=" + eps.toFloat + " accuracy=" + accuracy)
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
  }*/

  private def doEval(eps: Double, numRuns: Int,
                     learningRate: Float = LEARNING_RATE, lake: Lake = LARGE_WINDY_7x10_LAKE,
                     futureRewardDiscount: Float = FUTURE_REWARD_DISCOUNT): Float = {
    val trials = 20
    var sumError: Double = 0

    for (i <- 1 to trials) {
      val table =
        new QTable[Direction](new LakeState(lake), Some(lake.initialTable()), epsilon = eps, RND)
      val learner = new QLearner[Direction](learningRate, futureRewardDiscount)
      learner.learn(table, numEpisodes = numRuns)

      val evaluator = new LakeEvaluator(table, numRuns = 200, maxMoves = 500)
      sumError += evaluator.evaluate()
    }

    (sumError / trials).toFloat
  }
}
