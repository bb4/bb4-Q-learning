package com.barrybecker4.qlearning.chopsticks

import java.util.Random

import com.barrybecker4.qlearning.common.{ChartDataGenerator, QLearner, QTable, RmsEvaluator}
import org.scalactic.{Equality, TolerantNumerics}
import org.scalatest.funsuite.AnyFunSuite

/**
  * From experimentation, its best to have
  * futureRewardDiscount = 1.0  (since the game is deterministic)
  * learningRate = 0.8 (but it does not seem to matter much anything in the range 0.7 - 1.0 seems fine)
  * eps = 0.9  (but anything between 0.8 and 0.95 gives similar results)
  */
class ChopsticksEvaluatorSuite extends AnyFunSuite {

  /** Math.sqrt in RmsEvaluator may differ in the last bit across JVMs; only StrictMath is bit-identical. */
  given doubleEq: Equality[Double] = TolerantNumerics.tolerantDoubleEquality(1e-9)

  private val goldStandard = new QTable(ChopsticksState(), None, epsilon = 0.9, new Random(1))
  private val learner = new QLearner[ChopsticksAction](.95f, 1.0f)
  learner.learn(goldStandard, 1000000)


  test("evaluate chopsticks: eps = 0.3, runs = 50") {
    assert(0.7423179250951814 === doEval(0.3, 50))
  }

  test("evaluate chopsticks: eps = 0.1 runs = 500") {
    assert(0.6390911628970404 === doEval(0.1, 500))
  }

  test("evaluate chopsticks: eps = 0.3, runs = 5000") {
    assert(0.3736772176773048 === doEval(0.3, 5000))
  }

  test("evaluate chopsticks: eps = 0.4, runs = 5000") {
    assert(0.37073912694892647 === doEval(0.4, 5000))
  }

  test("evaluate chopsticks: eps = 0.5, runs = 5000") {
    assert(0.3565686543264575 === doEval(0.5, 5000))
  }

  test("evaluate chopsticks: eps = 0.7, runs = 5000") {
    assert(0.31804301726954115 === doEval(0.7, 5000))
  }

  test("evaluate chopsticks: eps = 0.9, runs = 5000") {
    assert(0.32020545989375854 === doEval(0.9, 5000))
  }

  test("evaluate chopsticks: eps = 0.1 runs = 5000") {
    assert(0.41940460430659965 === doEval(0.1, 5000))
  }

  test("evaluate chopsticks: eps = 0.1 runs = 10000") {
    assert(0.3763218587233667 === doEval(0.1, 10000))
  }

  test("evaluate chopsticks: eps = 0.4 runs = 10000") {
    assert(0.29566454939242726 === doEval(0.4, 10000))
  }

  test("evaluate chopsticks: eps = 0.6 runs = 10000") {
    assert(0.2702876862835808 === doEval(0.6, 10000))
  }

  test("evaluate chopsticks: eps = 0.8 runs = 10000") {
    assert(0.22602883061120713 === doEval(0.8, 10000))
  }
/*
  test("evaluate chopsticks: eps = 0.95 runs = 10000") {
    assert(0.024756010260868034 === doEval(0.95, 10000))
  }

  test("evaluate chopsticks: eps = 1.0 runs = 10000") {
    assert(0.036871011335225995 === doEval(1.0, 10000))
  }

  test("evaluate chopsticks: eps = 0.05 runs = 10000") {
    assert(0.41947800325647927 === doEval(0.05, 10000))
  }


  test("evaluate chopsticks: eps = 0.6 runs = 100000, learningRate = 0.6") {
    assert(0.00745778856072098 === doEval(0.6, 100000, learningRate = 0.6f))
  }

  test("evaluate chopsticks: eps = 0.6 runs = 100000, learningRate = 0.8") {
    assert(7.247222192423757E-4 === doEval(0.6, 100000, learningRate = 0.8f))
  }
*/
  test("evaluate chopsticks: eps = 0.6 runs = 100000, learningRate = 0.95") {
    assert(0.07553116914748674 === doEval(0.6, 100000, learningRate = 0.95f))
  }

  test("evaluate chopsticks: eps = 0.6 runs = 100000, learningRate = 1.0") {
    assert(0.07537780529294419 === doEval(0.6, 100000, learningRate = 1.0f))
  }


  /* data for plot. takes long time. *
  test("build chart data") {

    val epsSeq = Seq(0.7f, 0.75f, 0.8f, 0.85f, 0.9f, 0.95f, 1.0f)
    val futureRewardDiscountSeq = Seq(1.0f)
    val learningRateSeq = Seq(0.9f, 0.99f, 1.0f)
    val numRunsSeq = Seq(200, 400, 800, 1600, 3200, 6400, 12800, 25600, 51200, 102400)

    ChartDataGenerator.createEpsByNumRunsData(epsSeq, numRunsSeq, learningRateSeq, futureRewardDiscountSeq, doEval)
  }*/


  private def doEval(eps: Double, numRuns: Int,
                     learningRate: Float = QLearner.DEFAULT_LEARNING_RATE,
                     frd: Float = QLearner.DEFAULT_FUTURE_REWARD_DISCOUNT): Double = {
    val table = new QTable(ChopsticksState(), None, eps, new Random(1))
    val learner = new QLearner[ChopsticksAction](learningRate,  futureRewardDiscount = frd)
    learner.learn(table, numRuns)
    val evaluator = new RmsEvaluator(table, goldStandard)
    evaluator.evaluate()
  }

}
