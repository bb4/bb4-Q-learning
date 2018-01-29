package com.barrybecker4.qlearning.ttt


import org.scalatest.FunSuite
import com.barrybecker4.qlearning.ttt.TestHelper._

import scala.util.Random


class QTableSuite extends FunSuite {

  test("QTable init") {
    val qtable = new QTable

    assertResult(strip("""numEntries=5478 first 10 entries:
         |XOO
         |.XO
         |XX. -> Map(8 -> 0.0, 3 -> 0.0)
         |OO.
         |..X
         |XOX -> Map(2 -> 0.0, 4 -> 0.0, 3 -> 0.0)
         |O..
         |.O.
         |X.X -> Map(2 -> 0.0, 5 -> 0.0, 7 -> 0.0, 1 -> 0.0, 3 -> 0.0)
         |OOX
         |XXX
         |OOX -> Map()
         |.X.
         |O.X
         |O.X -> Map(2 -> 0.0, 4 -> 0.0, 7 -> 0.0, 0 -> 0.0)
         |X..
         |OXX
         |.O. -> Map(8 -> 0.0, 2 -> 0.0, 1 -> 0.0, 6 -> 0.0)
         |OXO
         |O.X
         |.X. -> Map(8 -> 0.0, 4 -> 0.0, 6 -> 0.0)
         |XXO
         |..O
         |..X -> Map(4 -> 0.0, 7 -> 0.0, 3 -> 0.0, 6 -> 0.0)
         |O.X
         |OXX
         |O.. -> Map()
         |X.X
         |.OO
         |.XO -> Map(1 -> 0.0, 3 -> 0.0, 6 -> 0.0)"""))
    { qtable.toString }
  }

  test("getNextMove, 0 episode (should be random") {
    val qtable = new QTable(new Random(1L))
    val b = TTTBoard("X...O.X..")
    val actions = qtable.getActions(b)
    assertResult("(8,0.0), (2,0.0), (5,0.0), (7,0.0), (1,0.0), (3,0.0)") {actions.toList.mkString(", ")}

    //getNextMove(b: TTTBoard, episodeNumber: Int)
    assertResult((2, 0.0)) {qtable.getNextAction(b, 0)}
  }

  test("getNextMove, 1 episode (should be random") {
    val qtable = new QTable(new Random(2L))
    val b = TTTBoard("X...O.X..")
    val actions = qtable.getActions(b)
    assertResult("(8,0.0), (2,0.0), (5,0.0), (7,0.0), (1,0.0), (3,0.0)") {actions.toList.mkString(", ")}

    //getNextMove(b: TTTBoard, episodeNumber: Int)
    assertResult((8, 0)) {qtable.getNextAction(b, 1)}
  }

  test("getNextMove medium episode (randomish)") {
    val qtable = new QTable(new Random(2L))
    val b = TTTBoard("X...O.X..")
    val actions = qtable.getActions(b)
    assertResult("(8,0.0), (2,0.0), (5,0.0), (7,0.0), (1,0.0), (3,0.0)") {actions.toList.mkString(", ")}

    //getNextMove(b: TTTBoard, episodeNumber: Int)
    assertResult((8, 0.0)) {qtable.getNextAction(b, 10)}
  }

  test("getNextMove high episode (not random)") {
    val qtable = new QTable(new Random(2L))
    val b = TTTBoard("X...O.X..")
    val actions = qtable.getActions(b)
    assertResult("(8,0.0), (2,0.0), (5,0.0), (7,0.0), (1,0.0), (3,0.0)") {actions.toList.mkString(", ")}

    //getNextMove(b: TTTBoard, episodeNumber: Int)
    assertResult((8, 0.0)) {qtable.getNextAction(b, 100)}
  }
}
