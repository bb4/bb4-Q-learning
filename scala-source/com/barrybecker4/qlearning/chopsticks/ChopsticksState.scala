package com.barrybecker4.qlearning.chopsticks

import com.barrybecker4.qlearning.chopsticks.ChopsticksState.{DONE_STATE, TransType}
import com.barrybecker4.qlearning.common.State
import scala.util.Random
import ChopsticksState.NUM_FINGERS_PER_HAND


object ChopsticksState {

  /** First number is the hand that is tapping (1 .. NUM_HANDS_PER_PERSON).
    * The second number is the hand being tapped (opponent hand (1 .. NUM_HANDS_PER_PERSON), or own other hand 0).
    */
  type TransType = (Byte, Byte)

  /** You can try learning with other than 5 fingers per hand if we want */
  private val NUM_FINGERS_PER_HAND = 5

  private val DONE_STATE = (0, 0)
}

/**
  * Always from the point of view of the player moving next
  */
case class ChopsticksState(playerHands: (Byte, Byte) = (1, 1),
                           opponentHands: (Byte, Byte) = (1, 1)) extends State[TransType]{

  def isWon: Boolean = playerHands == DONE_STATE
  def isLost: Boolean = opponentHands == DONE_STATE

  /** @return true if at least one valid transition from this state */
  override def hasTransitions: Boolean =
    playerHands != DONE_STATE && opponentHands != DONE_STATE

  /** Apply a transition to this state to make a new one.
    * @param transition A tuple where the first number is the players hand doing the tapping, and the second is the
    * opponent hand to tap (or self tap if 0).
    */
  override def makeTransition(transition: TransType): ChopsticksState = {
    transition._2 match {
      case 0 => doSelfSplit(transition)
      case oppHandIdx: Byte => tapOpponent(transition, oppHandIdx)
    }
  }

  private def doSelfSplit(transition: TransType): ChopsticksState ={
    val sum = playerHands._1 + playerHands._2
    assert(sum % 2 == 0, "Did not have an even sum of fingers!")
    val selfAvgFinders = (sum / 2).toByte
    ChopsticksState(opponentHands, (selfAvgFinders, selfAvgFinders))
  }

  private def tapOpponent(transition: TransType, oppHandIdx: Byte): ChopsticksState = {
    val ownFingersToAdd: Byte = if (transition._1 == 1) playerHands._1 else playerHands._2
    assert(ownFingersToAdd > 0, "You cannot tap with an inactive hand.")
    val newOppsHands =
      if (oppHandIdx == 1) ((opponentHands._1 + ownFingersToAdd).toByte, opponentHands._2)
      else (opponentHands._1, (opponentHands._2 + ownFingersToAdd).toByte)
    if (newOppsHands._1 >= NUM_FINGERS_PER_HAND)
      ChopsticksState((0, newOppsHands._2), playerHands)
    else if (newOppsHands._2 >= NUM_FINGERS_PER_HAND)
      ChopsticksState((newOppsHands._1, 0), playerHands)
    else
      ChopsticksState(newOppsHands, playerHands)
  }

  /** @return all legal transitions from the current state */
  override def getLegalTransitions: Seq[TransType] = {
    val sumFingers = playerHands._1 + playerHands._2
    val splitTransition = if (sumFingers % 2 == 0) Seq((1.toByte, 0.toByte)) else Seq()
    val firstHandMoves = if (playerHands._1 != 0) Seq((1.toByte, 1.toByte), (1.toByte, 2.toByte)) else Seq()
    val secondHandMoves = if (playerHands._2 != 0) Seq((2.toByte, 1.toByte), (2.toByte, 2.toByte)) else Seq()
    splitTransition ++ firstHandMoves ++ secondHandMoves
  }

  /** @return the amount of reward for the transition to this state */
  override def rewardForLastMove: Float = if (isWon) 1 else if (isLost) -1 else 0

  /** @return the best action to take from the current state */
  override def selectBestAction(actionList: Seq[(TransType, Float)], rnd: Random): (TransType, Float) = {
    val bestValue = actionList.map(_._2).max
    val bestActions = actionList.filter(_._2 == bestValue)
    bestActions(rnd.nextInt(bestActions.length))
  }
  override def toString: String = s"Player to move: $playerHands. Opposing player: $opponentHands"
}
