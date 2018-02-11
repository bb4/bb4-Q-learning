package com.barrybecker4.qlearning.frozenlake

import org.scalatest.FunSuite
import Direction._

import scala.util.Random

class LakeStateSuite extends FunSuite {

  val lake = Lake()

  test("LakeState on ice") {
    val lakeState = LakeState(Location(0, 1), lake)

    assertResult(false) { lakeState.isInHole }
    assertResult(false) { lakeState.isGoalReached }
    assertResult(Seq(S, E, W)) { lakeState.getLegalTransitions }
    assertResult(true) { lakeState.hasTransitions }
    assertResult(0.0f) { lakeState.rewardForLastMove }
  }

  test("LakeState in hole") {
    val lakeState = LakeState(Location(1, 1), lake)

    assertResult(true) { lakeState.isInHole }
    assertResult(false) { lakeState.isGoalReached }
    assertResult(Seq()) { lakeState.getLegalTransitions }
    assertResult(false) { lakeState.hasTransitions }
    assertResult(-1.0f) { lakeState.rewardForLastMove }
  }

  test("LakeState at goal") {
    val lakeState = LakeState(Location(3, 3), lake)

    assertResult(false) { lakeState.isInHole }
    assertResult(true) { lakeState.isGoalReached }
    assertResult(Seq()) { lakeState.getLegalTransitions }
    assertResult(false) { lakeState.hasTransitions }
    assertResult(1.0f) { lakeState.rewardForLastMove }
  }

  test("makeTransition when no wind") {
    val lakeState = LakeState(Location(2, 2), new Lake(windFrequency = 0))

    assertResult(Location(1, 2)) { lakeState.makeTransition(N).location }
    assertResult(Location(3, 2)) { lakeState.makeTransition(S).location }
    assertResult(Location(2, 3)) { lakeState.makeTransition(E).location }
    assertResult(Location(2, 1)) { lakeState.makeTransition(W).location }
  }

  test("makeTransition when high wind") {
    val lakeState = LakeState(Location(2, 2), new Lake(windFrequency = 0.9, rnd = new Random(2)))

    assertResult(Location(1, 1)) { lakeState.makeTransition(N).location }
    assertResult(Location(3, 1)) { lakeState.makeTransition(S).location }
    assertResult(Location(2, 3)) { lakeState.makeTransition(E).location }
    assertResult(Location(2, 0)) { lakeState.makeTransition(W).location }
  }

  test("selectBestAction") {
    val lakeState = LakeState(Location(2, 2), lake)
    assertResult((S, 0.4f)) { lakeState.selectBestAction(Seq((N, 0.3f), (S, 0.4f), (E, 0.1f)), new Random(1)) }
  }
}
