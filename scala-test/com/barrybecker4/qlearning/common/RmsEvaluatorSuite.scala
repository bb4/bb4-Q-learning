package com.barrybecker4.qlearning.common

import com.barrybecker4.qlearning.ttt.TTTBoard
import org.scalatest.funsuite.AnyFunSuite

import scala.collection.mutable
import scala.util.Random

class RmsEvaluatorSuite extends AnyFunSuite {

  test("identical tables have RMS 0") {
    val rnd = new Random(1)
    val t1 = new QTable(TTTBoard(), None, 0.1, rnd)
    val t2 = new QTable(TTTBoard(), None, 0.1, rnd)
    assert(new RmsEvaluator(t1, t2).evaluate() == 0.0)
  }

  test("RMS over single state matches hand calculation") {
    val rnd = new Random(1)
    val s0 = TTTBoard()
    val legal = s0.getLegalTransitions.toList
    val mGold = mutable.Map.from(legal.map(_ -> 0.0f))
    val mLearn = mutable.Map.from(legal.map(_ -> 0.0f))
    mLearn(legal.head) = 1.0f

    val gold = new QTable(s0, Some(Map(s0 -> mGold)), 0.1, rnd)
    val learned = new QTable(s0, Some(Map(s0 -> mLearn)), 0.1, rnd)

    val expected = math.sqrt(1.0 / legal.length)
    assert(math.abs(new RmsEvaluator(learned, gold).evaluate() - expected) < 1e-9)
  }

  test("empty tables yield 0 RMS") {
    val rnd = new Random(1)
    val s0 = TTTBoard()
    val empty = new QTable(s0, Some(Map.empty), 0.1, rnd)
    assert(new RmsEvaluator(empty, empty).evaluate() == 0.0)
  }
}
