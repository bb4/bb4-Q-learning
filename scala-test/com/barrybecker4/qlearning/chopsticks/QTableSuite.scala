package com.barrybecker4.qlearning.chopsticks

import ChopsticksAction.{Split, Tap}
import com.barrybecker4.qlearning.TestHelper.strip
import com.barrybecker4.qlearning.common.QTable
import org.scalatest.funsuite.AnyFunSuite
import scala.util.Random


class QTableSuite extends AnyFunSuite {

  private val stateToCheck = ChopsticksState((1, 3), (2, 2))

  private val initialActions =
    "(Tap(2,2),0.0), (Split,0.0), (Tap(1,2),0.0), (Tap(2,1),0.0), (Tap(1,1),0.0)"

  test("QTable init (find number of possible game states)") {
    val qtable = new QTable(ChopsticksState(), None)

    assertResult(strip("""numEntries=948"""))
    { qtable.toString }
  }

  test("getNextMove, 0 episode (should be random)") {
    val qtable = new QTable(ChopsticksState(), None, rnd = new Random(1L))
    val b = stateToCheck
    val actions = qtable.getActions(b)
    assertResult(initialActions) {
      actions.toList.mkString(", ")
    }
    assertResult((Tap(1, 2), 0.0f)) {qtable.getNextAction(b, 0)}
  }

  test("getNextMove, 1 episode (should be random") {
    val qtable = new QTable(ChopsticksState(), None, rnd = new Random(2L))
    val b = stateToCheck
    val actions = qtable.getActions(b)
    assertResult(initialActions) {
      actions.toList.mkString(", ")
    }
    assertResult((Tap(2, 2), 0.0f)) {qtable.getNextAction(b, episodeNumber = 1)}
  }

  test("getNextMove medium episode (randomish)") {
    val qtable = new QTable(ChopsticksState(), None, rnd = new Random(2L))
    val b = stateToCheck
    val actions = qtable.getActions(b)
    assertResult(initialActions) {
      actions.toList.mkString(", ")
    }
    assertResult((Tap(2, 2), 0.0f)) {qtable.getNextAction(b, episodeNumber = 10)}
  }

  test("getNextMove high episode (not random)") {
    val qtable = new QTable(ChopsticksState(), None, rnd = new Random(2L))
    val b = stateToCheck
    val actions = qtable.getActions(b)
    assertResult(initialActions) {
      actions.toList.mkString(", ")
    }
    assertResult((Tap(2, 2), 0.0f)) {qtable.getNextAction(b, 100)}
  }
}
