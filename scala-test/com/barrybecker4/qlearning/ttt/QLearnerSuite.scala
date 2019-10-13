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
      """.O.
        |OX.
        |XOX -> HashMap(0 -> 0.999936, 2 -> 0.0, 5 -> 0.0)
        |.X.
        |.XO
        |XO. -> HashMap(0 -> 0.7936, 8 -> 0.76800007, 2 -> 0.0, 3 -> 0.7936)
        |X..
        |O.X
        |... -> HashMap(1 -> 0.0, 2 -> 0.0, 4 -> 0.5734401, 6 -> 0.49152008, 7 -> 0.0, 8 -> 0.0)
        |XOO
        |..X
        |... -> HashMap(3 -> 0.81920004, 4 -> 0.0, 6 -> 0.0, 7 -> 0.0, 8 -> 0.0)
        |.X.
        |X..
        |XOO -> HashMap(0 -> 0.8729395, 2 -> 0.75890684, 4 -> 0.76800007, 5 -> 0.57344)
        |O..
        |XXO
        |X.. -> HashMap(8 -> 0.79872, 1 -> 0.64000005, 2 -> -0.77824, 7 -> 0.64000005)
        |..X
        |OO.
        |..X -> HashMap(0 -> 0.0, 1 -> 0.0, 5 -> 0.992, 6 -> 0.0, 7 -> 0.0)
        |OX.
        |.OX
        |..X -> HashMap(2 -> 0.0, 3 -> 0.64000005, 6 -> 0.64000005, 7 -> 0.76800007)
        |O..
        |XX.
        |.O. -> HashMap(1 -> 0.0, 2 -> 0.0, 5 -> 0.992, 6 -> 0.0, 8 -> 0.0)
        |X.X
        |...
        |..O -> HashMap(1 -> 0.0, 3 -> 0.76800007, 4 -> 0.64000005, 5 -> 0.64000005, 6 -> 0.64000005, 7 -> 0.64000005)""")) {
      qtable.getFirstNEntriesWithNon0Actions(10)
    }
  }

  test("Learning short, epsilon - 0.1") {
    val learner = new QLearner[Int]()
    val qtable = new QTable(TTTBoard(), None, epsilon = 0.1, new Random(0))

    learner.learn(qtable, 20000)

    assertResult(strip(
      """.O.
        |OX.
        |XOX -> HashMap(0 -> 0.992, 2 -> 0.0, 5 -> 0.0)
        |XX.
        |O.O
        |XO. -> HashMap(8 -> 0.0, 2 -> 0.96000004, 4 -> 0.0)
        |X..
        |.O.
        |..X -> HashMap(1 -> 0.0, 2 -> 0.99448276, 3 -> 0.0, 5 -> 0.0, 6 -> 0.92400515, 7 -> 0.0)
        |X..
        |O.X
        |... -> HashMap(1 -> 0.37958455, 2 -> 0.0, 4 -> 0.507904, 6 -> 0.6553601, 7 -> 0.0, 8 -> 0.0)
        |XOO
        |..X
        |... -> HashMap(3 -> 0.7168001, 4 -> 0.0, 6 -> 0.0, 7 -> 0.0, 8 -> -0.40960002)
        |.X.
        |X..
        |XOO -> HashMap(0 -> 0.7772569, 2 -> 0.9472, 4 -> 0.76800007, 5 -> 0.9472)
        |O..
        |XXO
        |X.. -> HashMap(8 -> 0.76800007, 1 -> 0.9472, 2 -> -0.97455305, 7 -> 0.0)
        |OX.
        |.OX
        |..X -> HashMap(2 -> 0.0, 3 -> 0.9216, 6 -> 0.9216, 7 -> 0.7936)
        |O..
        |XX.
        |.O. -> HashMap(1 -> 0.0, 2 -> 0.0, 5 -> 0.9999872, 6 -> 0.0, 8 -> 0.0)
        |X.X
        |...
        |..O -> HashMap(1 -> 0.98661476, 3 -> 0.9904128, 4 -> 0.99705815, 5 -> 0.98815995, 6 -> 0.98815995, 7 -> 0.9970663)""")) {
      qtable.getFirstNEntriesWithNon0Actions(10)
    }
  }

  test("Learning short, epsilon - 0.2") {
    val learner = new QLearner[Int]()
    val qtable = new QTable(TTTBoard(), None, epsilon = 0.2, new Random(0))  // 0.2 is too big

    learner.learn(qtable, 20000)

    assertResult(strip(
      """.O.
        |OX.
        |XOX -> HashMap(0 -> 0.0, 2 -> 0.9999999, 5 -> 0.0)
        |.X.
        |.XO
        |XO. -> HashMap(0 -> 0.79872, 8 -> 0.76800007, 2 -> 0.0, 3 -> 0.0)
        |X..
        |O.X
        |... -> HashMap(1 -> 0.0, 2 -> 0.0, 4 -> 0.40960002, 6 -> 0.817282, 7 -> 0.49152002, 8 -> 0.0)
        |.X.
        |X..
        |XOO -> HashMap(0 -> 0.9241363, 2 -> 0.896, 4 -> 0.76800007, 5 -> 0.76800007)
        |.XX
        |.OO
        |X.. -> HashMap(0 -> 0.0, 8 -> 0.64000005, 3 -> -1.0, 7 -> 0.9980723)
        |OX.
        |.OX
        |..X -> HashMap(2 -> 0.0, 3 -> 0.994179, 6 -> 0.9728, 7 -> 0.76800007)
        |O..
        |XX.
        |.O. -> HashMap(1 -> 0.0, 2 -> 0.0, 5 -> 0.9999872, 6 -> 0.0, 8 -> 0.0)
        |X.X
        |...
        |..O -> HashMap(1 -> 0.0, 3 -> 0.76800007, 4 -> 0.95744, 5 -> 0.896, 6 -> 0.98304, 7 -> 0.76800007)
        |XX.
        |O.O
        |XO. -> HashMap(8 -> -0.64000005, 2 -> 0.992, 4 -> 0.0)
        |.X.
        |O..
        |.XO -> HashMap(0 -> 0.0, 2 -> 0.0, 4 -> 0.9984, 5 -> 0.0, 6 -> 0.0)""")) {
      qtable.getFirstNEntriesWithNon0Actions(10)
    }
  }

  test("Learning (long)") {

    assertResult(strip(
      """.O.
        |OX.
        |XOX -> HashMap(0 -> 0.9999872, 2 -> 0.0, 5 -> 0.0)
        |X..
        |O.X
        |... -> HashMap(1 -> 0.9910875, 2 -> 0.0, 4 -> 0.99999994, 6 -> 0.9974575, 7 -> 0.99605936, 8 -> 0.0)
        |XOO
        |..X
        |... -> HashMap(3 -> 0.99510765, 4 -> 0.0, 6 -> 0.761856, 7 -> 0.0, 8 -> -0.8774451)
        |.X.
        |X..
        |XOO -> HashMap(0 -> 0.9910895, 2 -> 0.99783474, 4 -> 0.99737597, 5 -> 0.99737597)
        |OX.
        |.OX
        |..X -> HashMap(2 -> 0.0, 3 -> 1.0, 6 -> 1.0, 7 -> 1.0)
        |O..
        |XX.
        |.O. -> HashMap(1 -> 0.0, 2 -> 0.95346487, 5 -> 1.0, 6 -> 0.6144, 8 -> 0.0)
        |.XO
        |O..
        |X.. -> HashMap(0 -> -0.8614052, 4 -> 0.0, 5 -> 0.0, 7 -> 0.99932647, 8 -> 0.0)
        |X.X
        |...
        |..O -> HashMap(1 -> 1.0, 3 -> 1.0, 4 -> 1.0, 5 -> 1.0, 6 -> 1.0, 7 -> 1.0)
        |XX.
        |O.O
        |XO. -> HashMap(8 -> 0.0, 2 -> 0.96000004, 4 -> 0.0)
        |.X.
        |O..
        |.XO -> HashMap(0 -> 0.0, 2 -> 0.0, 4 -> 0.96000004, 5 -> 0.0, 6 -> 0.0)""")) {
      qtable.getFirstNEntriesWithNon0Actions(10)
    }

    assertResult(strip("""numEntries=5478""")) {
      qtable.toString
    }
  }

  test("check entries after X played in middle") {
    assertResult("0 -> 0.0, 1 -> 1.0, 2 -> 0.0, 3 -> 1.0, 5 -> 1.0, 6 -> 0.0, 7 -> 1.0, 8 -> 0.0") {
      qtable.getActions(TTTBoard("....X....", 'O')).mkString(", ")
    }
  }

  test("check actions when no win possible") {
    assertResult("2 -> 0.0, 3 -> 0.0, 5 -> 0.0, 6 -> 0.0") {
      qtable.getActions(TTTBoard("XO..X..XO", 'O')).mkString(", ")
    }
  }

  test("check actions when O can win diagonally") {
    assertResult("8 -> 0.7999488, 1 -> 0.0, 5 -> 0.0, 6 -> -0.9999872") {
      qtable.getActions(TTTBoard("X.OXO..X.", 'O')).mkString(", ") // for the diagonal win
    }
  }

  test("check actions when O can win top row horizontally") {
    assertResult("8 -> 0.64000005, 2 -> -1.0, 5 -> 0.0, 6 -> 0.92774403") {
      qtable.getActions(TTTBoard("OO.XX..X.", 'O')).mkString(", ") // for the diagonal win
    }
  }

  test("check actions when X can set up a 2 way win") {
    assertResult("0 -> -0.9472, 2 -> -0.64000005, 4 -> -0.928, 5 -> -0.76800007, 6 -> 0.9886932") {
      qtable.getActions(TTTBoard(".X.X...OO", 'X')).mkString(", ")
    }
  }

  /**   .X.
    *   OXX
    *   .O.
    */
  test("check actions when O can set up a 2 way win") {
    assertResult("0 -> 0.0, 8 -> 0.0, 2 -> 0.0, 6 -> 0.0") {
      qtable.getActions(TTTBoard(".X.OXX.O.", 'O')).mkString(", ")
    }
  }

  /**   .X.
    *   X..
    *   .OO
    */
  test("check actions when X can set up a 2 way win or block") {
    assertResult("0 -> -0.9472, 2 -> -0.64000005, 4 -> -0.928, 5 -> -0.76800007, 6 -> 0.9886932") {
      qtable.getActions(TTTBoard(".X.X...OO", 'X')).mkString(", ")
    }
  }
}
