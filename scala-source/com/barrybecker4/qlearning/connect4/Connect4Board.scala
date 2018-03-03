package com.barrybecker4.qlearning.connect4


import com.barrybecker4.qlearning.common.State
import scala.util.Random
import Connect4Board._

object Connect4Board {
  val NUM_ROWS = 6
  val NUM_COLS = 7
  val NUM_SPACES: Int = NUM_ROWS * NUM_COLS
}

/** Immutable Connect4 board state and its operations.
  * See https://en.wikipedia.org/wiki/Connect_Four
  *
  * The state is a string and represents the pieces in row order from the bottom up until the last placed piece.
  * That way all the empty positions do not need to be part of the string (this should save significant space).
  *
  * For example, the current board state would be "XXO....OXO" for the actual board configuration of
  * .......
  * .......
  * .......
  * .......
  * OXO....
  * XXO....
  *
  * Since there are the same number X's as O's, it has to be X's turn (X goes first).
  * The possible moves are 3, 4, 5, 6, 14, 15, 16,  corresponding to the indices of the empty positions.
  *
  * Since the size of the space is small, we can use a table, but for more complex games, like go for example,
  * we need to use a model like a deep neural net to approximate the total space of possible board positions.
  *
  * @param state current state of the board as a string like "XXOOX.O.OXOX...O"
  * @author Barry Becker
  */
case class Connect4Board(state: String = "", playerToMove: Char = 'X') extends State[Int]{

  def hasTransitions: Boolean = (state.length < NUM_SPACES || state.contains(".")) && !isWon
  def occupied(pos: Int): Boolean = pos < state.length && state.charAt(pos) != '.'

  def makeTransition(position: Int): Connect4Board = {
    val newState = if (position < state.length)
      state.substring(0, position) + playerToMove + state.substring(position + 1)
    else state + "." * (position - state.length) + playerToMove
    Connect4Board(newState, nextPlayerToMove)
  }

  def isWonByLastMove: Boolean = isWon(nextPlayerToMove)
  def isWon: Boolean = isWon('X') || isWon('O')

  def isWon(player: Char): Boolean = {
    val winStr = "" + player + player + player + player
    isRowWin(winStr) || isColumnWin(winStr) || isDiagonalWin(player)
  }

  private def isRowWin(winStr: String): Boolean = {
    var row = 0
    while (row * NUM_COLS < state.length && row < NUM_ROWS) {
      val rowStr = state.substring(row * NUM_COLS, Math.min(state.length, (row + 1) * NUM_COLS))
      if (rowStr.contains(winStr)) return true
      row += 1
    }
    false
  }

  // this could be optimized
  private def isColumnWin(winStr: String): Boolean = {
    if (state.length > 3 * NUM_COLS) {
      for (col <- 0 until NUM_COLS) {
        var row = 0
        var colStr = ""
        while (row < NUM_ROWS && occupied(row * NUM_COLS + col)) {
          colStr += state.charAt(row * NUM_COLS + col)
          row += 1
        }
        if (colStr.contains(winStr)) return true
      }
    }
    false
  }

  private def isDiagonalWin(player: Char): Boolean = {
    for (row <- 0 to state.length / NUM_COLS - 3) {
      for (col <- 0 until NUM_COLS) {
        val pos = row * NUM_COLS + col
        // check both diagonals for each "player" piece in the bottom rows
        if (state(pos) == player && (checkDiagonal(player, pos, 1) || checkDiagonal(player, pos, -1)))
            return true
      }
    }
    false
  }

  private def checkDiagonal(player: Char, pos: Int, direction: Int): Boolean = {
    var runlen = 1
    var nextPos = pos + NUM_COLS + direction
    while (nextPos < state.length && state(nextPos) == player) {
      runlen += 1
      nextPos += NUM_COLS + direction
    }
    runlen == 4
  }

  def getLegalTransitions: Seq[Int] = {
    if (isWonByLastMove) Seq()
    else {
      val topRow = state.length / NUM_COLS + 1
      var nextDown = 0
      (for (col <- 0 until NUM_COLS) yield {
        var empyPos = topRow * NUM_COLS + col
        nextDown = empyPos - NUM_COLS
        while (nextDown >= 0 && !occupied(nextDown)) {
          nextDown -= NUM_COLS
        }
        if (nextDown >= NUM_SPACES) None else Some(nextDown)
      }).flatten
    }
  }

  def rewardForLastMove: Float = {
    if (isWonByLastMove) { if (nextPlayerToMove == 'X') 1.0f else -1.0f }
    else 0.0f
  }

  /** @return the best action to take from the perspective of the current player.
    *  If more than one best, choose randomly from among them.
    */
  def selectBestAction(actionList: Seq[(Int, Float)], rnd: Random): (Int, Float) = {
    val bestValue = if (playerToMove == 'X') actionList.map(_._2).max else actionList.map(_._2).min
    val bestActions = actionList.filter(_._2 == bestValue)
    bestActions(rnd.nextInt(bestActions.length))
  }

  private def nextPlayerToMove = if (playerToMove == 'X') 'O' else 'X'

  override def toString: String = {
    val numRows = state.length / NUM_COLS
    var s = (1 to NUM_COLS).mkString("") + "\n"
    for (row <- NUM_ROWS - 1 until numRows by -1) s += "." * NUM_COLS + "\n"
    for (row <- numRows to 0 by -1) {
      val endCol = (row + 1) * NUM_COLS
      val txt = state.substring(row * NUM_COLS, Math.min(state.length, endCol))
      if (state.length < endCol) s += txt + "." * (endCol - state.length) + "\n"
      else s += txt + "\n"
    }
    s
  }
}