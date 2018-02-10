package com.barrybecker4.qlearning.ttt

import java.util.Scanner
import com.barrybecker4.qlearning.common.{QLearner, QTable}


object TTTPlayer {
  def main(args:Array[String]) {
    val gamePlayer = new TTTPlayer()
    println("Let's play Tic-Tac-Toe!")
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
    learnHowToPlay()
    val humanGoesFirst = determineIfHumanFirst()
    val finalState = playTheGame(humanGoesFirst)
    showOutcome(finalState, humanGoesFirst)
  }

  private def learnHowToPlay(): Unit = {
    print("Learning...")
    learner.learn(table, 200000)
    println("...I just learned how to play.\n")
  }

  private def determineIfHumanFirst(): Boolean = {
    println("Would you like to go first? (y/n)")
    print("[y]")
    val answer = scanner.nextLine()
    answer.isEmpty || answer.head.toUpper == 'H'
  }

  /** @return the final game state when no moves left */
  private def playTheGame(humanGoesFirst: Boolean): TTTBoard = {
    var state: TTTBoard = TTTBoard()
    val humanSymbol = if (humanGoesFirst) 'X' else 'O'

    while (!state.isWonByLastMove && state.hasTransitions) {
      println("--------")
      println(state.toString)
      if (state.playerToMove == humanSymbol) {
        println("move position [1 - 9]?")
        var pos = scanner.nextInt()
        while (pos < 1 || pos > 9 || state.occupied(pos - 1)) {
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

  private def showOutcome(finalState: TTTBoard, humanGoesFirst: Boolean): Unit = {
    println("\n" + finalState.toString)
    if (finalState.isWon(if (humanGoesFirst) 'X' else 'O')) {
      println("*** Congratulations - you won! ***")
    }
    else if (finalState.isWon(if (humanGoesFirst) 'O' else 'X')) {
      println("*** You lost! Better luck next time ***")
    }
    else {
      println("*** A tie! ****")
    }
  }
}