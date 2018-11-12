package com.barrybecker4.qlearning.chopsticks

import com.barrybecker4.qlearning.TestHelper.strip
import com.barrybecker4.qlearning.common.{QLearner, QTable}
import org.scalatest.FunSuite
import scala.util.Random


class QLearnerSuite extends FunSuite {

  val learner = new QLearner[(Byte, Byte)](learningRate = 0.8f, futureRewardDiscount = 0.95f)
  val qtable = new QTable(ChopsticksState(), None, epsilon = 0.1, new Random(0))
  learner.learn(qtable, 100000)

  test("Learning short, epsilon - 0.01") {
    val learner = new QLearner[(Byte, Byte)]()
    val qtable = new QTable(ChopsticksState(), None, epsilon = 0.01, new Random(0))    // 0.01 likely too small

    learner.learn(qtable, 20000)

    assertResult(strip(
      """First player fingers: (1,1).  Second player fingers: (4,2) -> Map((2,1) -> 1.0, (1,0) -> 0.0, (2,2) -> 0.0, (1,1) -> 1.0, (1,2) -> 0.0)
        |First player fingers: (1,1).  Second player fingers: (0,2) -> Map((2,1) -> 0.0, (1,0) -> 0.0, (2,2) -> 1.0, (1,1) -> 0.0, (1,2) -> 0.999936)
        |First player fingers: (1,1).  Second player fingers: (4,4) -> Map((2,1) -> 1.0, (1,0) -> 0.0, (2,2) -> 1.0, (1,1) -> 1.0, (1,2) -> 1.0)
        |First player fingers: (1,1).  Second player fingers: (0,4) -> Map((2,1) -> 0.0, (1,0) -> 1.0, (2,2) -> 1.0, (1,1) -> 0.0, (1,2) -> 1.0)
        |First player fingers: (1,1).  Second player fingers: (1,4) -> Map((2,1) -> 0.0, (1,0) -> 0.0, (2,2) -> 1.0, (1,1) -> 0.0, (1,2) -> 1.0)
        |First player fingers: (1,1).  Second player fingers: (4,0) -> Map((2,1) -> 1.0, (1,0) -> 1.0, (2,2) -> 0.0, (1,1) -> 1.0, (1,2) -> 0.0)
        |First player fingers: (1,1).  Second player fingers: (0,1) -> Map((2,1) -> 0.0, (1,0) -> 1.0, (2,2) -> 0.0, (1,1) -> 0.0, (1,2) -> 0.0)
        |First player fingers: (1,1).  Second player fingers: (0,2) -> Map((2,1) -> 0.0, (1,0) -> 1.0, (2,2) -> 0.0, (1,1) -> 0.0, (1,2) -> 0.0)
        |First player fingers: (1,1).  Second player fingers: (3,4) -> Map((2,1) -> 0.0, (1,0) -> 0.0, (2,2) -> 1.0, (1,1) -> 0.0, (1,2) -> 1.0)
        |First player fingers: (1,1).  Second player fingers: (0,3) -> Map((2,1) -> 0.0, (1,0) -> 0.0, (2,2) -> 1.0, (1,1) -> 0.0, (1,2) -> 1.0)""")) {
      qtable.getFirstNEntriesWithNon0Actions(10)
    }
  }

  test("Learning short, epsilon - 0.1") {
    val learner = new QLearner[(Byte, Byte)](learningRate = 0.8f, futureRewardDiscount = 0.95f)
    val qtable = new QTable(ChopsticksState(), None, epsilon = 0.1, new Random(0))

    learner.learn(qtable, 20000)

    assertResult(strip(
      """First player fingers: (1,1).  Second player fingers: (4,2) -> Map((2,1) -> 0.85737497, (1,0) -> 0.0, (2,2) -> 0.0, (1,1) -> 0.85737497, (1,2) -> 0.0)
        |First player fingers: (1,1).  Second player fingers: (0,2) -> Map((2,1) -> 0.0, (1,0) -> 0.0, (2,2) -> 0.9025, (1,1) -> 0.0, (1,2) -> 0.9025)
        |First player fingers: (1,1).  Second player fingers: (4,4) -> Map((2,1) -> 0.95, (1,0) -> 0.0, (2,2) -> 0.95, (1,1) -> 0.95, (1,2) -> 0.95)
        |First player fingers: (1,1).  Second player fingers: (0,4) -> Map((2,1) -> 0.0, (1,0) -> 0.95, (2,2) -> 1.0, (1,1) -> 0.0, (1,2) -> 1.0)
        |First player fingers: (1,1).  Second player fingers: (1,4) -> Map((2,1) -> 0.0, (1,0) -> 0.0, (2,2) -> 0.81450623, (1,1) -> 0.0, (1,2) -> 0.81450623)
        |First player fingers: (1,1).  Second player fingers: (4,0) -> Map((2,1) -> 1.0, (1,0) -> 0.95, (2,2) -> 0.0, (1,1) -> 1.0, (1,2) -> 0.0)
        |First player fingers: (1,1).  Second player fingers: (0,1) -> Map((2,1) -> 0.0, (1,0) -> 0.81450623, (2,2) -> 0.0, (1,1) -> 0.0, (1,2) -> 0.0)
        |First player fingers: (1,1).  Second player fingers: (0,2) -> Map((2,1) -> 0.0, (1,0) -> 0.85737497, (2,2) -> 0.0, (1,1) -> 0.0, (1,2) -> 0.0)
        |First player fingers: (1,1).  Second player fingers: (3,4) -> Map((2,1) -> 0.0, (1,0) -> 0.0, (2,2) -> 0.9025, (1,1) -> 0.0, (1,2) -> 0.9025)
        |First player fingers: (1,1).  Second player fingers: (0,3) -> Map((2,1) -> 0.0, (1,0) -> 0.0, (2,2) -> 0.95, (1,1) -> 0.0, (1,2) -> 0.95)""")) {
      qtable.getFirstNEntriesWithNon0Actions(10)
    }
  }

  test("Learning short, epsilon - 0.2") {
    val learner = new QLearner[(Byte, Byte)](learningRate = 0.8f, futureRewardDiscount = 0.95f)
    val qtable = new QTable(ChopsticksState(), None, epsilon = 0.2, new Random(0))  // 0.2 is too big

    learner.learn(qtable, 20000)

    assertResult(strip(
      """First player fingers: (1,1).  Second player fingers: (4,2) -> Map((2,1) -> 0.85737497, (1,0) -> 0.0, (2,2) -> 0.0, (1,1) -> 0.85737497, (1,2) -> 0.0)
        |First player fingers: (1,1).  Second player fingers: (0,2) -> Map((2,1) -> 0.0, (1,0) -> 0.0, (2,2) -> 0.9025, (1,1) -> 0.0, (1,2) -> 0.9025)
        |First player fingers: (1,1).  Second player fingers: (4,4) -> Map((2,1) -> 0.95, (1,0) -> 0.0, (2,2) -> 0.95, (1,1) -> 0.95, (1,2) -> 0.95)
        |First player fingers: (1,1).  Second player fingers: (0,4) -> Map((2,1) -> 0.0, (1,0) -> 0.95, (2,2) -> 1.0, (1,1) -> 0.0, (1,2) -> 1.0)
        |First player fingers: (1,1).  Second player fingers: (1,4) -> Map((2,1) -> 0.0, (1,0) -> 0.0, (2,2) -> 0.81450623, (1,1) -> 0.0, (1,2) -> 0.81450623)
        |First player fingers: (1,1).  Second player fingers: (4,0) -> Map((2,1) -> 1.0, (1,0) -> 0.95, (2,2) -> 0.0, (1,1) -> 1.0, (1,2) -> 0.0)
        |First player fingers: (1,1).  Second player fingers: (0,1) -> Map((2,1) -> 0.0, (1,0) -> 0.81450623, (2,2) -> 0.0, (1,1) -> 0.0, (1,2) -> 0.0)
        |First player fingers: (1,1).  Second player fingers: (0,2) -> Map((2,1) -> 0.0, (1,0) -> 0.85737497, (2,2) -> 0.0, (1,1) -> 0.0, (1,2) -> 0.0)
        |First player fingers: (1,1).  Second player fingers: (3,4) -> Map((2,1) -> 0.0, (1,0) -> 0.0, (2,2) -> 0.9025, (1,1) -> 0.0, (1,2) -> 0.9025)
        |First player fingers: (1,1).  Second player fingers: (0,3) -> Map((2,1) -> 0.0, (1,0) -> 0.0, (2,2) -> 0.95, (1,1) -> 0.0, (1,2) -> 0.95)""")) {
      qtable.getFirstNEntriesWithNon0Actions(10)
    }
  }

  test("Learning (long)") {
    assertResult(strip(
      """First player fingers: (1,1).  Second player fingers: (4,2) -> Map((2,1) -> 0.85737497, (1,0) -> 0.0, (2,2) -> 0.0, (1,1) -> 0.85737497, (1,2) -> 0.0)
        |First player fingers: (1,1).  Second player fingers: (0,2) -> Map((2,1) -> 0.0, (1,0) -> 0.0, (2,2) -> 0.9025, (1,1) -> 0.0, (1,2) -> 0.9025)
        |First player fingers: (1,1).  Second player fingers: (4,4) -> Map((2,1) -> 0.95, (1,0) -> 0.0, (2,2) -> 0.95, (1,1) -> 0.95, (1,2) -> 0.95)
        |First player fingers: (1,1).  Second player fingers: (0,4) -> Map((2,1) -> 0.0, (1,0) -> 0.95, (2,2) -> 1.0, (1,1) -> 0.0, (1,2) -> 1.0)
        |First player fingers: (1,1).  Second player fingers: (1,4) -> Map((2,1) -> 0.0, (1,0) -> 0.0, (2,2) -> 0.81450623, (1,1) -> 0.0, (1,2) -> 0.81450623)
        |First player fingers: (1,1).  Second player fingers: (4,0) -> Map((2,1) -> 1.0, (1,0) -> 0.95, (2,2) -> 0.0, (1,1) -> 1.0, (1,2) -> 0.0)
        |First player fingers: (1,1).  Second player fingers: (0,1) -> Map((2,1) -> 0.0, (1,0) -> 0.81450623, (2,2) -> 0.0, (1,1) -> 0.0, (1,2) -> 0.0)
        |First player fingers: (1,1).  Second player fingers: (0,2) -> Map((2,1) -> 0.0, (1,0) -> 0.85737497, (2,2) -> 0.0, (1,1) -> 0.0, (1,2) -> 0.0)
        |First player fingers: (1,1).  Second player fingers: (3,4) -> Map((2,1) -> 0.0, (1,0) -> 0.0, (2,2) -> 0.9025, (1,1) -> 0.0, (1,2) -> 0.9025)
        |First player fingers: (1,1).  Second player fingers: (0,3) -> Map((2,1) -> 0.0, (1,0) -> 0.0, (2,2) -> 0.95, (1,1) -> 0.0, (1,2) -> 0.95)""")) {
      qtable.getFirstNEntriesWithNon0Actions(10)
    }

    assertResult(strip("""numEntries=580""")) {
      qtable.toString
    }
  }

  test("check actions for normal move") {
    assertResult("(2,1) -> 0.0, (1,0) -> 0.0, (2,2) -> 0.0, (1,1) -> 0.0, (1,2) -> 0.0") {
      qtable.getActions(ChopsticksState((1, 1), (2, 2))).mkString(", ")
    }
  }

  test("check actions after player disables opponent hand") {
    assertResult("(2,1) -> 0.0, (1,0) -> 0.0, (2,2) -> 0.54503256") {
      qtable.getActions(ChopsticksState((0, 2), (2, 3))).mkString(", ")
    }
  }

  test("check actions when disabling a hand is possible") {
    assertResult("(1,0) -> 0.0, (1,1) -> 0.0, (1,2) -> 0.85737497") {
      qtable.getActions(ChopsticksState((2, 0), (1, 4))).mkString(", ")
    }
  }
}
