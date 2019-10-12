package com.barrybecker4.qlearning.chopsticks

import com.barrybecker4.qlearning.chopsticks.ChopsticksState.{DONE_STATE, TransType}
import com.barrybecker4.qlearning.common.State

import scala.util.Random
import ChopsticksState.NUM_FINGERS_PER_HAND
import scala.math.Ordering.Float.TotalOrdering


object ChopsticksState {

  /** First number is the hand that is tapping (1 .. NUM_HANDS_PER_PERSON).
    * The second number is the hand being tapped (opponent hand (1 .. NUM_HANDS_PER_PERSON), or own other hand 0).
    */
  type TransType = (Byte, Byte)

  /** You can try learning with other than 5 fingers per hand if we want */
  private val NUM_FINGERS_PER_HAND = 5

  /** If one player reaches this state (no fingers on either hand), then the other playe has won */
  private val DONE_STATE = (0, 0)
}

/**
  * Always from the point of view of the player moving next
  */
case class ChopsticksState(firstHands: (Byte, Byte) = (1, 1),
                           secondHands: (Byte, Byte) = (1, 1),
                           isFirstPlayerMove: Boolean = true) extends State[TransType]{

  def isWon(player1: Boolean): Boolean =
    if (player1) secondHands == DONE_STATE else firstHands == DONE_STATE

  /** @return true if at least one valid transition from this state */
  override def hasTransitions: Boolean =
    firstHands != DONE_STATE && secondHands != DONE_STATE

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
    if (isFirstPlayerMove) ChopsticksState(doHandSplit(firstHands), secondHands, !isFirstPlayerMove)
    else ChopsticksState(firstHands, doHandSplit(secondHands), !isFirstPlayerMove)
  }

  private def doHandSplit(hands: (Byte, Byte)): (Byte, Byte) ={
    val sum = hands._1 + hands._2
    assert(sum % 2 == 0, "Did not have an even sum of fingers!")
    val selfAvgFingers = (sum / 2).toByte
    (selfAvgFingers, selfAvgFingers)
  }

  private def tapOpponent(transition: TransType, oppHandIdx: Byte): ChopsticksState = {
    if (isFirstPlayerMove) doHandTap(transition, firstHands, secondHands, oppHandIdx)
    else doHandTap(transition, secondHands, firstHands, oppHandIdx)
  }

  private def doHandTap(transition: TransType, srcHands: TransType, destHands: TransType,
                        oppHandIdx: Byte): ChopsticksState = {
    val ownFingersToAdd: Byte = if (transition._1 == 1) srcHands._1 else srcHands._2
    assert(ownFingersToAdd > 0, "You cannot tap with an inactive hand.")

    val newOppsHands =
      if (oppHandIdx == 1) ((destHands._1 + ownFingersToAdd).toByte, destHands._2)
      else (destHands._1, (destHands._2 + ownFingersToAdd).toByte)

    val (src, newDest) =
      if (newOppsHands._1 >= NUM_FINGERS_PER_HAND) (srcHands, (0.toByte, newOppsHands._2))
      else if (newOppsHands._2 >= NUM_FINGERS_PER_HAND) (srcHands, (newOppsHands._1, 0.toByte))
      else (srcHands, newOppsHands)

    if (isFirstPlayerMove) ChopsticksState(src, newDest, false)
    else ChopsticksState(newDest, src, true)
  }

  /** @return all legal transitions from the current state */
  override def getLegalTransitions: Seq[TransType] = {
    val hands = if (isFirstPlayerMove) firstHands else secondHands
    val sumFingers = hands._1 + hands._2
    val splitTransition = if (hands._1 != hands._2 && sumFingers % 2 == 0) Seq((1.toByte, 0.toByte)) else Seq()
    val firstHandMoves = if (hands._1 != 0) Seq((1.toByte, 1.toByte), (1.toByte, 2.toByte)) else Seq()
    val secondHandMoves = if (hands._2 != 0) Seq((2.toByte, 1.toByte), (2.toByte, 2.toByte)) else Seq()
    splitTransition ++ firstHandMoves ++ secondHandMoves
  }

  /** @return the amount of reward for the transition to this state from the point of view of the first player */
  override def rewardForLastMove: Float =
    if (isWon(true)) 1
    else if (isWon(false)) -1
    else 0

  /** @return the best action to take from the perspective of the current player.
    *  If more than one best, choose randomly from among them.
    */
  override def selectBestAction(actionList: Seq[(TransType, Float)], rnd: Random): (TransType, Float) = {
    //implicit val order: Double.TotalOrdering.type = Ordering.Double.TotalOrdering
    import scala.math.Ordering.Float.TotalOrdering
    val bestValue1 = Seq[Float](0.3f, 0.5f, 0.1f, 0.8f).min

    val bestValue =
      if (isFirstPlayerMove) actionList.map(_._2).max
      else actionList.map(_._2).min
    val bestActions = actionList.filter(_._2 == bestValue)
    bestActions(rnd.nextInt(bestActions.length))
  }
  override def toString: String = s"First player fingers: $firstHands.  Second player fingers: $secondHands"
}
