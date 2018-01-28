package com.barrybecker4.qlearning.ttt

/**
  * Immutable tic-tac-toe board state and its operations.
  * @param state current state of the board as a string like "X.OOO.X.X"
  */
class TTTBoard(val state: String) {

  override def toString: String = state.substring(0, 3) + "\n" + state.substring(3, 6) + "\n" + state.substring(6)
}
