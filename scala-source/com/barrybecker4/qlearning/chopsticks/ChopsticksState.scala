package com.barrybecker4.qlearning.chopsticks

import ChopsticksAction.{Split, Tap}
import com.barrybecker4.qlearning.chopsticks.ChopsticksState.DONE_STATE
import com.barrybecker4.qlearning.common.{ActionSelection, State}

import scala.util.Random


object ChopsticksState:

  /** If one player reaches this state (no fingers on either hand), then the other playe has won */
  private val DONE_STATE = (0, 0)

  /** You can try learning with other than 5 fingers per hand if we want */
  private val NUM_FINGERS_PER_HAND = 5

/**
  * Always from the point of view of the player moving next
  */
case class ChopsticksState(firstHands: (Byte, Byte) = (1, 1),
                           secondHands: (Byte, Byte) = (1, 1),
                           isFirstPlayerMove: Boolean = true) extends State[ChopsticksAction]:

  def isWon(player1: Boolean): Boolean =
    if player1 then secondHands == DONE_STATE else firstHands == DONE_STATE

  /** @return true if at least one valid transition from this state */
  override def hasTransitions: Boolean =
    firstHands != DONE_STATE && secondHands != DONE_STATE

  override def makeTransition(action: ChopsticksAction): ChopsticksState =
    action match
      case Split => doSelfSplit()
      case Tap(ownHand, oppHand) => tapOpponent(ownHand, oppHand)

  private def doSelfSplit(): ChopsticksState =
    if isFirstPlayerMove then ChopsticksState(doHandSplit(firstHands), secondHands, !isFirstPlayerMove)
    else ChopsticksState(firstHands, doHandSplit(secondHands), !isFirstPlayerMove)

  private def doHandSplit(hands: (Byte, Byte)): (Byte, Byte) =
    val sum = hands._1 + hands._2
    assert(sum % 2 == 0, "Did not have an even sum of fingers!")
    val selfAvgFingers = (sum / 2).toByte
    (selfAvgFingers, selfAvgFingers)

  private def tapOpponent(ownHand: Byte, oppHandIdx: Byte): ChopsticksState =
    if isFirstPlayerMove then doHandTap(ownHand, firstHands, secondHands, oppHandIdx)
    else doHandTap(ownHand, secondHands, firstHands, oppHandIdx)

  private def doHandTap(ownHand: Byte, srcHands: (Byte, Byte), destHands: (Byte, Byte),
                        oppHandIdx: Byte): ChopsticksState =
    val ownFingersToAdd: Byte = if ownHand == 1 then srcHands._1 else srcHands._2
    assert(ownFingersToAdd > 0, "You cannot tap with an inactive hand.")

    val destFingers = if oppHandIdx == 1 then destHands._1 else destHands._2
    assert(destFingers > 0, "An inactive hand cannot be tapped.")

    val newOppsHands =
      if oppHandIdx == 1 then ((destHands._1 + ownFingersToAdd).toByte, destHands._2)
      else (destHands._1, (destHands._2 + ownFingersToAdd).toByte)

    val (src, newDest) =
      if newOppsHands._1 >= ChopsticksState.NUM_FINGERS_PER_HAND then (srcHands, (0.toByte, newOppsHands._2))
      else if newOppsHands._2 >= ChopsticksState.NUM_FINGERS_PER_HAND then (srcHands, (newOppsHands._1, 0.toByte))
      else (srcHands, newOppsHands)

    if isFirstPlayerMove then ChopsticksState(src, newDest, false)
    else ChopsticksState(newDest, src, true)

  override def getLegalTransitions: Seq[ChopsticksAction] =
    val hands = if isFirstPlayerMove then firstHands else secondHands
    val oppHands = if isFirstPlayerMove then secondHands else firstHands
    val sumFingers = hands._1 + hands._2
    val splitTransition =
      if hands._1 != hands._2 && sumFingers % 2 == 0 then Seq(Split) else Seq.empty
    val firstHandMoves =
      if hands._1 != 0 then
        Seq(Tap(1, 1)).filter(_ => oppHands._1 != 0) ++ Seq(Tap(1, 2)).filter(_ => oppHands._2 != 0)
      else Seq.empty
    val secondHandMoves =
      if hands._2 != 0 then
        Seq(Tap(2, 1)).filter(_ => oppHands._1 != 0) ++ Seq(Tap(2, 2)).filter(_ => oppHands._2 != 0)
      else Seq.empty
    splitTransition ++ firstHandMoves ++ secondHandMoves

  override def rewardForLastMove: Float =
    if isWon(true) then 1
    else if isWon(false) then -1
    else 0

  override def selectBestAction(actionList: Seq[(ChopsticksAction, Float)], rnd: Random): (ChopsticksAction, Float) =
    ActionSelection.pickBest(actionList, maximize = isFirstPlayerMove, rnd)

  override def toString: String = s"First player fingers: $firstHands.  Second player fingers: $secondHands"
