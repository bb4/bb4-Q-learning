package com.barrybecker4.qlearning.ttt

import org.scalatest.FunSuite
import com.barrybecker4.qlearning.TestHelper._
import com.barrybecker4.qlearning.common.{QLearner, QTable}

import scala.util.Random


class QLearnerSuite extends FunSuite {

  val learner = new QLearner[Int]()
  val qtable = new QTable(TTTBoard(), None, epsilon = 0.1, new Random(0))
  learner.learn(qtable, 200000)

  test("Learning short, epsilon - 0.01") {
    val learner = new QLearner[Int]()
    val qtable = new QTable(TTTBoard(), None, epsilon = 0.01, new Random(0))    // 0.01 likely too small

    learner.learn(qtable, 20000)

    assertResult(strip(
      """X.X
        |O.O
        |.X. -> Map(8 -> 0.64000005, 4 -> 0.0, 1 -> 0.78412515, 6 -> 0.0)
        |OXX
        |X..
        |OOX -> Map(5 -> 0.0, 4 -> 0.7936)
        |.XO
        |XOO
        |X.X -> Map(7 -> 0.9980313, 0 -> 0.9994076)
        |.XX
        |.XO
        |OOX -> Map(3 -> 0.9854976, 0 -> 0.0)
        |..X
        |X..
        |O.. -> Map(8 -> 0.0, 5 -> 0.0, 4 -> 0.0, 7 -> 0.31457287, 1 -> 0.0, 0 -> 0.40960002)
        |OOX
        |XXO
        |.XO -> Map(6 -> 1.0)
        |.O.
        |.X.
        |..X -> Map(2 -> 0.9216, 5 -> 0.76800007, 7 -> 0.9216, 3 -> 0.92672, 6 -> 0.9216, 0 -> 0.68812805)
        |XOX
        |..O
        |X.. -> Map(8 -> 0.79872, 4 -> 0.0, 7 -> 0.64000005, 3 -> 0.0)
        |O..
        |XX.
        |.O. -> Map(8 -> 0.0, 2 -> 0.0, 5 -> 0.9984, 1 -> 0.0, 6 -> 0.0)
        |..X
        |.OX
        |O.. -> Map(8 -> 0.9984, 7 -> 0.0, 1 -> 0.0, 3 -> 0.0, 0 -> 0.0)""")) {
      qtable.getFirstNEntriesWithNon0Actions(10)
    }
  }

  test("Learning short, epsilon - 0.1") {
    val learner = new QLearner[Int]()
    val qtable = new QTable(TTTBoard(), None, epsilon = 0.1, new Random(0))

    learner.learn(qtable, 20000)

    assertResult(strip(
      """OXX
        |X..
        |OOX -> Map(5 -> 0.0, 4 -> 0.64000005)
        |.XO
        |XOO
        |X.X -> Map(7 -> 0.99708927, 0 -> 0.9971076)
        |..X
        |X..
        |O.. -> Map(8 -> 0.0, 5 -> 0.0, 4 -> 0.0, 7 -> 0.6048973, 1 -> 0.40960002, 0 -> 0.40960002)
        |OOX
        |XXO
        |.XO -> Map(6 -> 1.0)
        |.O.
        |.X.
        |..X -> Map(2 -> 0.64000005, 5 -> 0.76800007, 7 -> 0.49152002, 3 -> 0.76800007, 6 -> 0.64000005, 0 -> 0.5734401)
        |XOX
        |..O
        |X.. -> Map(8 -> 0.99889153, 4 -> 0.99676156, 7 -> 0.9981952, 3 -> 0.99737597)
        |O..
        |XX.
        |.O. -> Map(8 -> 0.0, 2 -> 0.512, 5 -> 0.999936, 1 -> 0.0, 6 -> 0.71680003)
        |..X
        |.OX
        |O.. -> Map(8 -> 0.99999946, 7 -> 0.0, 1 -> 0.0, 3 -> 0.0, 0 -> 0.0)
        |OXO
        |X.O
        |OXX -> Map(4 -> 0.9999872)
        |XO.
        |OXO
        |XX. -> Map(8 -> 0.7936, 2 -> 0.896)""")) {
      qtable.getFirstNEntriesWithNon0Actions(10)
    }
  }

  test("Learning short, epsilon - 0.2") {
    val learner = new QLearner[Int]()
    val qtable = new QTable(TTTBoard(), None, epsilon = 0.2, new Random(0))  // 0.2 is too big

    learner.learn(qtable, 20000)

    assertResult(strip(
      """X.X
        |O.O
        |.X. -> Map(8 -> 0.0, 4 -> -0.8, 1 -> 0.6101402, 6 -> 0.76800007)
        |OXX
        |X..
        |OOX -> Map(5 -> 0.0, 4 -> 0.9971188)
        |.XO
        |XOO
        |X.X -> Map(7 -> 0.99778557, 0 -> 0.99808246)
        |.XX
        |.XO
        |OOX -> Map(3 -> 0.64000005, 0 -> 0.0)
        |..X
        |X..
        |O.. -> Map(8 -> 0.0, 5 -> 0.0, 4 -> 0.0, 7 -> 0.88515544, 1 -> 0.0, 0 -> 0.0)
        |OOX
        |XXO
        |.XO -> Map(6 -> 1.0)
        |.O.
        |.X.
        |..X -> Map(2 -> 0.76800007, 5 -> 0.64000005, 7 -> 0.64000005, 3 -> 0.896, 6 -> 0.49152008, 0 -> 0.0)
        |XOX
        |..O
        |X.. -> Map(8 -> 0.99962634, 4 -> 0.9990144, 7 -> 0.9984, 3 -> 0.9996452)
        |O..
        |XX.
        |.O. -> Map(8 -> 0.0, 2 -> 0.0, 5 -> 0.99999744, 1 -> 0.0, 6 -> 0.0)
        |..X
        |.OX
        |O.. -> Map(8 -> 1.0, 7 -> 0.0, 1 -> 0.0, 3 -> 0.0, 0 -> 0.0)""")) {
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

  /**   .X.
    *   X..
    *   .OO
    */
  test("check actions when X can set up a 2 way win or block") {
    assertResult("2 -> -0.64000005, 5 -> -0.76800007, 4 -> -0.76800007, 6 -> 0.9999578, 0 -> 0.0") {
      qtable.getActions(TTTBoard(".X.X...OO", 'X')).mkString(", ")
    }
  }
}
