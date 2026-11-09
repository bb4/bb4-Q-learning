package com.barrybecker4.qlearning.chopsticks

import ChopsticksAction.{Split, Tap}

/** Validates human move input for finger-chopsticks. */
private[chopsticks] object ChopsticksMoveValidator:

  def isValid(action: ChopsticksAction, state: ChopsticksState, humanGoesFirst: Boolean): Boolean =
    action match
      case Split => validSplit(state, humanGoesFirst)
      case tap: Tap => validTap(tap, state, humanGoesFirst)

  private def validSplit(state: ChopsticksState, humanGoesFirst: Boolean): Boolean =
    val hands = if humanGoesFirst then state.firstHands else state.secondHands
    val v = (hands._1 + hands._2) % 2 == 0 && hands._1 != hands._2
    if !v then println("You can't split if you do not have an even sum of fingers!")
    v

  private def validTap(tap: Tap, state: ChopsticksState, humanGoesFirst: Boolean): Boolean =
    val (h1, h2) =
      if humanGoesFirst then (state.firstHands, state.secondHands)
      else (state.secondHands, state.firstHands)
    val ownFingers = if tap.ownHand == 1 then h1._1 else h1._2
    val oppFingers = if tap.opponentHand == 1 then h2._1 else h2._2
    val inactive = ownFingers == 0 || oppFingers == 0
    if inactive then println("An inactive hand cannot tap or be tapped")
    !inactive && tap.ownHand >= 1 && tap.ownHand <= 2 && tap.opponentHand >= 1 && tap.opponentHand <= 2
