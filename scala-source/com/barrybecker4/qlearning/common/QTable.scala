package com.barrybecker4.qlearning.common

import com.barrybecker4.qlearning.common.QTable._
import scala.collection.mutable
import scala.util.Random


object QTable {

  /** The seed will be set in unit tests so things are deterministic, but normally we don't care */
  private val RND = new Random((Math.random() * 10000).toLong)

  /** There is always at least this probability that a random move will be selected.
    * Values between 0.01 and 0.1 are good, but more experimentation needed.
    */
  private val DEFAULT_EPS = 0.1

  /** The larger this value, the more slowly epsilon decreases (i.e. the probability of making random moves) */
  private val EPS_DROPOFF = 5.0f

}

/**
  * Map from states to possible moves and their values.
  * For each move transition (T), there will be a floating point number which is an estimate of the value
  * of making that particular move from the current state. Initially all those values are 0.
  *
  * A Qtable works well when the size of the space is relatively small, but for more complex games and puzzles,
  * like go, for example, we need to use a model like a deep neural net to approximate the total space.
  *
  * @param initialState starting state (optional). If provided, then all other states are inferred from this one.
  * @param theTable (Optional) table of all possible states and possible transitions from them.
  *            If not provided, then all states will be inferred from the starting state.
  * @param epsilon percent of the time to make a random transition instead of making the best one.
  *                It actually start out higher than this, and asymptotically approaches this value.
  * @param rnd used for deterministic unit tests.
  * @author Barry Becker
  */
case class QTable[T](initialState: State[T],
                     theTable: Option[Map[State[T], mutable.Map[T, Float]]],
                     epsilon: Double = DEFAULT_EPS, rnd: Random = RND) {

  val table: Map[State[T], mutable.Map[T, Float]] =
    if (theTable.isEmpty) createInitializedTable(initialState) else theTable.get

  /** @return the best transition from the current state, from point of view of current player */
  def getBestMove(b: State[T]): (T, Float) = {
    val actionsList = table(b).toSeq
    //println("the possible actions are " + actionsList.mkString(", "))
    b.selectBestAction(actionsList, rnd)
  }

  /** The selected action gets less random over time
    * @param s the current state
    * @param episodeNumber as the episode number increases, we are less likely to take random actions
    * @return the position to move to next
    */
  def getNextAction(s: State[T], episodeNumber: Int): (T, Float) = {
    val actions = table(s)
    val actionList: List[(T, Float)] =
      actions.toList.map(entry => (entry._1, entry._2))

    val eps = epsilon + EPS_DROPOFF / (episodeNumber + EPS_DROPOFF)
    if (rnd.nextDouble() < eps) actionList(rnd.nextInt(actionList.length)) // purely random action
    else s.selectBestAction(actionList, rnd) // select randomly from actions with best value
  }

  /** Update QTable with new knowledge.
    * Q[s,a] = Q[s,a] + learningRate * (reward + futureRewardDiscount * max(Q[s1,:]) - Q[s,a])
    * @param state last board
    * @param action transition that takes us from b to nextState. It's also the action who's value to update.
    * @param nextState the state that you get by applying the action
    */
  def update(state: State[T], action: (T, Float), nextState: State[T],
             learningRate: Float, futureRewardDiscount: Float = 1.0f): Unit = {
    val nextActions = table(nextState)
    val futureValue = if (nextActions.isEmpty) 0.0f else nextState.selectBestAction(nextActions.toSeq, rnd)._2
    val reward = nextState.rewardForLastMove
    val newValue = action._2 + learningRate * ((reward + futureRewardDiscount * futureValue) - action._2)
//    if (newValue != 0) {
//      println(s"newValue=$newValue (act_2=${action._2} + lr=$learningRate *((rew=$reward + " +
//        s"frd=$futureRewardDiscount * futureVal=$futureValue) - act_2=action._2) \n  for state $state and action $action")
//    }
    table(state) += (action._1 -> newValue)  // update
  }

  def getActions(b: State[T]): mutable.Map[T, Float] = table(b)

  def getFirstNEntriesWithNon0Actions(n: Int): String =
    table.filter(e => e._2.values.sum > 0.0f).take(n).mkString("\n")

  /** @return a map from all possible states to a map of possible actions to their expected value */
  private def createInitializedTable(initialState: State[T]): Map[State[T], mutable.Map[T, Float]] = {
    val table = mutable.Map[State[T], mutable.Map[T, Float]]()
    println("find all states starting from " + initialState)
    traverse(initialState, table)
    println("found " + table.size + " unique states.")
    table.map(entry => (entry._1, entry._2)).toMap // make immutable
  }

  /** Recursively traverse all possible board states using DFS. */
  private def traverse(currentState: State[T],
                       table: mutable.Map[State[T], mutable.Map[T, Float]]): Unit = {
    if (!table.contains(currentState)) {
      val possibleMoves = currentState.getLegalTransitions
      val moves: mutable.Map[T, Float] = mutable.Map(possibleMoves.map(m => (m, 0.0f)): _*)
      //println("adding " + moves.mkString(", ") + " for " + currentState)
      table(currentState) = moves
      for (position <- moves.keys)
        traverse(currentState.makeTransition(position), table)
    }
  }

  override def toString: String = "numEntries=" + table.size
}