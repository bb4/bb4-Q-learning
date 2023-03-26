package com.barrybecker4.qlearning.tttminimax

import com.barrybecker4.qlearning.tttminimax.BoardState.{COMPUTER, HUMAN}
import scala.annotation.tailrec


/**
  * Unbeatable tic-tac-toe using minimax.
  * Initially written by chatgpt, but debugged and refactored by me :)
  */
object TicTacToe {

  def main(args: Array[String]): Unit = {
    var state: BoardState = new BoardState(HUMAN)

    println("Welcome to Tic-Tac-Toe!")
    state.print()

    while (!state.isGameOver) {
      val newState =
        if (state.player == HUMAN) humanMove(state)
        else computerMove(state)
      newState.print()
      state = newState
    }

    state.print()
    state.printWinner()
  }

  @tailrec
  private def humanMove(state: BoardState): BoardState = {
    println("Your move! Enter a number from 1 to 9 to make your move:")
    val position = scala.io.StdIn.readInt() - 1
    if (position < 0 || position > 8 || state._1(position).isDefined) {
      println("Invalid move. Please choose an empty space.")
      humanMove(state)
    } else {
      state.makeHumanMove(position)
    }
  }

  def computerMove(state: BoardState): BoardState = 
    val (bestScore, bestMove) = minimax(state)
    state.makeComputerMove(bestMove)

  private def minimax(state: BoardState): (Int, Int) = {
    val availableMoves = state.getAvailableMoves
    if (state.isGameOver) {
      (state.getScore, 0)  // computer won
    } else {
      val initial = if (state.player == COMPUTER) Int.MaxValue else Int.MinValue
      val (bestScore: Int, bestMove: Int) = availableMoves.foldLeft((initial, -1)) {
        case ((score, move), m) =>
          val newBoard = state.board.updated(m, Some(state.player))
          val (newScore, _) = minimax(BoardState(newBoard, if (state.player == HUMAN) COMPUTER else HUMAN))
          if (state.player == 'X' && newScore > score || state.player == COMPUTER && newScore < score) (newScore, m)
          else (score, move)
      }
      (bestScore, bestMove)
    }
  }

}