package com.barrybecker4.qlearning.frozenlake

import org.scalatest.funsuite.AnyFunSuite

class LakeSimulatorSuite extends AnyFunSuite {

  test("learnerForHyperparams passes learning rate and future reward discount") {
    val lr = 0.42
    val frd = 0.71f
    val learner = LakeSimulatorLearning.learnerForHyperparams(lr, frd)
    assert(learner.learningRate == 0.42f)
    assert(learner.futureRewardDiscount == 0.71f)
  }
}
