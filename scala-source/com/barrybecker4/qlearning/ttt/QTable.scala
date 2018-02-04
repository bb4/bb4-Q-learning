package com.barrybecker4.qlearning.ttt

import scala.util.Random
import scala.collection.mutable
import QTable._


object QTable {

  val RND = new Random(0)

  /** There is always at least this probability that a random move will be selected.
    * Values betwee 0.01 and 0.1 are good, but more experimentation needed. */
  val EPS = 0.02

  /** The larger this value, the more slowly epsilon decreases (i.e. the probability of making random moves) */
  val EPS_DROPOFF = 1.0f

  /** @return a map from all possible states to a map of possible actions to their expected value */
  def createInitializedTable(): Map[TTTBoard, mutable.Map[Int, Float]] = {
    val table = mutable.Map[TTTBoard, mutable.Map[Int, Float]]()
    traverse(TTTBoard(), table)
    table.map(entry => (entry._1, entry._2)).toMap // make immutable
  }

  /** Recursively traverse all possible board states using DFS.
    * Terminate if come to a win or a position already seen.
    */
  private def traverse(currentState: TTTBoard,
                       table: mutable.Map[TTTBoard, mutable.Map[Int, Float]]): Unit = {
    if (!table.contains(currentState)) {
      val moves = if (currentState.isWonByLastMove) mutable.Map[Int, Float]()
                  else createPossibleMoves(currentState)
      table(currentState) = moves
      for (position <- moves.keys) {
        traverse(currentState.makeMove(position), table)
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
  * The possible moves are 1, 3, 6, or 7 corresponding to the indices of the empty positions.
  * For each of those moves, there will be a floating point number which is an estimate of the value
  * of making that particular move. Initially all those values are 0.
  *
  * For Tic Tac Toe, the number of possible board states is about 5,478 - far fewer than most games.
  * Since the size of the space is small, we can use a table, but for more complex games, like go for example,
  * we need to use a model like a deep neural net to approximate the total space of possible board positions.
  *
  * Currently the size of the table is 5,478, but could be reduced by a factor of 8 by taking advantage of symmetry.
  *
  * TODO: make generic and pull out to qlearning/common
  * @author Barry Becker
  */
class QTable(rnd: Random = RND) {

  var table: Map[TTTBoard, mutable.Map[Int, Float]] = createInitializedTable()

  /** @return the best transition from the current state, from point of view of current player */
  def getBestMove(b: TTTBoard): (Int, Float) = {
    val actions = table(b)
    val actionList = actions.toSeq
    println("the possible actions are " + actions.mkString(", "))
    val action = b.selectBestAction(actionList, rnd)
    println("of those, we select " + action)
    action
  }

  /** The selected action gets less random over time
    * @return the position to move to next
    */
  def getNextAction(b: TTTBoard, episodeNumber: Int): (Int, Float) = {
    val actions = table(b)
    val actionList: List[(Int, Float)] =
      actions.toList.map(entry => (entry._1, entry._2))

    val eps = EPS + EPS_DROPOFF / (episodeNumber + EPS_DROPOFF)
    val selectedAction = if (rnd.nextDouble() < eps) {
      actionList(rnd.nextInt(actionList.length)) // purely random action
    } else {
      // select randomly from actions with best value
      b.selectBestAction(actionList, rnd)
    }

    selectedAction
  }

  /** Update QTable with new knowledge.
    * Q[s,a] = Q[s,a] + learningRate * (reward + futureRewardDiscount * max(Q[s1,:]) - Q[s,a])
    * @param board last board
    * @param action transition that takes us from b to nextBoard. It's also the action who's value to update.
    */
  def update(board: TTTBoard, action: (Int, Float), nextBoard: TTTBoard,
             learningRate: Float, futureRewardDiscount: Float = 1.0f): Unit = {
    val nextActions = table(nextBoard)
    val futureValue = if (nextActions.isEmpty) 0.0f else nextBoard.selectBestAction(nextActions.toSeq, rnd)._2
    val reward = nextBoard.rewardForLastMove
    val newValue = action._2 + learningRate * ((reward + futureRewardDiscount * futureValue) - action._2)
    table(board) += (action._1 -> newValue)
  }

  def getActions(b: TTTBoard): mutable.Map[Int, Float] = {
    table(b)
  }

  def getFirstNEntriesWithNon0Actions(n: Int): String =
    table.filter(e => e._2.values.sum > 0.0f).take(n).mkString("\n")

  override def toString: String = "numEntries=" + table.size
}
