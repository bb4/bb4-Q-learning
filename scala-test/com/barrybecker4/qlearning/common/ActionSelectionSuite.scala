package com.barrybecker4.qlearning.common

import org.scalatest.funsuite.AnyFunSuite

import scala.util.Random

class ActionSelectionSuite extends AnyFunSuite {

  test("pickBest maximize chooses highest value") {
    val rnd = new Random(0)
    val actions = Seq((1, 0.1f), (2, 0.9f), (3, 0.5f))
    assertResult((2, 0.9f))(ActionSelection.pickBest(actions, maximize = true, rnd))
  }

  test("pickBest minimize chooses lowest value") {
    val rnd = new Random(0)
    val actions = Seq((1, 0.1f), (2, 0.9f), (3, 0.5f))
    assertResult((1, 0.1f))(ActionSelection.pickBest(actions, maximize = false, rnd))
  }

  test("pickBest throws on empty list") {
    intercept[IllegalArgumentException] {
      ActionSelection.pickBest(Seq.empty, maximize = true, new Random(0))
    }
  }
}
