package com.barrybecker4.qlearning.tttminimax

import com.barrybecker4.qlearning.tttminimax.BoardState.Player.{COMPUTER, HUMAN}
import org.scalatest.funsuite.AnyFunSuite

class BoardStateSuite extends AnyFunSuite {

  test("empty board has nine moves and game not over") {
    val s = new BoardState(HUMAN)
    assert(s.getAvailableMoves.length == 9)
    assert(!s.isGameOver)
    assert(s.getScore == 0)
  }

  test("detect row win for X") {
    val b: BoardState.Board = List(
      Some('X'), Some('X'), Some('X'),
      Some('O'), Some('O'), None,
      None, None, None
    )
    val s = BoardState(b, HUMAN)
    assert(s.isGameOver)
    assert(s.getScore == 1)
  }

  test("detect column win for O") {
    val b: BoardState.Board = List(
      Some('O'), Some('X'), Some('X'),
      Some('O'), Some('X'), None,
      Some('O'), None, None
    )
    val s = BoardState(b, HUMAN)
    assert(s.isGameOver)
    assert(s.getScore == -1)
  }

  test("serialize draws grid with dots for empty cells") {
    val s = new BoardState(HUMAN)
    assert(s.serialize().contains('.'))
  }
}
