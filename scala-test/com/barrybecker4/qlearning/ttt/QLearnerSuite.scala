package com.barrybecker4.qlearning.ttt

import org.scalatest.FunSuite
import TestHelper._


class QLearnerSuite extends FunSuite {

  test("Learning short") {
    val learner  = new QLearner()
    val qtable = new QTable()

    learner.learn(qtable, 20000)

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
         |.XO -> Map(1 -> 0.0, 3 -> 0.0, 6 -> 0.0)""")) {
      qtable.toString
    }
  }

  test("Learning (long)") {
    val learner  = new QLearner()
    val qtable = new QTable()

    learner.learn(qtable, 200000)

    assertResult(strip("""numEntries=5478 first 10 entries:
       |XOO
       |.XO
       |XX. -> Map(8 -> 0.0, 3 -> 0.0)
       |OO.
       |..X
       |XOX -> Map(2 -> 0.8, 4 -> 0.0, 3 -> 0.0)
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
       |.XO -> Map(1 -> 0.0, 3 -> 0.0, 6 -> 0.0)""")) {
      qtable.toString
    }
  }

  test("Learning (very long)") {
    val learner  = new QLearner()
    val qtable = new QTable()

    learner.learn(qtable, 2000000)

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
       |.XO -> Map(1 -> 0.0, 3 -> 0.0, 6 -> 0.0)""")) {
      qtable.toString
    }
  }

}
