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
      """First player fingers: (4,4).  Second player fingers: (1,4) -> HashMap((2,1) -> 0.0, (1,1) -> 0.0, (1,2) -> 0.0, (2,2) -> 1.0)
        |First player fingers: (1,4).  Second player fingers: (2,0) -> HashMap((2,1) -> 0.9984, (1,1) -> 0.0, (1,2) -> 0.0, (2,2) -> 0.0)
        |First player fingers: (3,3).  Second player fingers: (1,0) -> HashMap((1,1) -> 1.0, (1,2) -> 1.0)
        |First player fingers: (1,4).  Second player fingers: (2,0) -> HashMap((1,1) -> 1.0, (1,2) -> 1.0, (1,0) -> 0.0)
        |First player fingers: (3,2).  Second player fingers: (1,0) -> HashMap((1,1) -> 1.0, (1,2) -> 0.0)
        |First player fingers: (3,3).  Second player fingers: (2,1) -> HashMap((2,1) -> 0.0, (1,1) -> 0.7713332, (1,2) -> 0.7084325, (2,2) -> 0.0)
        |First player fingers: (0,3).  Second player fingers: (0,3) -> HashMap((2,1) -> -0.76800007, (2,2) -> 1.0)
        |First player fingers: (2,3).  Second player fingers: (4,1) -> HashMap((2,1) -> 1.0, (1,1) -> 1.0, (1,2) -> 0.0, (2,2) -> 0.0)
        |First player fingers: (0,1).  Second player fingers: (1,2) -> HashMap((2,1) -> 0.0, (1,1) -> 0.0, (1,2) -> 0.0, (2,2) -> 1.0)
        |First player fingers: (3,4).  Second player fingers: (2,0) -> HashMap((1,1) -> 1.0, (1,2) -> 1.0, (1,0) -> 0.0)""".stripMargin)) {
      qtable.getFirstNEntriesWithNon0Actions(10)
    }
  }

  test("Learning short, epsilon - 0.1") {
    val learner = new QLearner[(Byte, Byte)](learningRate = 0.8f, futureRewardDiscount = 0.95f)
    val qtable = new QTable(ChopsticksState(), None, epsilon = 0.1, new Random(0))

    learner.learn(qtable, 20000)

    assertResult(strip(
      """First player fingers: (4,4).  Second player fingers: (1,4) -> HashMap((2,1) -> 0.0, (1,1) -> 0.5878794, (1,2) -> 0.554496, (2,2) -> 0.9025)
        |First player fingers: (1,4).  Second player fingers: (2,0) -> HashMap((2,1) -> 0.9984, (1,1) -> -0.5041981, (1,2) -> 0.0, (2,2) -> 0.0)
        |First player fingers: (3,3).  Second player fingers: (1,0) -> HashMap((1,1) -> 0.95, (1,2) -> 0.95)
        |First player fingers: (1,4).  Second player fingers: (2,0) -> HashMap((1,1) -> 0.95, (1,2) -> 0.85737497, (1,0) -> 0.0)
        |First player fingers: (3,2).  Second player fingers: (1,0) -> HashMap((1,1) -> 0.95, (1,2) -> 0.0)
        |First player fingers: (3,3).  Second player fingers: (2,1) -> HashMap((2,1) -> 0.0, (1,1) -> 0.6703096, (1,2) -> 0.54701847, (2,2) -> 0.0)
        |First player fingers: (0,3).  Second player fingers: (0,3) -> HashMap((2,1) -> -0.95, (2,2) -> 1.0)
        |First player fingers: (2,3).  Second player fingers: (4,1) -> HashMap((2,1) -> 0.85737497, (1,1) -> 0.6983372, (1,2) -> 0.0, (2,2) -> 0.0)
        |First player fingers: (0,1).  Second player fingers: (1,2) -> HashMap((2,1) -> 0.0, (1,1) -> 0.0, (1,2) -> 0.0, (2,2) -> 0.6983372)
        |First player fingers: (3,4).  Second player fingers: (2,0) -> HashMap((1,1) -> 0.95, (1,2) -> 0.95, (1,0) -> 0.0)""")) {
      qtable.getFirstNEntriesWithNon0Actions(10)
    }
  }

  test("Learning short, epsilon - 0.2") {
    val learner = new QLearner[(Byte, Byte)](learningRate = 0.8f, futureRewardDiscount = 0.95f)
    val qtable = new QTable(ChopsticksState(), None, epsilon = 0.2, new Random(0))  // 0.2 is too big

    learner.learn(qtable, 20000)

    assertResult(strip(
      """First player fingers: (4,4).  Second player fingers: (1,4) -> HashMap((2,1) -> 0.0, (1,1) -> 0.7350101, (1,2) -> 0.8186809, (2,2) -> 0.9025)
        |First player fingers: (1,4).  Second player fingers: (2,0) -> HashMap((2,1) -> 1.0, (1,1) -> 0.0, (1,2) -> -0.39142016, (2,2) -> 0.0)
        |First player fingers: (3,3).  Second player fingers: (1,0) -> HashMap((1,1) -> 0.95, (1,2) -> 0.95)
        |First player fingers: (1,4).  Second player fingers: (2,0) -> HashMap((1,1) -> 0.95, (1,2) -> 0.85737497, (1,0) -> 0.0)
        |First player fingers: (3,2).  Second player fingers: (1,0) -> HashMap((1,1) -> 0.95, (1,2) -> 0.0)
        |First player fingers: (3,3).  Second player fingers: (2,1) -> HashMap((2,1) -> 0.0, (1,1) -> 0.670147, (1,2) -> 0.69154364, (2,2) -> 0.0)
        |First player fingers: (0,3).  Second player fingers: (0,3) -> HashMap((2,1) -> -0.95, (2,2) -> 1.0)
        |First player fingers: (2,3).  Second player fingers: (4,1) -> HashMap((2,1) -> 0.85737497, (1,1) -> 0.6983372, (1,2) -> 0.0, (2,2) -> 0.0)
        |First player fingers: (0,1).  Second player fingers: (1,2) -> HashMap((2,1) -> 0.0, (1,1) -> 0.0, (1,2) -> 0.0, (2,2) -> 0.6983372)
        |First player fingers: (3,4).  Second player fingers: (2,0) -> HashMap((1,1) -> 0.95, (1,2) -> 0.95, (1,0) -> 0.0)""".stripMargin)) {
      qtable.getFirstNEntriesWithNon0Actions(10)
    }
  }

  test("Learning (long)") {
    assertResult(strip(
      """First player fingers: (1,0).  Second player fingers: (4,0) -> HashMap((1,1) -> 1.0, (1,2) -> -0.95)
        |First player fingers: (4,4).  Second player fingers: (1,4) -> HashMap((2,1) -> 0.73485655, (1,1) -> 0.7350899, (1,2) -> 0.8328992, (2,2) -> 0.9025)
        |First player fingers: (3,3).  Second player fingers: (1,0) -> HashMap((1,1) -> 0.95, (1,2) -> 0.95)
        |First player fingers: (1,4).  Second player fingers: (2,0) -> HashMap((1,1) -> 0.95, (1,2) -> 0.85737497, (1,0) -> 0.0)
        |First player fingers: (3,2).  Second player fingers: (1,0) -> HashMap((1,1) -> 0.95, (1,2) -> 0.0)
        |First player fingers: (3,3).  Second player fingers: (2,1) -> HashMap((2,1) -> 0.0, (1,1) -> 0.69833684, (1,2) -> 0.69833714, (2,2) -> 0.0)
        |First player fingers: (0,3).  Second player fingers: (0,3) -> HashMap((2,1) -> -0.95, (2,2) -> 1.0)
        |First player fingers: (2,3).  Second player fingers: (4,1) -> HashMap((2,1) -> 0.85737497, (1,1) -> 0.6983372, (1,2) -> 0.0, (2,2) -> 0.0)
        |First player fingers: (0,1).  Second player fingers: (1,2) -> HashMap((2,1) -> 0.0, (1,1) -> 0.0, (1,2) -> 0.0, (2,2) -> 0.6983372)
        |First player fingers: (3,4).  Second player fingers: (2,0) -> HashMap((1,1) -> 0.95, (1,2) -> 0.95, (1,0) -> 0.0)""".stripMargin)) {
      qtable.getFirstNEntriesWithNon0Actions(10)
    }

    assertResult(strip("""numEntries=1094""")) {
      qtable.toString
    }
  }

  test("check actions for normal move") {
    assertResult("(2,1) -> 0.0, (1,1) -> 0.0, (1,2) -> 0.0, (2,2) -> 0.0") {
      qtable.getActions(ChopsticksState((1, 1), (2, 2))).mkString(", ")
    }
  }

  test("check actions after player disables opponent hand") {
    assertResult("(2,1) -> -0.95, (1,0) -> 0.0, (2,2) -> 0.0") {
      qtable.getActions(ChopsticksState((0, 2), (2, 3))).mkString(", ")
    }
  }

  test("check actions when disabling a hand is possible") {
    assertResult("(1,1) -> -0.95, (1,2) -> -0.85737497, (1,0) -> 0.0") {
      qtable.getActions(ChopsticksState((2, 0), (1, 4))).mkString(", ")
    }
  }
}
