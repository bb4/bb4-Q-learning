package com.barrybecker4.qlearning.ttt

import java.util.Scanner
import com.barrybecker4.qlearning.common.{QLearner, QTable}


object TTTPlayer {
  def main(args:Array[String]) {
    val gamePlayer = new TTTPlayer()
    println("Let's play Tic-Tac-Toe. You are X and go first.")
    gamePlayer.playGameAgainstHuman()
  }
}

/** Text based tic-tac-toe player.
  * First learn, then play.
  */
class TTTPlayer {

  private val table = new QTable(TTTBoard(), epsilon = 0.05)
  private val learner = new QLearner[Int]()
  private val scanner = new Scanner(System.in)

  def playGameAgainstHuman(): Unit = {
    learner.learn(table, 10000)
    val finalState = playTheGame()
    showOutcome(finalState)
  }

  /** @return the final game state when no moves left */
  private def playTheGame(): TTTBoard = {
    var state: TTTBoard = TTTBoard()

    while (!state.isWonByLastMove && state.hasTransitions) {
      println("--------")
      println(state.toString)
      if (state.playerToMove == 'X') {
        println("move position [1 - 9]?")
        var pos = scanner.nextInt()
        while (pos < 1 || pos > 9 || state.playedAlready(pos - 1)) {
          println("Invalid. Try again.")
          pos = scanner.nextInt()
        }

        state = state.makeTransition(pos - 1)
      } else {
        state = state.makeTransition(table.getBestMove(state)._1)
      }
    }
    state
  }

  private def showOutcome(finalState: TTTBoard): Unit = {
    println("\n" + finalState.toString)
    if (finalState.isWon('X')) {
      println("*** Congratulations - you won! ***")
    }
    else if (finalState.isWon('O')) {
      println("*** You lost! Better luck next time ***")
    }
    else {
      println("*** A tie! ****")
    }
  }
}