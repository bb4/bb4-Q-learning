package com.barrybecker4.qlearning.tttminimax
import scala.annotation.tailrec

/**
  * Unbeatable tic-tac-toe using minimax
  * Mostly written by chatgpt, but debugged by me :)
  */
object TicTacToe {
  val HUMAN = 'X'
  val COMPUTER = 'O'
  type Board = List[Option[Char]]
  val emptyBoard: Board = List.fill(9)(None)

  def main(args: Array[String]): Unit = {
    var initialState: (Board, Char) = (emptyBoard, HUMAN)

    println("Welcome to Tic-Tac-Toe!")
    printBoard(initialState._1)

    while (!isGameOver(initialState._1)) {
      val newState =
        if (initialState._2 == HUMAN) humanMove(initialState)
        else computerMove(initialState, COMPUTER)
      printBoard(newState._1)
      initialState = newState
    }

    printBoard(initialState._1)
    printWinner(initialState._1)
  }

  @tailrec
  private def humanMove(state: (Board, Char)): (Board, Char) = {
    println("Your move! Enter a number from 1 to 9 to make your move:")
    val index = scala.io.StdIn.readInt() - 1
    if (index < 0 || index > 8 || state._1(index).isDefined) {
      println("Invalid move. Please choose an empty space.")
      humanMove(state)
    } else {
      (state._1.updated(index, Some(state._2)), COMPUTER)
    }
  }

  def computerMove(state: (Board, Char), computerPlayer: Char): (Board, Char) = {
    val (bestScore, bestMove) = minimax(state._1, computerPlayer)
    assert(bestMove >= 0, s"bestMove=$bestMove  must be >=0 or it means that there was no move to make. \nBoard=" +
      serializeBoard(state._1))
    (state._1.updated(bestMove, Some(computerPlayer)), 'X')
  }

  private def minimax(board: Board, player: Char): (Int, Int) = {
    val availableMoves = getAvailableMoves(board)
    if (isGameOver(board)) {
      (getScore(board), -1)
    } else {
      val initial = if (player == COMPUTER) Int.MaxValue else Int.MinValue
      val (bestScore, bestMove) = availableMoves.foldLeft((initial, -1)) {
        case ((score, move), m) =>
          val newBoard = board.updated(m, Some(player))
          val (newScore, _) = minimax(newBoard, if (player == HUMAN) COMPUTER else HUMAN)
          if (player == 'X' && newScore > score || player == COMPUTER && newScore < score) (newScore, m) else (score, move)
      }
      (bestScore, bestMove)
    }
  }

  private def getAvailableMoves(board: Board): List[Int] =
    board.zipWithIndex.collect { case (None, index) => index }

  private def isGameOver(board: Board): Boolean =
    hasPlayerWon(board, HUMAN) || hasPlayerWon(board, COMPUTER) || board.forall(_.isDefined)

  private def hasPlayerWon(board: Board, player: Char): Boolean = {
    val winningPositions = List(
      List(0, 1, 2), List(3, 4, 5), List(6, 7, 8), // Rows
      List(0, 3, 6), List(1, 4, 7), List(2, 5, 8), // Columns
      List(0, 4, 8), List(2, 4, 6) // Diagonals
    )
    winningPositions.exists(position => position.forall(board(_).contains(player)))
  }

  private def printWinner(board: Board): Unit = {
    val score = getScore(board)
    if (score < 0) println("The winner is the computer!")
    else if (score > 0) println("The winner is you!")
    else println("A tie!")
  }

  private def getScore(board: Board): Int = {
    if (hasPlayerWon(board, HUMAN)) 1
    else if (hasPlayerWon(board, COMPUTER)) -1
    else 0
  }

  def printBoard(board: Board): Unit = println(serializeBoard(board))

  def serializeBoard(board: Board): String =
    serializeRow(board.take(3)) + serializeRow(board.slice(3, 6)) + serializeRow(board.drop(6))

  private def serializeRow(row: List[Option[Char]]): String =
    row.map(c => if (c.isDefined) c.get else ".").mkString("[", "", "]") + "\n"
}