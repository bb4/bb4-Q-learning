package com.barrybecker4.qlearning.ttt

import org.scalatest.FunSuite


class TTTBoardSuite extends FunSuite {

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
    val b = TTTBoard("XO.O.O.XX")
    assertResult("XOX\nO.O\n.XX") {b.makeMove(2, 'X').toString}
  }
}