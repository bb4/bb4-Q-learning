package com.barrybecker4.qlearning.ttt

import com.barrybecker4.qlearning.common.QTable
import org.scalatest.funsuite.AnyFunSuite
import com.barrybecker4.qlearning.TestHelper._

import scala.util.Random

class QTableSuite extends AnyFunSuite {

  test("QTable init") {
    val qtable = new QTable(TTTBoard(), None)

    assertResult(strip("""numEntries=5478"""))
    { qtable.toString }
  }

  test("getNextMove, 0 episode (should be random)") {
    val qtable = new QTable(TTTBoard(), None, rnd = new Random(1L))
    val b = TTTBoard("X...O.X..", 'O')
    val actions = qtable.getActions(b)
    assertResult("(1,0.0), (2,0.0), (3,0.0), (5,0.0), (7,0.0), (8,0.0)") {actions.toList.mkString(", ")}
    assertResult((2, 0.0)) {qtable.getNextAction(b, 0)}
  }

  test("getNextMove, 1 episode (should be random") {
    val qtable = new QTable(TTTBoard(), None, rnd = new Random(2L))
    val b = TTTBoard("X...O.X..", 'O')
    val actions = qtable.getActions(b)
    assertResult("(1,0.0), (2,0.0), (3,0.0), (5,0.0), (7,0.0), (8,0.0)") {actions.toList.mkString(", ")}
    assertResult((3, 0.0)) {qtable.getNextAction(b, episodeNumber = 1)}
  }

  test("getNextMove medium episode (randomish)") {
    val qtable = new QTable(TTTBoard(), None, rnd = new Random(2L))
    val b = TTTBoard("X...O.X..", 'O')
    val actions = qtable.getActions(b)
    assertResult("(1,0.0), (2,0.0), (3,0.0), (5,0.0), (7,0.0), (8,0.0)") {actions.toList.mkString(", ")}
    assertResult((3, 0.0)) {qtable.getNextAction(b, episodeNumber = 10)}
  }

  test("getNextMove high episode (not random)") {
    val qtable = new QTable(TTTBoard(), None, rnd = new Random(2L))
    val b = TTTBoard("X...O.X..", 'O')
    val actions = qtable.getActions(b)
    assertResult("(1,0.0), (2,0.0), (3,0.0), (5,0.0), (7,0.0), (8,0.0)") {actions.toList.mkString(", ")}
    assertResult((3, 0.0)) {qtable.getNextAction(b, 100)}
  }
}
