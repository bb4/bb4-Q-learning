package com.barrybecker4.qlearning.ttt

import scala.util.Random

import QTable.createInitializedTable
import scala.collection.mutable
import QTable._


object QTable {

  val RND = new Random(0)

  /** @return a map from all possible to state to a map of actions to their expected value */
  def createInitializedTable(): Map[TTTBoard, mutable.Map[Int, Float]] = {
    val table = mutable.Map[TTTBoard, mutable.Map[Int, Float]]()
    traverse(TTTBoard(), 'X', table)
    table.map(entry => (entry._1, entry._2)).toMap // make immutable
  }

  /**
    * Recursively traverse all possible board states using DFS.
    * Terminate if come to a win or a position already seen.
    */
  private def traverse(currentState: TTTBoard, playerToMove: Char,
                       table: mutable.Map[TTTBoard, mutable.Map[Int, Float]]): Unit = {
    if (!table.contains(currentState)) {
      val moves = if (currentState.isWon) mutable.Map[Int, Float]() else createPossibleMoves(currentState)
      table(currentState) = moves
      for (position <- moves.keys) {
        val nextPlayerToMove = if (playerToMove == 'X') 'O' else 'X'
        traverse(currentState.makeMove(position, playerToMove), nextPlayerToMove, table)
      }
    }
  }

  private def createPossibleMoves(board: TTTBoard): mutable.Map[Int, Float] = {
    val m = mutable.Map[Int, Float]()
    for (i <- 0 until 9 if board.state.charAt(i) == '.')
      m(i) = 0.0f
    m
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
  *
  * TODO: pull out to qlearning/common
  */
class QTable(rnd: Random = RND) {

  var table: Map[TTTBoard, mutable.Map[Int, Float]] = createInitializedTable()

  /** The selected action gets less random over time
    * @return the position to move to next
    */
  def getNextAction(b: TTTBoard, episodeNumber: Int): (Int, Float) = {
    val actions = table(b)
    val actionsList: List[(Int, Float)] =
      actions.toList.map(entry => (entry._1, entry._2 + rnd.nextInt(actions.size) * (1.0f / (episodeNumber + 1))))
    //println("actionsList = " + actionsList.mkString(", "))
    val idx = actionsList.max(Ordering.by((_ : (Int, Float))._2))._1
    (idx, actions(idx))
  }

  /**
    * Update QTable with new knowledge.
    * Q[s,a] = Q[s,a] + learningRate*(reward + futureRewardDiscount * max(Q[s1,:]) - Q[s,a])
    */
  def update(b: TTTBoard, action: (Int, Float), nextBoard: TTTBoard, reward: Float,
             learningRate: Float, futureRewardDiscount: Float = 1.0f): Unit = {
    val actions = table(nextBoard)
    val futureValue =
      if (actions.isEmpty) 0.0f else actions.values.max
    val newValue = action._2 + learningRate * ((reward + futureRewardDiscount * futureValue) - action._2)
    table(b) += (action._1 -> newValue)
//    if (newValue != 0.0f && Math.abs(newValue) != 1.0) {
//      println("table of " + b.toString + " " + table(b).mkString(",  ") + "  newValue=" + newValue + " reward = " + reward)
//    }
  }

  // for testing only
  def getActions(b: TTTBoard): mutable.Map[Int, Float] = table(b)

  override def toString: String = "numEntries=" + table.size + " first 10 entries:\n" + table.take(10).mkString("\n")
}
