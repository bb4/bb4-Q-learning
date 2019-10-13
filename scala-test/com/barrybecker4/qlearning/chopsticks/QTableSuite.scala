package com.barrybecker4.qlearning.chopsticks

import com.barrybecker4.qlearning.TestHelper.strip
import com.barrybecker4.qlearning.common.QTable
import org.scalatest.FunSuite
import scala.util.Random


class QTableSuite extends FunSuite {

  private val stateToCheck = ChopsticksState((1, 3), (2, 2))

  test("QTable init (find number of possible game states)") {
    val qtable = new QTable(ChopsticksState(), None)

    assertResult(strip("""numEntries=1094"""))
    { qtable.toString }
  }

  test("getNextMove, 0 episode (should be random)") {
    val qtable = new QTable(ChopsticksState(), None, rnd = new Random(1L))
    val b = stateToCheck
    val actions = qtable.getActions(b)
    assertResult("((2,1),0.0), ((1,1),0.0), ((1,2),0.0), ((1,0),0.0), ((2,2),0.0)") {
      actions.toList.mkString(", ")
    }
    assertResult(((1, 2), 0.0)) {qtable.getNextAction(b, 0)}
  }

  test("getNextMove, 1 episode (should be random") {
    val qtable = new QTable(ChopsticksState(), None, rnd = new Random(2L))
    val b = stateToCheck
    val actions = qtable.getActions(b)
    assertResult("((2,1),0.0), ((1,1),0.0), ((1,2),0.0), ((1,0),0.0), ((2,2),0.0)") {
      actions.toList.mkString(", ")
    }
    assertResult(((2, 1), 0)) {qtable.getNextAction(b, episodeNumber = 1)}
  }

  test("getNextMove medium episode (randomish)") {
    val qtable = new QTable(ChopsticksState(), None, rnd = new Random(2L))
    val b = stateToCheck
    val actions = qtable.getActions(b)
    assertResult("((2,1),0.0), ((1,1),0.0), ((1,2),0.0), ((1,0),0.0), ((2,2),0.0)") {
      actions.toList.mkString(", ")
    }
    assertResult(((2, 1), 0.0)) {qtable.getNextAction(b, episodeNumber = 10)}
  }

  test("getNextMove high episode (not random)") {
    val qtable = new QTable(ChopsticksState(), None, rnd = new Random(2L))
    val b = stateToCheck
    val actions = qtable.getActions(b)
    assertResult("((2,1),0.0), ((1,1),0.0), ((1,2),0.0), ((1,0),0.0), ((2,2),0.0)") {
      actions.toList.mkString(", ")
    }
    assertResult(((2, 1), 0.0)) {qtable.getNextAction(b, 100)}
  }
}
