package com.barrybecker4.qlearning.connect4

import java.util.Scanner

import com.barrybecker4.qlearning.common.{QLearner, QTable}


object Connect4Player {
  def main(args:Array[String]) {
    val gamePlayer = new Connect4Player()
    println("Let's play Connect 4!")
    gamePlayer.playGameAgainstHuman()
  }
}


class Connect4Player {

  println("The qtable has too many states (4,531,985,219,092)... this will take forever")
  private val table = new QTable(Connect4Board(), None, epsilon = 0.05)
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
    learner.learn(table, 10000)
    println("...I just learned how to play.\n")
  }

  private def determineIfHumanFirst(): Boolean = {
    println("Would you like to go first? (y/n)")
    print("[y]")
    var answer = scanner.nextLine()
    answer.isEmpty || answer.head.toUpper == 'Y'
  }

  /** @return the final game state when no moves left */
  private def playTheGame(humanGoesFirst: Boolean): Connect4Board = {
    var state: Connect4Board = Connect4Board()
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

  private def showOutcome(finalState: Connect4Board, humanGoesFirst: Boolean): Unit = {
    println("\n" + finalState.toString)
    if (finalState.isWon(if (humanGoesFirst) 'X' else 'O'))
      println("*** Congratulations - you won! ***")
    else if (finalState.isWon(if (humanGoesFirst) 'O' else 'X'))
      println("*** You lost! Better luck next time ***")
    else
      println("*** A tie! ****")
  }
}
