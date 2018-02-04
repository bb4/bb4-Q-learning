package com.barrybecker4.qlearning.common

import com.barrybecker4.qlearning.common.QTable._
import scala.collection.mutable
import scala.util.Random


object QTable {

  val RND = new Random(0)

  /** There is always at least this probability that a random move will be selected.
    * Values between 0.01 and 0.1 are good, but more experimentation needed. */
  val DEFAULT_EPS = 0.1

  /** The larger this value, the more slowly epsilon decreases (i.e. the probability of making random moves) */
  val EPS_DROPOFF = 5.0f
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
  * @param initialState the initial state of the game (or whatever)
  * @param epsilon percent of the time to make a random move instead of make the best one as indicated by the model.
  * @param rnd used for deterministic unit tests.
  * @author Barry Becker
  */
class QTable[T](val initialState: State[T], epsilon: Double = DEFAULT_EPS, rnd: Random = RND) {

  var table: Map[State[T], mutable.Map[T, Float]] = createInitializedTable()

  /** @return the best transition from the current state, from point of view of current player */
  def getBestMove(b: State[T]): (T, Float) = {
    val actionsList = table(b).toSeq
    println("the possible actions are " + actionsList.mkString(", "))
    val action = b.selectBestAction(actionsList, rnd)
    println("of those, we select " + action)
    action
  }

  /** The selected action gets less random over time
    * @return the position to move to next
    */
  def getNextAction(b: State[T], episodeNumber: Int): (T, Float) = {
    val actions = table(b)
    val actionList: List[(T, Float)] =
      actions.toList.map(entry => (entry._1, entry._2))

    val eps = epsilon + EPS_DROPOFF / (episodeNumber + EPS_DROPOFF)
    if (rnd.nextDouble() < eps) actionList(rnd.nextInt(actionList.length)) // purely random action
    else b.selectBestAction(actionList, rnd) // select randomly from actions with best value
  }

  /** Update QTable with new knowledge.
    * Q[s,a] = Q[s,a] + learningRate * (reward + futureRewardDiscount * max(Q[s1,:]) - Q[s,a])
    * @param board last board
    * @param action transition that takes us from b to nextBoard. It's also the action who's value to update.
    */
  def update(board: State[T], action: (T, Float), nextBoard: State[T],
             learningRate: Float, futureRewardDiscount: Float = 1.0f): Unit = {
    val nextActions = table(nextBoard)
    val futureValue = if (nextActions.isEmpty) 0.0f else nextBoard.selectBestAction(nextActions.toSeq, rnd)._2
    val reward = nextBoard.rewardForLastMove
    val newValue = action._2 + learningRate * ((reward + futureRewardDiscount * futureValue) - action._2)
    table(board) += (action._1 -> newValue)  // update
  }

  def getActions(b: State[T]): mutable.Map[T, Float] = table(b)

  def getFirstNEntriesWithNon0Actions(n: Int): String =
    table.filter(e => e._2.values.sum > 0.0f).take(n).mkString("\n")

  /** @return a map from all possible states to a map of possible actions to their expected value */
  def createInitializedTable(): Map[State[T], mutable.Map[T, Float]] = {
    val table = mutable.Map[State[T], mutable.Map[T, Float]]()
    traverse(initialState, table)
    table.map(entry => (entry._1, entry._2)).toMap // make immutable
  }

  /** Recursively traverse all possible board states using DFS. */
  private def traverse(currentState: State[T],
                       table: mutable.Map[State[T], mutable.Map[T, Float]]): Unit = {
    if (!table.contains(currentState)) {
      val possibleMoves = currentState.getLegalTransitions
      val moves: mutable.Map[T, Float] = mutable.Map(possibleMoves.map(m => (m, 0.0f)): _*)
      table(currentState) = moves
      for (position <- moves.keys) {
        traverse(currentState.makeTransition(position), table)
      }
    }
  }

  override def toString: String = "numEntries=" + table.size
}