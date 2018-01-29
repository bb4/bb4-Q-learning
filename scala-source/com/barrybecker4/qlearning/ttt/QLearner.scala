package com.barrybecker4.qlearning.ttt

import QLearner._

object QLearner {
  /** The speed at which we move toward the what a playou t */
  val DEFAULT_LEARNING_RATE = 0.8f

  /** This can be 1 since it is a game of perfect information. If there were randomness, it should be less than 1 */
  val FUTURE_REWARD_DISCOUNT = 1.0f

  def main(args:Array[String]) {
    val learner = new QLearner()
    val qtable = new QTable()
    learner.learn(qtable)
    println("after learning qtable = " + qtable.toString)
  }
}

/**
  * Given a QTable, play lots of game episodes in order to train the table so that
  * correct values can be assigned to next move.
  *
  * TODO: pull out to qlearning/common
  */
class QLearner {

  /**
    * @param qtable the qtable to optimize
    * @param numEpisodes number of training games to play
    */
  def learn(qtable: QTable, numEpisodes: Int = 1000): Unit = {
    for (i <- 0 until numEpisodes) {
      var b = TTTBoard()
      var playerToMove = 'X'
      while (b.hasMoves) {
        val action = qtable.getNextAction(b, i)
        val nextBoard = b.makeMove(action._1, playerToMove)
        val reward = if (nextBoard.isWon(playerToMove)) {
          if (playerToMove == 'X') 1.0f else -1.0f
        } else 0.0f
        qtable.update(b, action, nextBoard, reward, DEFAULT_LEARNING_RATE, FUTURE_REWARD_DISCOUNT)
        b = nextBoard
        playerToMove = if (playerToMove =='X') 'O' else 'X'
      }
    }
  }
}