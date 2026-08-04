package com.barrybecker4.qlearning.ttt

import org.scalatest.funsuite.AnyFunSuite

import scala.util.Random


class TTTBoardSuite extends AnyFunSuite {

  test("construct board and serialize") {
    val b = TTTBoard("X......O.")
    assertResult("X..\n...\n.O.") {b.toString}
  }

  test("is won when won -") {
    val b = TTTBoard(".X.OOOXX.")
    assertResult(true) {b.isWon('O')}
    assertResult(false) {b.isWon('X')}
    assertResult(true) {b.isWon}
  }

  test("is won when won |") {
    val b = TTTBoard(".X.OXOOX.")
    assertResult(false) {b.isWon('O')}
    assertResult(true) {b.isWon('X')}
    assertResult(true) {b.isWon}
  }

  test("is won when won /") {
    val b = TTTBoard("OOX.XOX.O")
    assertResult(false) {b.isWon('O')}
    assertResult(true) {b.isWon('X')}
    assertResult(true) {b.isWon}
  }

  test("is won when won ") {
    val b = TTTBoard("XO.OXOOXX")
    assertResult(false) {b.isWon('O')}
    assertResult(true) {b.isWon('X')}
    assertResult(true) {b.isWon}
  }

  test("is won when not won") {
    val b = TTTBoard("XO.O.O.XX")
    assertResult(false) {b.isWon('O')}
    assertResult(false) {b.isWon('X')}
    assertResult(false) {b.isWon}
  }

  test("make move X") {
    val b = TTTBoard("XO.O.O.XX", 'X')
    assertResult("XOX\nO.O\n.XX") {b.makeTransition(2).toString}
  }

  test("make move O") {
    val b = TTTBoard("XO.O.O.XX", 'O')
    assertResult("XOO\nO.O\n.XX") {b.makeTransition(2).toString}
  }

  test("isWonByLastMove when won") {
    val b = TTTBoard("XO.O.O.XX", 'X').makeTransition(4)
    assertResult(true) {b.isWonByLastMove}
  }

  test("isWonByLastMove when not won") {
    val b = TTTBoard("XO.O.O.XX", 'X').makeTransition(2)
    assertResult(false) {b.isWonByLastMove}
  }

  test("hasMoves when some") {
    val b = TTTBoard("OX.XXO...", 'O')
    assertResult(true) {b.hasTransitions}
  }

  test("hasMoves when none") {
    val b = TTTBoard("OXOXXOOOX", 'O')
    assertResult(false) {b.hasTransitions}
  }

  test("getReward when not won") {
    val b = TTTBoard("OX.XXO...", 'O')
    assertResult(0f) {b.rewardForLastMove}
    b.makeTransition(2)
    assertResult(0f) {b.rewardForLastMove}
  }

  test("getReward when X won") {
    val b0 = TTTBoard("OX.XXO...", 'X')
    assertResult(0f) {b0.rewardForLastMove}
    val b = b0.makeTransition(7)
    assertResult(1.0f) {b.rewardForLastMove}
  }

  test("getReward when O won") {
    val b0 = TTTBoard("OX.XOX...", 'O')
    assertResult(0f) {b0.rewardForLastMove}
    val b = b0.makeTransition(8)
    assertResult(-1.0f) {b.rewardForLastMove}
  }

  test("select best move for O") {
    val b = TTTBoard("OX.XOX...", 'O')
    val actionsList: Seq[(Int, Float)] = Seq((3, 0.5f), (5, 0.4f))
    val best = b.selectBestAction(actionsList, new Random(1))
    assertResult((5, 0.4f)) { best }
  }

  test("select best move for X") {
    val b = TTTBoard("OX.XOX...", 'X')
    val actionsList: Seq[(Int, Float)] = Seq((3, 0.5f), (5, 0.4f))
    val best = b.selectBestAction(actionsList, new Random(1))
    assertResult((3, 0.5f)) { best }
  }

  test("played already when played") {
    assertResult(true) {TTTBoard("OX.XOX...", 'X').occupied(1)}
  }
  test("played already when available") {
    assertResult(false) {TTTBoard("OX.XOX...", 'X').occupied(2)}
  }
}