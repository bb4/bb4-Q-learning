package com.barrybecker4.qlearning.ttt

import com.barrybecker4.qlearning.common.State
import scala.util.Random

/** Immutable tic-tac-toe board state and its operations.
  * Currently the size of the QTable is 5,478, but could be reduced by a factor of 8 by taking advantage of symmetry.
  *
  * For example, the current board state might be "X.O.XO..X".
  * Since there are more X's than O's, it has to be O's turn.
  * The possible moves are 1, 3, 6, or 7 corresponding to the indices of the empty positions.
  *
  * For Tic Tac Toe, the number of possible board states is about 5,478 - far fewer than most games.
  * Since the size of the space is small, we can use a table, but for more complex games, like go for example,
  * we need to use a model like a deep neural net to approximate the total space of possible board positions.
  *
  * @param state current state of the board as a string like "X.OOO.X.X"
  * @author Barry Becker
  */
case class TTTBoard(state: String = ".........", playerToMove: Char = 'X') extends State[Int]{

  def hasTransitions: Boolean = state.contains('.') && !isWon
  def occupied(pos: Int): Boolean = state.charAt(pos) != '.'

  def makeTransition(position: Int): TTTBoard = {
    val newState = state.substring(0, position) + playerToMove + state.substring(position + 1)
    TTTBoard(newState, nextPlayerToMove)
  }

  def isWonByLastMove: Boolean = isWon(nextPlayerToMove)
  def isWon: Boolean = isWon('X') || isWon('O')

  def isWon(player: Char): Boolean = {
    val winStr = "" + player + player + player
    for (i <- 0 until 3) {
      val idx = 3 * i
      if (checkForWin(winStr, idx, idx + 1, idx + 2)) return true
      if (checkForWin(winStr, i, i + 3, i + 6)) return true
    }
    if (checkForWin(winStr, 0, 4, 8)) return true
    checkForWin(winStr, 2, 4, 6)
  }

  def getLegalTransitions: Seq[Int] = {
    if (isWonByLastMove) Seq()
    else for (i <- 0 until 9 if state.charAt(i) == '.') yield i
  }

  /** @return the amount of reward from the point of view of the player that moved first */
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
  private def checkForWin(winStr: String, idx1: Int, idx2: Int, idx3: Int): Boolean =
    ("" + state.charAt(idx1) + state.charAt(idx2) + state.charAt(idx3)) == winStr
  override def toString: String =
    state.substring(0, 3) + "\n" + state.substring(3, 6) + "\n" + state.substring(6)
}