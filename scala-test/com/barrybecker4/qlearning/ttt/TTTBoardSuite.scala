package com.barrybecker4.qlearning.ttt

import org.scalatest.FunSuite


class TTTBoardSuite extends FunSuite {



  test("construct board and serialize") {
    val b = new TTTBoard("X......O.")
    assertResult("X..\n...\n.O.") {b.toString}
  }
}