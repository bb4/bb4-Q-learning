package com.barrybecker4.qlearning.ttt

import org.scalatest.FunSuite
import com.barrybecker4.qlearning.ttt.TestHelper._
import scala.util.Random

class QTableSuite extends FunSuite {

  test("QTable init") {
    val qtable = new QTable

    assertResult(strip("""numEntries=5478 first 10 entries:
         |X.X
         |O.O
         |.X. -> Map(8 -> 0.0, 4 -> 0.0, 1 -> 0.0, 6 -> 0.0)
         |XXO
         |.OO
         |.X. -> Map(8 -> 0.0, 3 -> 0.0, 6 -> 0.0)
         |OXX
         |X..
         |OOX -> Map(5 -> 0.0, 4 -> 0.0)
         |.XO
         |XOO
         |X.X -> Map(7 -> 0.0, 0 -> 0.0)
         |XO.
         |.XX
         |OOX -> Map()
         |.XX
         |.XO
         |OOX -> Map(3 -> 0.0, 0 -> 0.0)
         |.X.
         |XXO
         |..O -> Map(2 -> 0.0, 7 -> 0.0, 6 -> 0.0, 0 -> 0.0)
         |..X
         |X..
         |O.. -> Map(8 -> 0.0, 5 -> 0.0, 4 -> 0.0, 7 -> 0.0, 1 -> 0.0, 0 -> 0.0)
         |X.X
         |..X
         |O.O -> Map(4 -> 0.0, 7 -> 0.0, 1 -> 0.0, 3 -> 0.0)
         |O.X
         |XXX
         |OO. -> Map()"""))
    { qtable.toString }
  }

  test("getNextMove, 0 episode (should be random)") {
    val qtable = new QTable(new Random(1L))
    val b = TTTBoard("X...O.X..", 'O')
    val actions = qtable.getActions(b)
    assertResult("(8,0.0), (2,0.0), (5,0.0), (7,0.0), (1,0.0), (3,0.0)") {actions.toList.mkString(", ")}

    //getNextMove(b: TTTBoard, episodeNumber: Int)
    assertResult((2, 0.0)) {qtable.getNextAction(b, 0)}
  }

  test("getNextMove, 1 episode (should be random") {
    val qtable = new QTable(new Random(2L))
    val b = TTTBoard("X...O.X..", 'O')
    val actions = qtable.getActions(b)
    assertResult("(8,0.0), (2,0.0), (5,0.0), (7,0.0), (1,0.0), (3,0.0)") {actions.toList.mkString(", ")}

    //getNextMove(b: TTTBoard, episodeNumber: Int)
    assertResult((8, 0)) {qtable.getNextAction(b, 1)}
  }

  test("getNextMove medium episode (randomish)") {
    val qtable = new QTable(new Random(2L))
    val b = TTTBoard("X...O.X..", 'O')
    val actions = qtable.getActions(b)
    assertResult("(8,0.0), (2,0.0), (5,0.0), (7,0.0), (1,0.0), (3,0.0)") {actions.toList.mkString(", ")}

    //getNextMove(b: TTTBoard, episodeNumber: Int)
    assertResult((8, 0.0)) {qtable.getNextAction(b, 10)}
  }

  test("getNextMove high episode (not random)") {
    val qtable = new QTable(new Random(2L))
    val b = TTTBoard("X...O.X..", 'O')
    val actions = qtable.getActions(b)
    assertResult("(8,0.0), (2,0.0), (5,0.0), (7,0.0), (1,0.0), (3,0.0)") {actions.toList.mkString(", ")}

    //getNextMove(b: TTTBoard, episodeNumber: Int)
    assertResult((8, 0.0)) {qtable.getNextAction(b, 100)}
  }
}
