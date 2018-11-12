package com.barrybecker4.qlearning.chopsticks

import java.util.Random

import com.barrybecker4.qlearning.common.{QLearner, QTable, RmsEvaluator}
import org.scalatest.FunSuite


class ChopsticksEvaluatorSuite extends FunSuite {

  private val goldStandard = new QTable(ChopsticksState(), None, epsilon = 0.8, new Random(1))
  private val learner = new QLearner[(Byte, Byte)](0.8f, 0.999f)
  learner.learn(goldStandard, 500000)
  val TRIALS = 20000


  test("evaluate ttt: eps = 0.3, runs = 50") {
    assertResult(0.726558505528403) { doEval(0.3, 50) }
  }

  test("evaluate ttt: eps = 0.1 runs = 500") {
    assertResult(0.6502501375063329) { doEval(0.1, 500) }
  }

  test("evaluate ttt: eps = 0.3, runs = 5000") {
    assertResult(0.34466503554283334) { doEval(0.3, 5000) }
  }

  test("evaluate ttt: eps = 0.1 runs = 5000") {
    assertResult(0.414656409605097) { doEval(0.1, 5000) }
  }

  test("evaluate ttt: eps = 0.1 runs = 10000") {
    assertResult(0.3758140555688104) { doEval(0.1, 10000) }
  }

  test("evaluate ttt: eps = 0.05 runs = 10000") {
    assertResult(0.4058155383208999) { doEval(0.05, 10000) }
  }



//  test(s"evaluate ttt: eps = 0.3, runs = $TRIALS ") {
//    assertResult(0.48811130126635904) { doEval(0.3, TRIALS ) }
//  }
//
//  test(s"evaluate ttt: eps = 0.4, runs = $TRIALS ") {
//    assertResult(0.4457547569401316) { doEval(0.4, TRIALS ) }
//  }

  test(s"evaluate ttt: eps = 0.5, runs = $TRIALS ") {
    assertResult(0.11244032849430617) { doEval(0.5, TRIALS ) }
  }

//  test(s"evaluate ttt: eps = 0.6, runs = $TRIALS") {
//    assertResult(0.36849077331230623) { doEval(0.6, TRIALS ) }
//  }
//
//  test(s"evaluate ttt: eps = 0.7, runs = $TRIALS ") {
//    assertResult(0.3454863442625571) { doEval(0.7, TRIALS ) }
//  }
//
//  test(s"evaluate ttt: eps = 0.8, runs = $TRIALS ") {
//    assertResult(0.3155669600008248) { doEval(0.8, TRIALS ) }
//  }
//
//  test(s"evaluate ttt: eps = 0.9, runs = $TRIALS ") {
//    assertResult(0.3165364115611281) { doEval(0.9, TRIALS ) }
//  }
//
//  test(s"evaluate ttt: eps = 1.0, runs = $TRIALS ") {
//    assertResult(0.32430275476060266) { doEval(1.0, TRIALS ) }
//  }

  /* For the plot. Takes a long time.
  test("build data for eps = 0 to 1 step 0.05, and runs = 100, 200, ... 51,200") {
    var zmap = Map[(Double, Int), Double]()
    val numRunsSeq = Seq(100, 200, 400, 800, 1600, 3200, 6400, 12800, 25600, 51200, 102400)
    val epsSeq = for (i <- 0 to 20) yield 0.05 * i

    for (numRuns <- numRunsSeq) {
      for (eps <- epsSeq) {
        zmap += (eps, numRuns) -> (1.0 - doEval(eps, numRuns) )
      }
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

  private def doEval(eps: Double, numRuns: Int): Double = {
    val table = new QTable(ChopsticksState(), None, eps, new Random(1))
    learner.learn(table, numRuns)
    val evaluator = new RmsEvaluator(table, goldStandard)
    evaluator.evaluate()
  }

}
