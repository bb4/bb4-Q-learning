package com.barrybecker4.qlearning.ttt

import java.util.Random

import com.barrybecker4.qlearning.common.{QLearner, QTable}
import org.scalatest.FunSuite


class TTTEvaluatorSuite extends FunSuite {

  private val goldStandard = new QTable(TTTBoard(), None, epsilon = 0.6, new Random(1))
  private val learner = new QLearner[Int]()
  learner.learn(goldStandard, 400000)
  val TRIALS = 20000


  test("evaluate ttt: eps = 0.3, runs = 50") {
    assertResult(0.8949913913722832) { doEval(0.3, 50) }
  }

  test("evaluate ttt: eps = 0.1 runs = 500") {
    assertResult(0.8821901701678707) { doEval(0.1, 500) }
  }

  test("evaluate ttt: eps = 0.3, runs = 5000") {
    assertResult(0.7390366031249752) { doEval(0.3, 5000) }
  }

  test("evaluate ttt: eps = 0.1 runs = 5000") {
    assertResult(0.7420402985152421) { doEval(0.1, 5000) }
  }

  test(s"evaluate ttt: eps = 1.0, runs = 100000 ") {
    assertResult(0.00010451553473530755) { doEval(.9, 100000 ) }
  }


  test(s"evaluate ttt: eps = 0.3, runs = $TRIALS ") {
    assertResult(0.4881105622609041) { doEval(0.3, TRIALS ) }
  }

  test(s"evaluate ttt: eps = 0.4, runs = $TRIALS ") {
    assertResult(0.44575432538503224) { doEval(0.4, TRIALS ) }
  }

  test(s"evaluate ttt: eps = 0.5, runs = $TRIALS ") {
    assertResult(0.41192070652492874) { doEval(0.5, TRIALS ) }
  }

  test(s"evaluate ttt: eps = 0.6, runs = $TRIALS") {
    assertResult(0.3684896091892809) { doEval(0.6, TRIALS ) }
  }

  test(s"evaluate ttt: eps = 0.7, runs = $TRIALS ") {
    assertResult(0.34548600825517106) { doEval(0.7, TRIALS ) }
  }

  test(s"evaluate ttt: eps = 0.8, runs = $TRIALS ") {
    assertResult(0.31556631715501093) { doEval(0.8, TRIALS ) }
  }

  test(s"evaluate ttt: eps = 0.9, runs = $TRIALS ") {
    assertResult(0.3165362266569276) { doEval(0.9, TRIALS ) }
  }

  test(s"evaluate ttt: eps = 1.0, runs = $TRIALS ") {
    assertResult(0.32430255647240025) { doEval(1.0, TRIALS ) }
  }

  private def doEval(eps: Double, numRuns: Int): Double = {
    val table = new QTable(TTTBoard(), None, eps, new Random(1))
    learner.learn(table, numRuns)
    val evaluator = new TTTEvaluator(table, goldStandard)
    evaluator.evaluate()
  }

}
