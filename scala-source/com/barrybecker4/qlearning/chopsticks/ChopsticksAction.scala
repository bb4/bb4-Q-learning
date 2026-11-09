package com.barrybecker4.qlearning.chopsticks

/** Legal finger-chopsticks moves (tap opponent hand or split own fingers). */
enum ChopsticksAction:
  case Tap(ownHand: Byte, opponentHand: Byte)
  case Split

object ChopsticksAction:

  def fromInputDigits(ownHand: Byte, destHand: Byte): ChopsticksAction =
    if destHand == 0 then Split else Tap(ownHand, destHand)

  /** Decode legacy two-digit human input (first = own hand, second = tapped hand or 0 for split). */
  def fromInput(input: Int): ChopsticksAction =
    fromInputDigits((input / 10).toByte, (input % 10).toByte)
