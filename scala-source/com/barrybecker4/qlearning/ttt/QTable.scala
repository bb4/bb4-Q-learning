package com.barrybecker4.qlearning.ttt

import QTable.createInitializedTable


object QTable {

  /** @return a map from all possible to state to a map of actions to their expected value */
  def createInitializedTable(): Map[TTTBoard, Map[Int, Float]] = {
    Map()
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
