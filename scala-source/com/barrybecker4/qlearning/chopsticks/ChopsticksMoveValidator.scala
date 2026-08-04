package com.barrybecker4.qlearning.chopsticks

import ChopsticksState.TransType

/** Validates human move input for finger-chopsticks. */
private[chopsticks] object ChopsticksMoveValidator:

  private val validInputs: Set[TransType] =
    (for i <- 1 to 2; j <- 0 to 2 yield (i.toByte, j.toByte)).toSet

  def isValid(action: TransType, state: ChopsticksState, humanGoesFirst: Boolean): Boolean =
    validInputs.contains(action) &&
      validSplitIfSplit(action, state, humanGoesFirst) &&
      !inactiveHandTapped(action, state, humanGoesFirst)

  private def validSplitIfSplit(action: TransType, state: ChopsticksState, humanGoesFirst: Boolean): Boolean =
    val hands = if humanGoesFirst then state.firstHands else state.secondHands
    val v = action._2 != 0 || (hands._1 + hands._2) % 2 == 0
    if !v then println("You can't split if you do not have an even sum of fingers!")
    v

  private def inactiveHandTapped(action: TransType, state: ChopsticksState, humanGoesFirst: Boolean): Boolean =
    if action._2 == 0 then return false // self tap of inactive hand is ok to do split
    val (h1, h2) =
      if humanGoesFirst then (state.firstHands, state.secondHands)
      else (state.secondHands, state.firstHands)
    val inactiveTapped = action match
      case (1, 1) => h1._1 == 0 || h2._1 == 0
      case (2, 1) => h1._2 == 0 || h2._1 == 0
      case (1, 2) => h1._1 == 0 || h2._2 == 0
      case (2, 2) => h1._2 == 0 || h2._2 == 0
      case _ => false
    if inactiveTapped then println("An inactive hand cannot tap or be tapped")
    inactiveTapped
