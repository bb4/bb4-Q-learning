package com.barrybecker4.qlearning.chopsticks

import java.util.Random

import com.barrybecker4.qlearning.common.{ChartDataGenerator, QLearner, QTable, RmsEvaluator}
import org.scalatest.funsuite.AnyFunSuite

/**
  * From experimentation, its best to have
  * futureRewardDiscount = 1.0  (since the game is deterministic)
  * learningRate = 0.8 (but it does not seem to matter much anything in the range 0.7 - 1.0 seems fine)
  * eps = 0.9  (but anything between 0.8 and 0.95 gives similar results)
  */
class ChopsticksEvaluatorSuite extends AnyFunSuite {

  private val goldStandard = new QTable(ChopsticksState(), None, epsilon = 0.9, new Random(1))
  private val learner = new QLearner[(Byte, Byte)](.95f, 1.0f)
  learner.learn(goldStandard, 1000000)


  test("evaluate chopsticks: eps = 0.3, runs = 50") {
    assertResult(0.7282194248632798) { doEval(0.3, 50) }
  }

  test("evaluate chopsticks: eps = 0.1 runs = 500") {
    assertResult(0.6490735916036947) { doEval(0.1, 500) }
  }

  test("evaluate chopsticks: eps = 0.3, runs = 5000") {
    assertResult(0.3467494861956071) { doEval(0.3, 5000) }
  }

  test("evaluate chopsticks: eps = 0.4, runs = 5000") {
    assertResult(0.3149084939610007) { doEval(0.4, 5000) }
  }

  test("evaluate chopsticks: eps = 0.5, runs = 5000") {
    assertResult(0.2604145598080305) { doEval(0.5, 5000) }
  }

  test("evaluate chopsticks: eps = 0.7, runs = 5000") {
    assertResult(0.19613598299626517) { doEval(0.7, 5000) }
  }

  test("evaluate chopsticks: eps = 0.9, runs = 5000") {
    assertResult(0.14769597319628783) { doEval(0.9, 5000) }
  }

  test("evaluate chopsticks: eps = 0.1 runs = 5000") {
    assertResult(0.42645091778230876) { doEval(0.1, 5000) }
  }

  test("evaluate chopsticks: eps = 0.1 runs = 10000") {
    assertResult(0.3812735970189622) { doEval(0.1, 10000) }
  }

  test("evaluate chopsticks: eps = 0.4 runs = 10000") {
    assertResult(0.22505485060640595) { doEval(0.4, 10000) }
  }

  test("evaluate chopsticks: eps = 0.6 runs = 10000") {
    assertResult(0.12035791477132503) { doEval(0.6, 10000) }
  }

  test("evaluate chopsticks: eps = 0.8 runs = 10000") {
    assertResult(0.0644372003576509) { doEval(0.8, 10000) }
  }
/*
  test("evaluate chopsticks: eps = 0.95 runs = 10000") {
    assertResult(0.024756010260868034) { doEval(0.95, 10000) }
  }

  test("evaluate chopsticks: eps = 1.0 runs = 10000") {
    assertResult(0.036871011335225995) { doEval(1.0, 10000) }
  }

  test("evaluate chopsticks: eps = 0.05 runs = 10000") {
    assertResult(0.41947800325647927) { doEval(0.05, 10000) }
  }


  test("evaluate chopsticks: eps = 0.6 runs = 100000, learningRate = 0.6") {
    assertResult(0.00745778856072098) { doEval(0.6, 100000, learningRate = 0.6f) }
  }

  test("evaluate chopsticks: eps = 0.6 runs = 100000, learningRate = 0.8") {
    assertResult(7.247222192423757E-4) { doEval(0.6, 100000, learningRate = 0.8f) }
  }
*/
  test("evaluate chopsticks: eps = 0.6 runs = 100000, learningRate = 0.95") {
    assertResult(4.0499788335011155E-5) { doEval(0.6, 100000, learningRate = 0.95f) }
  }

  test("evaluate chopsticks: eps = 0.6 runs = 100000, learningRate = 1.0") {
    assertResult(0.0) { doEval(0.6, 100000, learningRate = 1.0f) }
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
    val learner = new QLearner[(Byte, Byte)](learningRate,  futureRewardDiscount = frd)
    learner.learn(table, numRuns)
    val evaluator = new RmsEvaluator(table, goldStandard)
    evaluator.evaluate()
  }

}
