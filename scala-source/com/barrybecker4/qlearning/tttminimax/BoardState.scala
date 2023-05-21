package com.barrybecker4.qlearning.tttminimax

import com.barrybecker4.qlearning.tttminimax.BoardState.{Board, Player}
import com.barrybecker4.qlearning.tttminimax.BoardState.Player.{HUMAN, COMPUTER}
import scala.annotation.tailrec


object BoardState {
  enum Player(val symbol: Char) {
    case HUMAN extends Player('X')
    case COMPUTER extends Player('O')
  }
  type Board = List[Option[Char]]
}

case class BoardState(board: Board, player: Player) {

  def this(player: Player) = this(List.fill(9)(None), player)

  def makeHumanMove(position: Int): BoardState =
    BoardState(board.updated(position, Some(player.symbol)), COMPUTER)

  def makeComputerMove(position: Int): BoardState =
    BoardState(board.updated(position, Some(player.symbol)), HUMAN)

  def getAvailableMoves: List[Int] =
    board.zipWithIndex.collect { case (None, index) => index }

  def isGameOver: Boolean = hasPlayerWon(COMPUTER) || hasPlayerWon(HUMAN) || noMovesLeft

  def getScore: Int = {
    if (hasPlayerWon(HUMAN)) 1
    else if (hasPlayerWon(COMPUTER)) -1
    else 0
  }

  def print(): Unit = println(serialize())

  def serialize(): String =
    serializeRow(board.take(3)) + serializeRow(board.slice(3, 6)) + serializeRow(board.drop(6))

  def printWinner(): Unit = {
    val score = getScore
    if (score < 0) println("The winner is the computer!")
    else if (score > 0) println("The winner is you!")
    else println("A tie!")
  }

  private def serializeRow(row: List[Option[Char]]): String =
    row.map(c => if (c.isDefined) c.get else ".").mkString("[", "", "]") + "\n"

  private def hasPlayerWon(player: Player): Boolean = {
    val winningPositions = List(
      List(0, 1, 2), List(3, 4, 5), List(6, 7, 8), // Rows
      List(0, 3, 6), List(1, 4, 7), List(2, 5, 8), // Columns
      List(0, 4, 8), List(2, 4, 6) // Diagonals
    )
    winningPositions.exists(position => position.forall(board(_).contains(player.symbol)))
  }

  private def noMovesLeft: Boolean = board.forall(_.isDefined)
}