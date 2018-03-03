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

  test("isWon horizontal (false)") {
    val b = Connect4Board("XXOOX.O.OXOX...O")
    assertResult(false) (b.isWon('X'))
    assertResult(false) (b.isWon('O'))
  }

  test("isWon horizontal (true") {
    val b = Connect4Board(".XXXXX.O.OXOX...O")
    assertResult(true) (b.isWon('X'))
    assertResult(false) (b.isWon('O'))
  }

  test("isWon vertical (false)") {
    val b = Connect4Board("XXOOX.O.OXOX...OXO.....OX")
    assertResult(false, "unexpected win by X") (b.isWon('X'))
    assertResult(false, "unexpected win by O") (b.isWon('O'))
  }

  test("isWon vertical (true") {
    val b = Connect4Board("XXOOX.O.OXOX...OXO......O")
    assertResult(false) (b.isWon('X'))
    assertResult(true) (b.isWon('O'))
  }

  test("isWon diagonal / (false)") {
    val b = Connect4Board("XOXOXOXOOXXOOXX.X.O.OX.OO")
    assertResult(false, "unexpected win by X") (b.isWon('X'))
    assertResult(false, "unexpected win by O") (b.isWon('O'))
  }

  test("isWon diagonal / (true") {
    val b = Connect4Board("XOXOXOXOXOXOOXX.XO..OX..OO")
    assertResult(false) (b.isWon('X'))
    assertResult(true) (b.isWon('O'))
  }

  test("isWon diagonal \\ (false)") {
    val b = Connect4Board("XOXOXOXOOXXOOXX.X.O.OX.OO")
    assertResult(false, "unexpected win by X") (b.isWon('X'))
    assertResult(false, "unexpected win by O") (b.isWon('O'))
  }

  test("isWon diagonal \\ (true)") {
    val b = Connect4Board("XOXOOOXOXOXOOXX.XO..O.XOO")
    assertResult(false) (b.isWon('X'))
    assertResult(true) (b.isWon('O'))
  }

  test("has transitions (none when win present)") {
    val b = Connect4Board("XOXOOOXOXOXOOXX.XO..O.XOO")
    assertResult(false) (b.hasTransitions)
  }

  test("has transitions (true)") {
    val b = Connect4Board("XOXOOOXOXOXOOXX.XO..O.XXO")
    assertResult(false) (b.isWon('X'))
    assertResult(false) (b.isWon('O'))
    assertResult(true) (b.hasTransitions)
  }

  test("has transitions (almost all filled)") {
    val b = Connect4Board("XOXOXXXOXOXOOXXOXOXOOXOXOX")
    println(b)
    assertResult(false) (b.isWon('X'))
    assertResult(false) (b.isWon('O'))
    assertResult(true) (b.hasTransitions)
  }

  test("has no transitions") {
    val b = Connect4Board("XOXOOOXOXOXOOOXXOXOOOXXOXOOOXXOXOOO")
    assertResult(false) (b.hasTransitions)
  }

  test("get legal transitions (when none)") {
    val b = Connect4Board("XOXOOOXOXOXOOOXXOXOOOXXOXOOOXXOXOOO")
    assertResult("") (b.getLegalTransitions.mkString(", "))
  }

  test("get legal transitions (when some)") {
    val b = Connect4Board("XOXOOOXOXOXOOOXXOXOXOXXOXXXOX.XO")
    println(b)
    assertResult("35, 29, 37, 38, 32, 33, 34") (b.getLegalTransitions.mkString(", "))
  }

  test("get legal transitions (for empty board)") {
    val b = Connect4Board("")
    assertResult("0, 1, 2, 3, 4, 5, 6") (b.getLegalTransitions.mkString(", "))
  }

  test("get legal transitions (at start of game)") {
    val b = Connect4Board("...X.O")
    assertResult("0, 1, 2, 10, 4, 12, 6") (b.getLegalTransitions.mkString(", "))
  }
}
