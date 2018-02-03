package com.barrybecker4.qlearning.ttt

/**
  * Immutable tic-tac-toe board state and its operations.
  * @param state current state of the board as a string like "X.OOO.X.X"
  * @author Barry Becker
  */
case class TTTBoard(state: String = ".........", playerToMove: Char = 'X') {

  def isWon: Boolean = isWon('X') || isWon('O')
  def hasMoves: Boolean = state.contains('.') && !isWon
  def isWonByLastMove: Boolean = isWon(nextPlayerToMove)

  def makeMove(position: Int): TTTBoard = {
    val newState = state.substring(0, position) + playerToMove + state.substring(position + 1)
    TTTBoard(newState, nextPlayerToMove)
  }

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

  def rewardForLastMove: Float = {
    if (isWonByLastMove) {
      if (playerToMove == 'X') 1.0f else -1.0f
    } else 0.0f
  }

  private def nextPlayerToMove = if (playerToMove == 'X') 'O' else 'X'
  private def checkForWin(winStr: String, idx1: Int, idx2: Int, idx3: Int): Boolean =
    ("" + state.charAt(idx1) + state.charAt(idx2) + state.charAt(idx3)) == winStr
  override def toString: String = state.substring(0, 3) + "\n" + state.substring(3, 6) + "\n" + state.substring(6)
}