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
      """First player fingers: (3,2).  Second player fingers: (1,4) -> Map((2,1) -> 0.0, (2,2) -> 1.0, (1,1) -> 0.0, (1,2) -> 1.0)
        |First player fingers: (2,4).  Second player fingers: (0,2) -> Map((2,1) -> 1.0, (1,0) -> 0.0, (2,2) -> 0.0)
        |First player fingers: (2,4).  Second player fingers: (2,4) -> Map((2,1) -> 0.0, (1,0) -> 0.9977705, (2,2) -> 0.0, (1,1) -> 0.7517849, (1,2) -> 0.0)
        |First player fingers: (3,3).  Second player fingers: (3,0) -> Map((2,1) -> 0.0, (2,2) -> 0.0, (1,1) -> 0.96000004, (1,2) -> 0.0)
        |First player fingers: (2,4).  Second player fingers: (0,4) -> Map((2,1) -> 0.99967957, (1,0) -> 0.0, (2,2) -> 0.999997)
        |First player fingers: (0,3).  Second player fingers: (0,4) -> Map((2,1) -> -0.9216, (2,2) -> 1.0)
        |First player fingers: (1,2).  Second player fingers: (3,1) -> Map((2,1) -> 0.0, (1,0) -> 0.0, (2,2) -> 0.57344, (1,1) -> 0.71678036, (1,2) -> -1.0)
        |First player fingers: (3,2).  Second player fingers: (4,1) -> Map((2,1) -> 0.0, (2,2) -> 1.0, (1,1) -> 0.0, (1,2) -> 0.99999994)
        |First player fingers: (4,4).  Second player fingers: (4,2) -> Map((2,1) -> 0.0, (2,2) -> 0.0, (1,1) -> 0.99927294, (1,2) -> 0.0)
        |First player fingers: (0,1).  Second player fingers: (0,2) -> Map((2,1) -> 0.0, (1,0) -> 0.0, (2,2) -> 1.0)""")) {
      qtable.getFirstNEntriesWithNon0Actions(10)
    }
  }

  test("Learning short, epsilon - 0.1") {
    val learner = new QLearner[(Byte, Byte)](learningRate = 0.8f, futureRewardDiscount = 0.95f)
    val qtable = new QTable(ChopsticksState(), None, epsilon = 0.1, new Random(0))

    learner.learn(qtable, 20000)

    assertResult(strip(
      """First player fingers: (3,2).  Second player fingers: (1,4) -> Map((2,1) -> 0.0, (2,2) -> 0.6983372, (1,1) -> 0.0, (1,2) -> 0.85737497)
        |First player fingers: (2,4).  Second player fingers: (0,2) -> Map((2,1) -> 0.95, (1,0) -> 0.0, (2,2) -> 0.0)
        |First player fingers: (2,4).  Second player fingers: (2,4) -> Map((2,1) -> 0.0, (1,0) -> 0.8572295, (2,2) -> 0.0, (1,1) -> 0.77378, (1,2) -> 0.0)
        |First player fingers: (3,3).  Second player fingers: (3,0) -> Map((2,1) -> 0.96000004, (2,2) -> -0.52325827, (1,1) -> 1.0, (1,2) -> -0.6531679)
        |First player fingers: (2,4).  Second player fingers: (0,4) -> Map((2,1) -> 0.9499392, (1,0) -> 0.0, (2,2) -> 0.94847804)
        |First player fingers: (0,3).  Second player fingers: (0,4) -> Map((2,1) -> -0.9499586, (2,2) -> 1.0)
        |First player fingers: (1,2).  Second player fingers: (3,1) -> Map((2,1) -> 0.7737808, (1,0) -> 0.0, (2,2) -> 0.7737789, (1,1) -> 0.8504721, (1,2) -> -0.81450623)
        |First player fingers: (3,2).  Second player fingers: (4,1) -> Map((2,1) -> 0.0, (2,2) -> 0.85737497, (1,1) -> 0.0, (1,2) -> 0.6983372)
        |First player fingers: (2,4).  Second player fingers: (1,2) -> Map((2,1) -> 0.5755132, (2,2) -> 0.0, (1,1) -> 0.0, (1,2) -> 0.0)
        |First player fingers: (2,1).  Second player fingers: (3,0) -> Map((1,1) -> -0.7350918, (1,2) -> 0.8512)""")) {
      qtable.getFirstNEntriesWithNon0Actions(10)
    }
  }

  test("Learning short, epsilon - 0.2") {
    val learner = new QLearner[(Byte, Byte)](learningRate = 0.8f, futureRewardDiscount = 0.95f)
    val qtable = new QTable(ChopsticksState(), None, epsilon = 0.2, new Random(0))  // 0.2 is too big

    learner.learn(qtable, 20000)

    assertResult(strip(
      """First player fingers: (3,2).  Second player fingers: (1,4) -> Map((2,1) -> 0.0, (2,2) -> 0.6983372, (1,1) -> 0.0, (1,2) -> 0.85737497)
        |First player fingers: (2,4).  Second player fingers: (0,2) -> Map((2,1) -> 0.95, (1,0) -> 0.0, (2,2) -> 0.0)
        |First player fingers: (2,4).  Second player fingers: (2,4) -> Map((2,1) -> 0.0, (1,0) -> 0.85737497, (2,2) -> 0.0, (1,1) -> 0.77350944, (1,2) -> 0.0)
        |First player fingers: (3,3).  Second player fingers: (3,0) -> Map((2,1) -> 1.0, (2,2) -> -0.8573169, (1,1) -> 0.999936, (1,2) -> -0.6858861)
        |First player fingers: (2,4).  Second player fingers: (0,4) -> Map((2,1) -> 0.9499388, (1,0) -> 0.0, (2,2) -> 0.95)
        |First player fingers: (0,3).  Second player fingers: (0,4) -> Map((2,1) -> -0.904704, (2,2) -> 1.0)
        |First player fingers: (1,2).  Second player fingers: (3,1) -> Map((2,1) -> 0.7737809, (1,0) -> 0.0, (2,2) -> 0.7737809, (1,1) -> 0.85737497, (1,2) -> -0.81450623)
        |First player fingers: (3,2).  Second player fingers: (4,1) -> Map((2,1) -> 0.0, (2,2) -> 0.85737497, (1,1) -> 0.0, (1,2) -> 0.6983372)
        |First player fingers: (2,4).  Second player fingers: (1,2) -> Map((2,1) -> 0.658464, (2,2) -> 0.0, (1,1) -> 0.0, (1,2) -> 0.0)
        |First player fingers: (2,1).  Second player fingers: (3,0) -> Map((1,1) -> -0.7350918, (1,2) -> 0.8497368)""")) {
      qtable.getFirstNEntriesWithNon0Actions(10)
    }
  }

  test("Learning (long)") {
    assertResult(strip(
      """First player fingers: (3,2).  Second player fingers: (1,4) -> Map((2,1) -> 0.0, (2,2) -> 0.6983372, (1,1) -> 0.0, (1,2) -> 0.85737497)
        |First player fingers: (2,4).  Second player fingers: (0,2) -> Map((2,1) -> 0.95, (1,0) -> 0.0, (2,2) -> 0.0)
        |First player fingers: (2,4).  Second player fingers: (2,4) -> Map((2,1) -> 0.0, (1,0) -> 0.85737497, (2,2) -> 0.0, (1,1) -> 0.7737809, (1,2) -> 0.0)
        |First player fingers: (3,3).  Second player fingers: (3,0) -> Map((2,1) -> 0.9999999, (2,2) -> -0.8568404, (1,1) -> 1.0, (1,2) -> -0.8570482)
        |First player fingers: (2,4).  Second player fingers: (0,4) -> Map((2,1) -> 0.95, (1,0) -> 0.0, (2,2) -> 0.95)
        |First player fingers: (0,3).  Second player fingers: (0,4) -> Map((2,1) -> -0.95, (2,2) -> 1.0)
        |First player fingers: (1,2).  Second player fingers: (3,1) -> Map((2,1) -> 0.7737809, (1,0) -> 0.0, (2,2) -> 0.7737809, (1,1) -> 0.85737497, (1,2) -> -0.81450623)
        |First player fingers: (3,2).  Second player fingers: (4,1) -> Map((2,1) -> 0.0, (2,2) -> 0.85737497, (1,1) -> 0.0, (1,2) -> 0.6983372)
        |First player fingers: (2,4).  Second player fingers: (1,2) -> Map((2,1) -> 0.72526544, (2,2) -> 0.0, (1,1) -> 0.0, (1,2) -> 0.0)
        |First player fingers: (2,1).  Second player fingers: (3,0) -> Map((1,1) -> -0.7350918, (1,2) -> 0.94999987)""")) {
      qtable.getFirstNEntriesWithNon0Actions(10)
    }

    assertResult(strip("""numEntries=1094""")) {
      qtable.toString
    }
  }

  test("check actions for normal move") {
    assertResult("(2,1) -> 0.0, (2,2) -> 0.0, (1,1) -> 0.0, (1,2) -> 0.0") {
      qtable.getActions(ChopsticksState((1, 1), (2, 2))).mkString(", ")
    }
  }

  test("check actions after player disables opponent hand") {
    assertResult("(2,1) -> -0.95, (1,0) -> 0.0, (2,2) -> 0.0") {
      qtable.getActions(ChopsticksState((0, 2), (2, 3))).mkString(", ")
    }
  }

  test("check actions when disabling a hand is possible") {
    assertResult("(1,0) -> 0.0, (1,1) -> -0.95, (1,2) -> -0.85737497") {
      qtable.getActions(ChopsticksState((2, 0), (1, 4))).mkString(", ")
    }
  }
}
