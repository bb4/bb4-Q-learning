package com.barrybecker4.qlearning.frozenlake

import com.barrybecker4.qlearning.common.State
import scala.util.Random
import Direction.Direction


/** Immutable state of the lake.
  * The main difference between difference states is the position of the human on the lake.
  * @author Barry Becker
  */
case class LakeState(location: Location, lake: Lake) extends State[Direction]{

  def hasTransitions: Boolean = !(lake.isInHole(location) || lake.isGoal(location))

  def makeTransition(direction: Direction): LakeState = {
    val newLocation = lake.attemptToMoveInDirection(location, direction)
    LakeState(newLocation, lake)
  }

  def isGoalReached: Boolean = lake.isGoal(location)
  def getLegalTransitions: Seq[Direction] = lake.getLegalTransitionsFrom(location)


  def rewardForLastMove: Float = {
    if (lake.isInHole(location)) -1.0f
    else if (lake.isGoal(location)) 1.0f
    else 0.0f
  }

  /** @return the best action to take assuming no wind. If more than one best, choose randomly from among them.  */
  def selectBestAction(actionList: Seq[(Direction, Float)], rnd: Random): (Direction, Float) = {
    val bestValue = actionList.map(_._2).max
    val bestActions = actionList.filter(_._2 == bestValue)
    bestActions(rnd.nextInt(bestActions.length))
  }

  override def toString: String = s"Location = $location \n$lake"
}