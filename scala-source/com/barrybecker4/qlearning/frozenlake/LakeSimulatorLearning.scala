package com.barrybecker4.qlearning.frozenlake

import com.barrybecker4.qlearning.common.QLearner

/** Hyperparameter wiring for frozen-lake training (kept separate from `LakeSimulator` App to avoid eager stdin). */
object LakeSimulatorLearning:
  private[qlearning] def learnerForHyperparams(learningRate: Double, frDiscount: Float): QLearner[Direction] =
    new QLearner[Direction](learningRate = learningRate.toFloat, futureRewardDiscount = frDiscount)
