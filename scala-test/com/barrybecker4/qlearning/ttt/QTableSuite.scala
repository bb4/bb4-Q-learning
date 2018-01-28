package com.barrybecker4.qlearning.ttt

import org.scalatest.FunSuite
import com.barrybecker4.qlearning.ttt.TestHelper._


class QTableSuite extends FunSuite {

  test("QTable init") {
    val qtable = new QTable
    assertResult(strip("""numEntries=5478 first 10 entries:
       |XOO
       |.XO
       |XX. -> Map(3 -> 0.0, 8 -> 0.0)
       |OO.
       |..X
       |XOX -> Map(2 -> 0.0, 3 -> 0.0, 4 -> 0.0)
       |O..
       |.O.
       |X.X -> Map(5 -> 0.0, 1 -> 0.0, 2 -> 0.0, 7 -> 0.0, 3 -> 0.0)
       |OOX
       |XXX
       |OOX -> Map()
       |.X.
       |O.X
       |O.X -> Map(0 -> 0.0, 2 -> 0.0, 4 -> 0.0, 7 -> 0.0)
       |X..
       |OXX
       |.O. -> Map(1 -> 0.0, 2 -> 0.0, 6 -> 0.0, 8 -> 0.0)
       |OXO
       |O.X
       |.X. -> Map(4 -> 0.0, 6 -> 0.0, 8 -> 0.0)
       |XXO
       |..O
       |..X -> Map(3 -> 0.0, 4 -> 0.0, 6 -> 0.0, 7 -> 0.0)
       |O.X
       |OXX
       |O.. -> Map()
       |X.X
       |.OO
       |.XO -> Map(1 -> 0.0, 3 -> 0.0, 6 -> 0.0)"""))
    { qtable.toString }
  }
}
