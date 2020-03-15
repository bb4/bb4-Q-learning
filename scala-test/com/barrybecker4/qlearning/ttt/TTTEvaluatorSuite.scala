package com.barrybecker4.qlearning.ttt

import java.util.Random

import com.barrybecker4.qlearning.common.{QLearner, QTable, RmsEvaluator}
import org.scalatest.funsuite.AnyFunSuite


/**
  * From experimentation, its best to have
  * futureRewardDiscount = 1.0  (since the game is deterministic)
  * learningRate = 0.8 (but it does not seem to matter much anything in the range 0.7 - 1.0 seems fine)
  * eps = 0.9  (but anything between 0.8 and 0.95 gives similar results)
  */
class TTTEvaluatorSuite extends AnyFunSuite {

  private val goldStandard = new QTable(TTTBoard(), None, epsilon = 0.9, new Random(1))
  private val learner = new QLearner[Int](learningRate = 0.8f, futureRewardDiscount = 1.0f)
  learner.learn(goldStandard, 1000000)
  val TRIALS = 20000


  test("evaluate ttt: eps = 0.3, runs = 50") {
    assertResult(0.8950898407190325) { doEval(0.3, 50) }
  }

  test("evaluate ttt: eps = 0.1 runs = 500") {
    assertResult(0.8817548205341939) { doEval(0.1, 500) }
  }

  test("evaluate ttt: eps = 0.3, runs = 5000") {
    assertResult(0.7381020134189465) { doEval(0.3, 5000) }
  }

  test("evaluate ttt: eps = 0.1 runs = 5000") {
    assertResult(0.742327008002397) { doEval(0.1, 5000) }
  }

  test(s"evaluate ttt: eps = 0.4, runs = 100000 learningRate=0.9") {
    assertResult(0.172922081103319) { doEval(0.4, 100000, learningRate = 0.9f ) }
  }
  test(s"evaluate ttt: eps = 0.8, runs = 100000 ") {
    assertResult(0.003036032810401645) { doEval(0.8, 100000 ) }
  }
  test(s"evaluate ttt: eps = 0.9, runs = 100000 ") {
    assertResult(9.993831398032048E-5) { doEval(0.9, 100000 ) }
  }
  test(s"evaluate ttt: eps = 0.99, runs = 100000 ") {
    assertResult( 1.3428868824026785E-4) { doEval(0.99, 100000 ) }
  }
  test(s"evaluate ttt: eps = 0.9, runs = 100000 learningRate = 0.4") {
    assertResult(0.01376660617984976) { doEval(0.9, 100000, learningRate = 0.4f ) }
  }
  /*
  test(s"evaluate ttt: eps = 0.9, runs = 100000 learningRate = 0.6") {
    assertResult(0.0013632256561776786) { doEval(0.9, 100000, learningRate = 0.6f ) }
  }
  test(s"evaluate ttt: eps = 0.9, runs = 100000 learningRate = 0.8") {
    assertResult(1.0095078758163613E-4) { doEval(0.9, 100000, learningRate = 0.8f ) }
  }
  test(s"evaluate ttt: eps = 0.9, runs = 100000 learningRate = 0.9") {
    assertResult(1.7663773026035155E-5) { doEval(9.0, 100000, learningRate = 0.9f ) }
  }
  test(s"evaluate ttt: eps = 0.9, runs = 100000 learningRate = 1.0") {
    assertResult(0.0) { doEval(9.0, 100000, learningRate = 1.0f ) }
  }
  test(s"evaluate ttt: eps = 1.0, runs = 100000 learningRate = 1.0") {
    assertResult(0.0) { doEval(1.0, 100000, learningRate = 1.0f ) }
  }
  test(s"evaluate ttt: eps = 1.0, runs = 100000 learningRate=1.0 frd=0.9") {
    assertResult(0.12667115151916478) {
      doEval(1.0, 100000, learningRate = 1.0f , futureRewardDiscount = 0.9f)
    }
  }*/

  test(s"evaluate ttt: eps = 0.3, runs = $TRIALS ") {
    assertResult(0.48519802707809007) { doEval(0.3, TRIALS ) }
  }
  test(s"evaluate ttt: eps = 0.3, runs = $TRIALS learningRate=0.9") {
    assertResult(0.4699262661940876) { doEval(0.3, TRIALS, learningRate = 0.9f ) }
  }
  test(s"evaluate ttt: eps = 0.3, runs = $TRIALS learningRate=1.0") {
    assertResult(0.46826683568834904) { doEval(0.3, TRIALS, learningRate = 1.0f ) }
  }

  test(s"evaluate ttt: eps = 0.4, runs = $TRIALS ") {
    assertResult(0.4508351348186567) { doEval(0.4, TRIALS ) }
  }

  test(s"evaluate ttt: eps = 0.5, runs = $TRIALS ") {
    assertResult(0.4070558295003606) { doEval(0.5, TRIALS ) }
  }

  test(s"evaluate ttt: eps = 0.6, runs = $TRIALS") {
    assertResult(0.37852552055271804) { doEval(0.6, TRIALS ) }
  }

  test(s"evaluate ttt: eps = 0.7, runs = $TRIALS ") {
    assertResult(0.34356752354968684) { doEval(0.7, TRIALS ) }
  }

  test(s"evaluate ttt: eps = 0.8, runs = $TRIALS ") {
    assertResult(0.3118899265235139) { doEval(0.8, TRIALS ) }
  }

  test(s"evaluate ttt: eps = 0.9, runs = $TRIALS ") {
    assertResult(0.317812028658438) { doEval(0.9, TRIALS ) }
  }

  test(s"evaluate ttt: eps = 1.0, runs = $TRIALS ") {
    assertResult(0.33184374324555915) { doEval(1.0, TRIALS ) }
  }

  /* data for plot. takes long time. *
  test("build data for eps = 0 to 1 step 0.05, and runs = 100, 200, ... 51,200") {

    val epsSeq = Seq(0.7f, 0.75f, 0.8f, 0.85f, 0.9f, 0.95f, 1.0f)
    val futureRewardDiscountSeq = Seq(1.0f)
    val learningRateSeq = Seq(0.9f, 0.99f, 1.0f)
    val numRunsSeq = Seq(200, 400, 800, 1600, 3200, 6400, 12800, 25600, 51200, 102400)

    ChartDataGenerator.createEpsByNumRunsData(epsSeq, numRunsSeq, learningRateSeq, futureRewardDiscountSeq, doEval)
  }*/

  private def doEval(eps: Double, numRuns: Int,
                     learningRate: Float = QLearner.DEFAULT_LEARNING_RATE,
                     futureRewardDiscount: Float = QLearner.DEFAULT_FUTURE_REWARD_DISCOUNT): Double = {
    val table = new QTable(TTTBoard(), None, eps, new Random(1))
    val learner = new QLearner[Int](learningRate, futureRewardDiscount)
    learner.learn(table, numRuns)
    val evaluator = new RmsEvaluator(table, goldStandard)
    evaluator.evaluate()
  }

}
