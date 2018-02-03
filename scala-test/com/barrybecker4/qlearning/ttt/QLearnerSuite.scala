package com.barrybecker4.qlearning.ttt

import org.scalatest.FunSuite
import TestHelper._


class QLearnerSuite extends FunSuite {

  test("Learning short") {
    val learner  = new QLearner()
    val qtable = new QTable()

    learner.learn(qtable, 20000)

    assertResult(strip(
      """.OX
       |...
       |XOX -> Map(5 -> 0.0, 4 -> 0.8, 3 -> 0.0, 0 -> 0.0), .XO
       |XOX
       |.XO -> Map(6 -> 0.8, 0 -> 0.0), XX.
       |..O
       |.XO -> Map(2 -> 0.8, 4 -> 0.0, 3 -> 0.0, 6 -> 0.0), ..X
       |...
       |X.O -> Map(5 -> 0.0, 4 -> 0.512, 7 -> 0.0, 1 -> 0.0, 3 -> 0.0, 0 -> 0.0), X.X
       |O.O
       |..X -> Map(4 -> 0.8, 7 -> 0.0, 1 -> 0.0, 6 -> 0.0), ..X
       |.OO
       |..X -> Map(7 -> 0.0, 1 -> 0.76800007, 3 -> 0.0, 6 -> 0.0, 0 -> 0.0), .X.
       |XOO
       |XXO -> Map(2 -> 0.8, 0 -> 0.0), XOO
       |.OX
       |X.X -> Map(7 -> 0.8, 3 -> 0.0), ..O
       |..X
       |XOX -> Map(4 -> 0.0, 1 -> 0.512, 3 -> 0.0, 0 -> 0.0), X.O
       |XOX
       |.OX -> Map(1 -> 0.96000004, 6 -> 0.0)""")) {
      qtable.getFirstNEntriesWithNon0Actions(10)
    }

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
       |OO. -> Map()""")) {
      qtable.toString
    }


  }

  test("Learning (long)") {
    val learner  = new QLearner()
    val qtable = new QTable()

    learner.learn(qtable, 200000)

    assertResult(strip(
      """.XO
        |XOX
        |.XO -> Map(6 -> 0.8, 0 -> 0.0), OXO
        |..X
        |XXO -> Map(4 -> 0.8, 3 -> 0.0), X.O
        |XOX
        |.OX -> Map(1 -> 0.8, 6 -> 0.0), .XX
        |OO.
        |OXX -> Map(5 -> 0.8, 0 -> 0.0), ..O
        |OXX
        |OXX -> Map(1 -> 0.0, 0 -> 0.8), ...
        |.X.
        |... -> Map(8 -> 1.0, 2 -> 0.0, 5 -> 0.0, 7 -> 0.0, 1 -> 0.0, 3 -> 0.0, 6 -> 0.0, 0 -> 0.0), ...
        |...
        |... -> Map(8 -> 0.0, 2 -> 0.0, 5 -> 0.0, 4 -> 1.0, 7 -> 0.0, 1 -> 0.0, 3 -> 0.0, 6 -> 0.0, 0 -> 0.0), O.X
        |..X
        |X.O -> Map(4 -> 0.8, 7 -> 0.0, 1 -> 0.0, 3 -> 0.0), OXO
        |.OX
        |XX. -> Map(8 -> 0.8, 3 -> 0.0), .XX
        |OXX
        |.OO -> Map(6 -> 0.992, 0 -> 0.0)""")) {
      qtable.getFirstNEntriesWithNon0Actions(10)
    }


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
           |OO. -> Map()""")) {
      qtable.toString
    }
  }

  test("Learning (very long)") {
    val learner  = new QLearner()
    val qtable = new QTable()

    learner.learn(qtable, 2000000)

    assertResult(strip(
      """.X.
         |OOX
         |O.X -> Map(2 -> 0.0, 7 -> 0.64000005, 0 -> 0.0), O.O
         |OXX
         |.XX -> Map(1 -> 0.0, 6 -> 0.8), OXX
         |.X.
         |O.O -> Map(5 -> 0.64000005, 7 -> 0.0, 3 -> 0.0), OXX
         |OXO
         |..X -> Map(7 -> 0.0, 6 -> 0.8), OOX
         |XO.
         |X.X -> Map(5 -> 0.0, 7 -> 0.8), X.O
         |XOX
         |.OX -> Map(1 -> 0.8, 6 -> 0.0), .OO
         |XX.
         |XXO -> Map(5 -> 0.96000004, 0 -> 0.0), .XX
         |OO.
         |OXX -> Map(5 -> 0.8, 0 -> 0.0), X.O
         |XOX
         |... -> Map(8 -> 0.0, 7 -> 0.0, 1 -> 0.512, 6 -> 0.0), OXX
         |.XX
         |O.O -> Map(7 -> 0.992, 3 -> 0.0)""")) {
      qtable.getFirstNEntriesWithNon0Actions(10)
    }

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
         |OO. -> Map()""")) {
      qtable.toString
    }
  }

}
