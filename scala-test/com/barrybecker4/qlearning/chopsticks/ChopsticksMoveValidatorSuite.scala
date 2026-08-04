package com.barrybecker4.qlearning.chopsticks

import org.scalatest.funsuite.AnyFunSuite

/** Characterization tests for [[ChopsticksMoveValidator]]. */
class ChopsticksMoveValidatorSuite extends AnyFunSuite:

  private val start = ChopsticksState()

  test("legal opponent tap is valid") {
    assert(ChopsticksMoveValidator.isValid((1, 1), start, humanGoesFirst = true))
  }

  test("split with even finger sum is valid") {
    val state = ChopsticksState((1, 3), (1, 1))
    assert(ChopsticksMoveValidator.isValid((1, 0), state, humanGoesFirst = true))
  }

  test("split with odd finger sum is invalid") {
    val state = ChopsticksState((1, 2), (1, 1))
    assert(!ChopsticksMoveValidator.isValid((1, 0), state, humanGoesFirst = true))
  }

  test("tapping with inactive hand is invalid") {
    val state = ChopsticksState((0, 2), (1, 1))
    assert(!ChopsticksMoveValidator.isValid((1, 1), state, humanGoesFirst = true))
  }

  test("out of range action is invalid") {
    assert(!ChopsticksMoveValidator.isValid((3, 1), start, humanGoesFirst = true))
  }
