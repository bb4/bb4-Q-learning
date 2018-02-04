package com.barrybecker4.qlearning.ttt

import org.scalatest.FunSuite
import TestHelper._


class QLearnerSuite extends FunSuite {

  val learner = new QLearner()
  val qtable = new QTable()
  learner.learn(qtable, 200000)

  test("Learning short") {
    val learner = new QLearner()
    val qtable = new QTable()

    learner.learn(qtable, 20000)

    assertResult(strip(
      """OXX
        |X..
        |OOX -> Map(5 -> 0.0, 4 -> 0.79998976)
        |.XO
        |XOO
        |X.X -> Map(7 -> 0.998359, 0 -> 0.9994199)
        |.XX
        |.XO
        |OOX -> Map(3 -> 0.76800007, 0 -> 0.0)
        |..X
        |X..
        |O.. -> Map(8 -> 0.0, 5 -> 0.0, 4 -> 0.0, 7 -> 0.26214403, 1 -> 0.0, 0 -> 0.75038725)
        |OOX
        |XXO
        |.XO -> Map(6 -> 1.0)
        |.O.
        |.X.
        |..X -> Map(2 -> 0.9992192, 5 -> 0.99955505, 7 -> 0.9993011, 3 -> 0.9985708, 6 -> 0.9996288, 0 -> 0.9990435)
        |XOX
        |..O
        |X.. -> Map(8 -> 0.76800007, 4 -> 0.64000005, 7 -> 0.64000005, 3 -> 0.0)
        |O..
        |XX.
        |.O. -> Map(8 -> 0.0, 2 -> 0.0, 5 -> 0.99999744, 1 -> 0.0, 6 -> 0.0)
        |..X
        |.OX
        |O.. -> Map(8 -> 0.99968, 7 -> 0.0, 1 -> 0.0, 3 -> 0.0, 0 -> 0.0)
        |OXO
        |X.O
        |OXX -> Map(4 -> 0.99999946)""")) {
      qtable.getFirstNEntriesWithNon0Actions(10)
    }
  }

  test("Learning (long)") {

    assertResult(strip(
      """OXX
        |X..
        |OOX -> Map(5 -> 0.0, 4 -> 0.64000005)
        |.XO
        |XOO
        |X.X -> Map(7 -> 1.0, 0 -> 1.0)
        |.XX
        |.XO
        |OOX -> Map(3 -> 0.79998976, 0 -> 0.0)
        |..X
        |X..
        |O.. -> Map(8 -> 0.0, 5 -> 0.0, 4 -> 0.0, 7 -> 0.9993486, 1 -> 0.99386805, 0 -> 0.9999991)
        |OOX
        |XXO
        |.XO -> Map(6 -> 1.0)
        |.O.
        |.X.
        |..X -> Map(2 -> 0.92794883, 5 -> 0.9472, 7 -> 0.8981903, 3 -> 0.95360005, 6 -> 0.92774403, 0 -> 0.9537643)
        |XOX
        |..O
        |X.. -> Map(8 -> 1.0, 4 -> 1.0, 7 -> 1.0, 3 -> 1.0)
        |O..
        |XX.
        |.O. -> Map(8 -> 0.0, 2 -> 0.843776, 5 -> 1.0, 1 -> 0.0, 6 -> 0.99773437)
        |..X
        |.OX
        |O.. -> Map(8 -> 1.0, 7 -> 0.0, 1 -> 0.0, 3 -> -0.9996722, 0 -> 0.0)
        |OXO
        |X.O
        |OXX -> Map(4 -> 1.0)""")) {
      qtable.getFirstNEntriesWithNon0Actions(10)
    }

    assertResult(strip("""numEntries=5478""")) {
      qtable.toString
    }
  }

  test("check entries after X played in middle") {
    assertResult("8 -> 0.0, 2 -> 0.0, 5 -> 1.0, 7 -> 1.0, 1 -> 1.0, 3 -> 1.0, 6 -> 0.0, 0 -> 0.0") {
      qtable.getActions(TTTBoard("....X....", 'O')).mkString(", ")
    }
  }

  test("check actions when no win possible") {
    assertResult("2 -> 0.0, 5 -> 0.0, 3 -> 0.0, 6 -> 0.0") {
      qtable.getActions(TTTBoard("XO..X..XO", 'O')).mkString(", ")
    }
  }

  test("check actions when O can win diagonally") {
    assertResult("8 -> 0.0, 5 -> 0.8, 1 -> 0.0, 6 -> -0.99968") {
      qtable.getActions(TTTBoard("X.OXO..X.", 'O')).mkString(", ") // for the diagonal win
    }
  }

  test("check actions when O can win top row horizontally") {
    assertResult("8 -> 0.0, 2 -> -1.0, 5 -> 0.0, 6 -> 0.0") {
      qtable.getActions(TTTBoard("OO.XX..X.", 'O')).mkString(", ") // for the diagonal win
    }
  }

  test("check actions when X can set up a 2 way win") {
    assertResult("2 -> -0.64000005, 5 -> -0.76800007, 4 -> -0.76800007, 6 -> 0.9999578, 0 -> 0.0") {
      qtable.getActions(TTTBoard(".X.X...OO", 'X')).mkString(", ")
    }
  }

  /**   .X.
    *   OXX
    *   .O.
    */
  test("check actions when O can set up a 2 way win") {
    assertResult("8 -> 0.0, 2 -> 0.0, 6 -> -0.9725542, 0 -> 0.0") {
      qtable.getActions(TTTBoard(".X.OXX.O.", 'O')).mkString(", ")
    }
  }

  /** .X.
    * X..
    * .OO
    */
  test("check actions when X can set up a 2 way win or block") {
    assertResult("2 -> -0.64000005, 5 -> -0.76800007, 4 -> -0.76800007, 6 -> 0.9999578, 0 -> 0.0") {
      qtable.getActions(TTTBoard(".X.X...OO", 'X')).mkString(", ")
    }
  }
}
