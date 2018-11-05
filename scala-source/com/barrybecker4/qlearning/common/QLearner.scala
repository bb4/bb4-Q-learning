package com.barrybecker4.qlearning.common

import com.barrybecker4.qlearning.common.QLearner._


object QLearner {

  /** The step size toward newly acquired information - referred to as learning rate.
    * It determines to what extent information overrides old. If 0, then the agent learn nothing,
    * while a factor of 1 makes the agent consider only the most recent information.
    * In fully deterministic environments, a learning rate of 1 is optimal.
    * When there is chance involved, the algorithm converges under some technical conditions on the learning rate
    * that require it to decrease to zero. In practice, often a constant learning rate is used, such as 0.1.
    */
  val DEFAULT_LEARNING_RATE = 0.8f

  /** This can be 1 if a game of perfect information. If there is randomness, it should be less than 1.
    * The discount factor (often shown as gamma) determines the importance of future rewards.
    * A factor of 0 will make the agent short-sighted by only considering current rewards,
    * while a factor approaching 1 will make it strive for a long-term high reward.
    */
  val DEFAULT_FUTURE_REWARD_DISCOUNT = 1.0f
}

/** Given a QTable, play lots of episodes in order to train the table so that
  * correct reward values can be assigned to the next move.
  * See https://en.wikipedia.org/wiki/Q-learning and
  * https://medium.com/emergent-future/simple-reinforcement-learning-with-tensorflow-part-0-q-learning-with-tables-and-neural-networks-d195264329d0
  * https://www.cs.rochester.edu/u/kautz/Courses/242spring2014/242ai20-reinforcement-learning.pdf
  * https://en.wikipedia.org/wiki/Q-learning
  * @author Barry Becker
  */
case class QLearner[T](learningRate: Float = DEFAULT_LEARNING_RATE,
                       futureRewardDiscount: Float = DEFAULT_FUTURE_REWARD_DISCOUNT) {

  /** @param qtable the q-table to optimize
    * @param numEpisodes number of training games to play
    */
  def learn(qtable: QTable[T], numEpisodes: Int = 1000): Unit = {
    for (i <- 0 until numEpisodes) {
      var state = qtable.initialState
      while (state.hasTransitions) {
        val action = qtable.getNextAction(state, i)
        val nextState = state.makeTransition(action._1)
        qtable.update(state, action, nextState, learningRate, futureRewardDiscount)
        state = nextState
      }
    }
  }
}