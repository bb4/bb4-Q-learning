package com.barrybecker4.qlearning.common

import com.barrybecker4.qlearning.common.QLearner._


object QLearner {
  /** The step size toward newly acquired information.  */
  val DEFAULT_LEARNING_RATE = 0.8f

  /** This can be 1 if a game of perfect information. If there is randomness, it should be less than 1 */
  val DEFAULT_FUTURE_REWARD_DISCOUNT = 1.0f
}

/**
  * Given a QTable, play lots of episodes in order to train the table so that
  * correct values can be assigned to next move.
  * See https://en.wikipedia.org/wiki/Q-learning and
  * https://medium.com/emergent-future/simple-reinforcement-learning-with-tensorflow-part-0-q-learning-with-tables-and-neural-networks-d195264329d0
  * https://www.cs.rochester.edu/u/kautz/Courses/242spring2014/242ai20-reinforcement-learning.pdf
  *
  * TODO: generify pull out to qlearning/common
  * @author Barry Becker
  */
case class QLearner[T](learningRate: Float = DEFAULT_LEARNING_RATE,
                    futureRewardDiscount: Float = DEFAULT_FUTURE_REWARD_DISCOUNT) {

  /** @param qtable the qtable to optimize
    * @param numEpisodes number of training games to play
    */
  def learn(qtable: QTable[T], numEpisodes: Int = 1000): Unit = {
    for (i <- 0 until numEpisodes) {
      var b = qtable.initialState
      while (b.hasTransitions) {
        val action = qtable.getNextAction(b, i)
        val nextBoard = b.makeTransition(action._1)
        qtable.update(b, action, nextBoard, learningRate, futureRewardDiscount)
        b = nextBoard
      }
    }
  }
}