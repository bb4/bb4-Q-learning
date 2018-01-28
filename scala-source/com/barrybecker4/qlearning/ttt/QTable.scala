package com.barrybecker4.qlearning.ttt

import QTable.createInitializedTable

import scala.collection.mutable


object QTable {

  /** @return a map from all possible to state to a map of actions to their expected value */
  def createInitializedTable(): Map[TTTBoard, Map[Int, Float]] = {

    val initialBoardState = new TTTBoard(".........")

    val table = mutable.Map[TTTBoard, Map[Int, Float]]()
    traverse(initialBoardState, 'X', table)
    table.map(entry => (entry._1, entry._2)).toMap
  }

  /**
    * Recursively traverse all possible board states using DFS.
    * Terminate if come to a win or a position already seen.
    */
  private def traverse(currentState: TTTBoard, playerToMove: Char,
                       table: mutable.Map[TTTBoard, Map[Int, Float]]): Unit = {
    if (!table.contains(currentState) && !currentState.isWon) {
      val moves: Map[Int, Float] = createPossibleMoves(currentState)
      table(currentState) = moves
      for (position <- moves.keys) {
        val nextPlayerToMove = if (playerToMove == 'X') 'O' else 'X'
        traverse(currentState.makeMove(position, playerToMove), nextPlayerToMove, table)
      }
    }
  }

  private def createPossibleMoves(board: TTTBoard): Map[Int, Float] = {
    (for (i <- 0 until 9 if board.state.charAt(i) == '.') yield i -> 0.0f).toMap
  }

}

/**
  * Map from board states to possible moves and their values.
  * For example, the current board state might be "X.O.XO..X".
  * Since there are more X's than O's, it has to be O's turn.
  * The possible moves are 1, 3, 6, or 7 corresponding ot the indices of the empty positions.
  * For each of those moves there will be a floating point number which is an estimate of the value
  * of making that particular move. Initially all those values are 0.
  *
  * For Tic Tac Toe, the number of possible board states is about 5,478 - far fewer than most games.
  * Since the size of the space is small, we can use a table, but for more complex games, like go for example,
  * we need to use a model like a deep neural net to approximate the total space of possible board positions.
  */
class QTable {

  var table: Map[TTTBoard, Map[Int, Float]] = createInitializedTable()


}
