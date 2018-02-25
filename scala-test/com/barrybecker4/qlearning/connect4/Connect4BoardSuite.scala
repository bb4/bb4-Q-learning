package com.barrybecker4.qlearning.connect4

import com.barrybecker4.qlearning.connect4.Connect4Board
import org.scalatest.FunSuite
import com.barrybecker4.qlearning.TestHelper.strip


class Connect4BoardSuite extends FunSuite {

  test("construct board and serialize") {
    val b = Connect4Board("XXOOX.O.OXOX...O")
    assertResult(strip("""1234567
         |.......
         |.......
         |.......
         |.O.....
         |.OXOX..
         |XXOOX.O
         |""")) {b.toString}
  }
}
