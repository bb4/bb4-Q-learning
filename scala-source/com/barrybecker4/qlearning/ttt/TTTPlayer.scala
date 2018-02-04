package com.barrybecker4.qlearning.ttt

import java.util.Scanner

object TTTPlayer {
  def main(args:Array[String]) {
    val gamePlayer = new TTTPlayer()
    println("Let's play Tic-Tac-Toe. You are X and go first.")
    gamePlayer.playGameAgainstHuman()
  }
}

/**
  * Text based tic-tac-toe player.
  * First learn, then play.
  */
class TTTPlayer {

  private val table = new QTable()
  private val learner = new QLearner()
  private val scanner = new Scanner(System.in)

  def playGameAgainstHuman(): Unit = {
    learner.learn(table, 100000)
    println("top "+ table.getFirstNEntriesWithNon0Actions(20))
    println("actions for " + "....X.... are "  + table.getActions(TTTBoard("....X....", 'O')).mkString(", "))

    val finalState = playTheGame()
    showOutcome(finalState)
  }

  /** @return the finale game state when no moves left */
  private def playTheGame(): TTTBoard = {
    var state: TTTBoard = TTTBoard()

    while (!state.isWonByLastMove && state.hasMoves) {
      println("--------")
      println(state.toString)
      if (state.playerToMove == 'X') {
        println("move position [1 - 9]?")
        var pos = scanner.nextInt()
        while (pos < 1 && pos > 9) {
          println("Invalid. Try again.")
          pos = scanner.nextInt()
        }

        state = state.makeMove(pos - 1)
      } else {
        state = state.makeMove(table.getBestMove(state)._1)
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