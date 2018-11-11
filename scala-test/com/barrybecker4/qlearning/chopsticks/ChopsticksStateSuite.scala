package com.barrybecker4.qlearning.chopsticks

import org.scalatest.FunSuite
import scala.util.Random


class ChopsticksStateSuite extends FunSuite {

  test("construct board and serialize") {
    val b = ChopsticksState((1, 2), (3, 3))
    assertResult("Player to move: (1,2). Opposing player: (3,3)") {b.toString}
  }

  test("is won when firstPlayer won") {
    val b = ChopsticksState((0, 0), (4, 4))
    assertResult(true) {b.isWon}
    assertResult(false) {b.isLost}
  }

  test("is won when opponent won") {
    val b = ChopsticksState((4, 4), (0, 0))
    assertResult(true) {b.isLost}
    assertResult(false) {b.isWon}
  }

  test("make move 11") {
    val b = ChopsticksState((1, 1), (1, 2))
    assertResult("Player to move: (2,2). Opposing player: (1,1)") {b.makeTransition((1, 1)).toString}
  }

  test("make move 22 which diables a hand") {
    val b = ChopsticksState((1, 3), (1, 2))
    assertResult("Player to move: (1,0). Opposing player: (1,3)") {b.makeTransition((2, 2)).toString}
  }

  test("make move 21 which wins") {
    var b = ChopsticksState((0, 4), (1, 0))
    b = b.makeTransition((2, 1))
    assertResult(true) {b.isWon}
  }

  test("make split move when 0 4") {
    var b = ChopsticksState((0, 4), (1, 0))
    assertResult("Player to move: (1,0). Opposing player: (2,2)") {b.makeTransition((2, 0)).toString}
  }

  test("make split move when 3 1") {
    var b = ChopsticksState((3, 1), (1, 0))
    assertResult("Player to move: (1,0). Opposing player: (2,2)") {b.makeTransition((2, 0)).toString}
  }

  test("make split move when 3 2 (not allowed)") {
    var b = ChopsticksState((3, 2), (1, 0))
    assertThrows[AssertionError]{b.makeTransition((2, 0))}
  }

  test("hasMoves when some") {
    var b = ChopsticksState((2, 1), (0, 1))
    assertResult(true) {b.hasTransitions}
  }

  test("hasMoves when none") {
    var b = ChopsticksState((0, 0), (1, 1))
    assertResult(false) {b.hasTransitions}
  }

  test("getReward when not won") {
    var b = ChopsticksState((2, 3), (3, 2))
    assertResult(0f) {b.rewardForLastMove}
    b.makeTransition((2, 1))
    assertResult(0f) {b.rewardForLastMove}
  }

  test("getReward when player 1 won") {
    var b = ChopsticksState((1, 0), (0, 4))
    assertResult(0f) {b.rewardForLastMove}
    b = b.makeTransition((1, 2))
    assertResult(1.0f) {b.rewardForLastMove}
  }

  test("getReward when won") {
    var b = ChopsticksState((0, 0), (1, 1))
    assertResult(1.0f) {b.rewardForLastMove}
  }

  test("select best move for player") {
    val b = ChopsticksState((0, 1), (4, 0))
    val actionsList: Seq[((Byte, Byte), Float)] = Seq(((1, 1), 0.1f), ((1, 2), 0.6f))
    val best = b.selectBestAction(actionsList, new Random(1))
    assertResult(((1, 2), 0.6f)) { best }
  }

  test("played already when played") {
    var b = ChopsticksState((1, 0), (0, 4))
    assertThrows[AssertionError] {b.makeTransition((2, 2))}
  }
}