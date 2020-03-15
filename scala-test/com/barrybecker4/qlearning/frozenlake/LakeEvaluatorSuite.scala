package com.barrybecker4.qlearning.frozenlake

import com.barrybecker4.qlearning.common.{ChartDataGenerator, QLearner, QTable}
import com.barrybecker4.qlearning.frozenlake.Direction.Direction
import org.scalatest.funsuite.AnyFunSuite

import scala.util.Random
import LakeEvaluatorSuite._
import Lake._


object LakeEvaluatorSuite {
  val RND = new Random(1L)

  val DEFAULT_LEARNING_RATE = 0.4f
  val DEFAULT_FUTURE_REWARD_DISCOUNT = 0.8f
}

/**
  * This problem is a bit different from the other because of the randomness.
  * From experimentation, for LARGE_WINDY_LAKE, its best to have
  * futureRewardDiscount = 0.4
  * learningRate = 0.4
  * eps = 0.2
  *
  * For lake where wind frequency is a little calmer, these params are better
  * futureRewardDiscount = 0.7
  * learningRate = 0.3
  * * eps = 0.3
  */
class LakeEvaluatorSuite extends AnyFunSuite {


  test(s"evaluate windy Lake: eps = 0.6, runs = 1000 ") {
    assertResult(0.095f) { doEval(0.6, 1000, lake = LARGE_WINDY_7x10_LAKE) }
  }
  test(s"evaluate calm Lake: eps = 0.6, runs = 1000 ") {
    assertResult(0.03775f) { doEval(0.6, 1000, lake = LARGE_CALM_7x10_LAKE) }
  }

  test(s"evaluate Lake: eps = 0.6, runs = 1000 learningRate = 0.6") {
    assertResult(0.10575f) { doEval(0.6, learningRate = 0.6f, numRuns = 1000 ) }
  }

  test(s"evaluate Lake: eps = 0.6, runs = 10000 learningRate = 0.6") {
    assertResult(0.1005f) { doEval(0.6, learningRate = 0.6f, numRuns = 10000 ) }
  }

  test(s"evaluate Lake: eps = 0.6, runs = 1000 learningRate = 0.4 ") {
    assertResult(0.059f) { doEval(0.6, learningRate = 0.4f, numRuns = 1000 ) }
  }

  test(s"evaluate Lake: eps = 0.6, runs = 10000 learningRate = 0.4") {
    assertResult(0.05725f) { doEval(0.6, learningRate = 0.4f, numRuns = 10000 ) }
  }

  /*
  test(s"evaluate Lake: eps = 0.6, runs = 20000 learningRate = 0.4") {
    assertResult(0.06675f) { doEval(0.6, learningRate = 0.4f, numRuns = 20000 ) }
  }

  test(s"evaluate Lake: eps = 0.4, runs = 20000 learningRate = 0.4") {
    assertResult(0.04225f) { doEval(0.4, learningRate = 0.4f, numRuns = 20000 ) }
  }*/
/*
  test(s"evaluate Lake: eps = 0.4, runs = 20000 learningRate = 0.4, frd = 0.5") {
    assertResult(0.04025f) {doEval(0.4, learningRate = 0.4f, numRuns = 20000, frd = 0.5f )}
  }

  test(s"evaluate Lake: eps = 0.3, runs = 20000 learningRate = 0.4, frd = 0.5") {
    assertResult(0.023f) {doEval(0.3, learningRate = 0.4f, numRuns = 20000, frd = 0.5f )}
  }

  test(s"evaluate Lake: eps = 0.2, runs = 20000 learningRate = 0.4, frd = 0.5") {
    assertResult(0.01625f) {doEval(0.2, learningRate = 0.4f, numRuns = 20000, frd = 0.5f )}
  }

  test(s"evaluate Lake: eps = 0.1, runs = 20000 learningRate = 0.3, frd = 0.4") {
    assertResult(0.013f) {doEval(0.1, learningRate = 0.4f, numRuns = 20000, frd = 0.4f )}
  }

  test(s"evaluate Lake: eps = 0.2, runs = 20000 learningRate = 0.3, frd = 0.4") {
    assertResult(0.01675f) {doEval(0.2, learningRate = 0.4f, numRuns = 20000, frd = 0.4f )}
  }

  test(s"evaluate Lake: eps = 0.2, runs = 20000 learningRate = 0.3, frd = 0.3") {
    assertResult(0.00925f) {  doEval(0.2, learningRate = 0.4f, numRuns = 20000, frd = 0.3f )}
  }*/


  /* data for plot. takes long time.
  test("build chart data") {

    val epsSeq = Seq(0.2f, 0.3f)
    val futureRewardDiscountSeq = Seq(0.3f, 0.4f, 0.5f)
    val learningRateSeq = Seq(0.1f, 0.15f, 0.2f, 0.25f, 0.3f, 0.35f, 0.4f, 0.45f, 0.5f, 0.55f, 0.6f)
    val numRunsSeq = Seq(100, 200, 400, 800, 1600, 3200, 6400) //, 12800, 25600)

    ChartDataGenerator.createLearningRateByNumRunsData(epsSeq, numRunsSeq, learningRateSeq, futureRewardDiscountSeq, doEval1)
  }

  private def doEval1(eps: Double, numRuns: Int,
                      learningRate: Float = DEFAULT_LEARNING_RATE,
                      frd: Float = DEFAULT_FUTURE_REWARD_DISCOUNT) = {
    doEval(eps, numRuns, learningRate, frd, LARGE_WINDY_7x10_LAKE)
  }*/

  private def doEval(eps: Double, numRuns: Int,
                     learningRate: Float = DEFAULT_LEARNING_RATE,
                     frd: Float = DEFAULT_FUTURE_REWARD_DISCOUNT, lake: Lake = LARGE_WINDY_7x10_LAKE): Float = {
    val trials = 20
    var sumError: Double = 0

    for (i <- 1 to trials) {
      val table =
        new QTable[Direction](new LakeState(lake), Some(lake.initialTable()), epsilon = eps, RND)
      val learner = new QLearner[Direction](learningRate, frd)
      learner.learn(table, numEpisodes = numRuns)

      val evaluator = new LakeEvaluator(table, numRuns = 200, maxMoves = 500)
      sumError += evaluator.evaluate()
    }

    (sumError / trials).toFloat
  }
}
