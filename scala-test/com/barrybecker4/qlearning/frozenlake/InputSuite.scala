package com.barrybecker4.qlearning.frozenlake

import org.scalatest.funsuite.AnyFunSuite

import java.io.ByteArrayInputStream
import java.nio.charset.StandardCharsets
import java.util.Scanner

class InputSuite extends AnyFunSuite {

  private def inputFromLines(lines: String*): Input =
    new Input(new Scanner(new ByteArrayInputStream(lines.mkString("\n").getBytes(StandardCharsets.UTF_8))))

  test("getNumber returns default on empty line") {
    val in = inputFromLines("\n")
    assertResult(3.5)(in.getNumber(3.5, 0, 10))
  }

  test("getNumber parses valid number") {
    val in = inputFromLines("2.5")
    assertResult(2.5)(in.getNumber(0, 0, 10))
  }

  test("getNumber rejects non-numeric then accepts valid") {
    val in = inputFromLines("not-a-number", "4")
    assertResult(4.0)(in.getNumber(0, 0, 10))
  }

  test("getNumber rejects out of range then accepts valid") {
    val in = inputFromLines("99", "5")
    assertResult(5.0)(in.getNumber(0, 0, 10))
  }

  test("charQuery returns default on empty line") {
    val in = inputFromLines("\n")
    assertResult('y')(in.charQuery("ok?", Seq('y', 'n'), Some('y')))
  }
}
