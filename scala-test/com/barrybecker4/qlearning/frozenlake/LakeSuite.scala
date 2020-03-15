package com.barrybecker4.qlearning.frozenlake

import org.scalatest.funsuite.AnyFunSuite
import Direction._

import scala.util.Random


class LakeSuite extends AnyFunSuite {

  test("Basic Construction") {
    val lake = new Lake()
    assertResult(4) { lake.numRows }
    assertResult(4) { lake.numColumns }
    assertResult(Location(0, 0)) { lake.start }
    assertResult(true) { lake.isGoal(Location(3, 3)) }
    assertResult(false) { lake.isInHole(Location(1, 2)) }
    assertResult(true) { lake.isInHole(Location(1, 3)) }
  }

  test("legalTransitions") {
    val lake = new Lake()
    assertResult(List(N, S, E, W)) { lake.getLegalTransitionsFrom(Location(1, 2)) }
    assertResult(List()) { lake.getLegalTransitionsFrom(Location(1, 3)) }
    assertResult(List(S, E)) { lake.getLegalTransitionsFrom(Location(0, 0)) }
    assertResult(List(N, E, W)) { lake.getLegalTransitionsFrom(Location(3, 2)) }
  }

  test("attempt moves with no wind") {
    val lake = new Lake(windFrequency = 0, rnd = new Random(1))
    assertResult(Location(1, 0)) {lake.attemptToMoveInDirection(Location(0, 0), S)}
    assertResult(Location(0, 1)) {lake.attemptToMoveInDirection(Location(0, 0), E)}
  }

  test("attempt moves when high wind") {
    val lake = new Lake(windFrequency = 1.0, rnd = new Random(1))
    assertResult(Location(2, 0)) {lake.attemptToMoveInDirection(Location(0, 0), S)}
    assertResult(Location(0, 1)) {lake.attemptToMoveInDirection(Location(0, 0), E)}
    assertResult(Location(0, 2)) {lake.attemptToMoveInDirection(Location(0, 3), N)}
    assertResult(Location(0, 3)) {lake.attemptToMoveInDirection(Location(0, 3), S)}
    assertResult(Location(0, 3)) {lake.attemptToMoveInDirection(Location(0, 3), E)}
    assertResult(Location(0, 1)) {lake.attemptToMoveInDirection(Location(0, 3), W)}
  }
}
