package com.barrybecker4.qlearning.tttminimax

import com.barrybecker4.qlearning.tttminimax.BoardState.Player.HUMAN
import org.scalatest.funsuite.AnyFunSuite

class TicTacToeSuite extends AnyFunSuite {

  test("computerMove returns legal position after human opens center") {
    val s0 = new BoardState(HUMAN)
    val afterHuman = s0.makeHumanMove(4)
    assert(afterHuman.player == BoardState.Player.COMPUTER)
    val afterComputer = TicTacToe.computerMove(afterHuman)
    assert(afterComputer.board(4) == Some('X'))
    assert(afterComputer.getAvailableMoves.length == 7)
  }

  test("computerMove after human opens corner places O") {
    val s0 = new BoardState(HUMAN)
    val afterHuman = s0.makeHumanMove(0)
    val afterComputer = TicTacToe.computerMove(afterHuman)
    assert(afterComputer.board(0) == Some('X'))
    assert(afterComputer.board.count(_.isDefined) == 2)
  }
}
