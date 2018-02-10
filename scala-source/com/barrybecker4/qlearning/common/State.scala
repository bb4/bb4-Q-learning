package com.barrybecker4.qlearning.common

import scala.util.Random

trait State[T] {

  /** @return true if at least one valid transition from this state */
  def hasTransitions: Boolean

  /** Apply a transition to this state to make a new one */
  def makeTransition(transition: T): State[T]

  /** all legal transitions from the current state */
  def getLegalTransitions: Seq[T]

  /** @return the amount of reward for the transition to this state */
  def rewardForLastMove: Float

  /** @return the best action to take */
  def selectBestAction(actionList: Seq[(T, Float)], rnd: Random): (T, Float)
}
