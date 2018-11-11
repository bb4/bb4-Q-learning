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
      """Player to move: (3,2). Opposing player: (3,0) -> Map((2,1) -> 0.0, (2,2) -> 0.0, (1,1) -> 0.0, (1,2) -> 0.7077889)
        |Player to move: (2,3). Opposing player: (4,1) -> Map((2,1) -> 0.8, (2,2) -> 0.0, (1,1) -> 0.0, (1,2) -> 0.0)
        |Player to move: (0,3). Opposing player: (0,4) -> Map((2,1) -> 1.0, (2,2) -> 0.0)
        |Player to move: (1,3). Opposing player: (1,2) -> Map((2,1) -> 0.512, (1,0) -> 1.0, (2,2) -> 0.0, (1,1) -> 0.99328, (1,2) -> 0.0)
        |Player to move: (4,1). Opposing player: (1,2) -> Map((2,1) -> 0.0, (2,2) -> 0.0, (1,1) -> 0.0, (1,2) -> 0.99995226)
        |Player to move: (3,2). Opposing player: (2,3) -> Map((2,1) -> 0.0, (2,2) -> 0.0, (1,1) -> 0.9914905, (1,2) -> 0.0)
        |Player to move: (3,4). Opposing player: (0,2) -> Map((2,1) -> 0.0, (2,2) -> 0.0, (1,1) -> 0.0, (1,2) -> 0.96000004)
        |Player to move: (0,1). Opposing player: (4,4) -> Map((2,1) -> 0.0, (2,2) -> 0.9999997)
        |Player to move: (4,2). Opposing player: (0,4) -> Map((2,1) -> 0.0, (1,0) -> 0.0, (2,2) -> 0.992, (1,1) -> 0.0, (1,2) -> 0.0)
        |Player to move: (4,0). Opposing player: (3,1) -> Map((1,0) -> 0.0, (1,1) -> 0.0, (1,2) -> 0.8)""")) {
      qtable.getFirstNEntriesWithNon0Actions(10)
    }
  }

  test("Learning short, epsilon - 0.1") {
    val learner = new QLearner[(Byte, Byte)](learningRate = 0.8f, futureRewardDiscount = 0.95f)
    val qtable = new QTable(ChopsticksState(), None, epsilon = 0.1, new Random(0))

    learner.learn(qtable, 20000)

    assertResult(strip(
      """Player to move: (4,3). Opposing player: (1,1) -> Map((2,1) -> 0.0, (2,2) -> 0.0, (1,1) -> 0.0, (1,2) -> 0.4142247)
        |Player to move: (1,0). Opposing player: (0,2) -> Map((1,1) -> 0.6859, (1,2) -> 0.90248585)
        |Player to move: (3,2). Opposing player: (3,0) -> Map((2,1) -> 1.0, (2,2) -> 0.0, (1,1) -> 0.0, (1,2) -> 0.0)
        |Player to move: (2,0). Opposing player: (3,1) -> Map((1,0) -> 0.529172, (1,1) -> 0.0, (1,2) -> 0.0)
        |Player to move: (2,3). Opposing player: (4,1) -> Map((2,1) -> 0.0, (2,2) -> 0.6810098, (1,1) -> 0.0, (1,2) -> 0.0)
        |Player to move: (0,3). Opposing player: (0,4) -> Map((2,1) -> 0.0, (2,2) -> 0.8)
        |Player to move: (4,0). Opposing player: (3,4) -> Map((1,0) -> 0.0, (1,1) -> 0.0, (1,2) -> 0.94999987)
        |Player to move: (0,1). Opposing player: (0,4) -> Map((2,1) -> 0.912, (2,2) -> 0.0)
        |Player to move: (1,3). Opposing player: (1,2) -> Map((2,1) -> 0.85737497, (1,0) -> 0.806687, (2,2) -> 0.7922839, (1,1) -> 0.8142455, (1,2) -> 0.18499193)
        |Player to move: (4,1). Opposing player: (1,2) -> Map((2,1) -> 0.0, (2,2) -> 0.0, (1,1) -> 0.9024884, (1,2) -> 0.722)""")) {
      qtable.getFirstNEntriesWithNon0Actions(10)
    }
  }

  test("Learning short, epsilon - 0.2") {
    val learner = new QLearner[(Byte, Byte)](learningRate = 0.8f, futureRewardDiscount = 0.95f)
    val qtable = new QTable(ChopsticksState(), None, epsilon = 0.2, new Random(0))  // 0.2 is too big

    learner.learn(qtable, 20000)

    assertResult(strip(
      """Player to move: (4,3). Opposing player: (1,1) -> Map((2,1) -> 0.0, (2,2) -> 0.85600066, (1,1) -> 0.0, (1,2) -> 0.0)
        |Player to move: (1,0). Opposing player: (0,2) -> Map((1,1) -> 0.8571865, (1,2) -> 0.9025)
        |Player to move: (3,2). Opposing player: (3,0) -> Map((2,1) -> 0.9999872, (2,2) -> 0.6859, (1,1) -> 0.0, (1,2) -> 0.8573194)
        |Player to move: (2,0). Opposing player: (3,1) -> Map((1,0) -> 0.81450623, (1,1) -> 0.68575543, (1,2) -> 0.54872)
        |Player to move: (3,0). Opposing player: (4,4) -> Map((1,1) -> 0.9496862, (1,2) -> 0.0)
        |Player to move: (2,3). Opposing player: (4,1) -> Map((2,1) -> 0.89528, (2,2) -> 0.0, (1,1) -> 0.0, (1,2) -> 0.0)
        |Player to move: (0,3). Opposing player: (0,4) -> Map((2,1) -> 0.0, (2,2) -> 0.99968)
        |Player to move: (4,0). Opposing player: (3,4) -> Map((1,0) -> 0.8573016, (1,1) -> 0.94167036, (1,2) -> 0.95)
        |Player to move: (0,1). Opposing player: (0,4) -> Map((2,1) -> 0.95, (2,2) -> 0.0)
        |Player to move: (1,3). Opposing player: (1,2) -> Map((2,1) -> 0.85737497, (1,0) -> 0.81450623, (2,2) -> 0.81450623, (1,1) -> 0.81450623, (1,2) -> 0.7737809)""")) {
      qtable.getFirstNEntriesWithNon0Actions(10)
    }
  }

  test("Learning (long)") {
    assertResult(strip(
      """Player to move: (4,3). Opposing player: (1,1) -> Map((2,1) -> 0.0, (2,2) -> 0.0, (1,1) -> 0.0, (1,2) -> 0.4142247)
        |Player to move: (1,0). Opposing player: (0,2) -> Map((1,1) -> 0.85600317, (1,2) -> 0.9025)
        |Player to move: (3,2). Opposing player: (3,0) -> Map((2,1) -> 1.0, (2,2) -> 0.6859, (1,1) -> 0.0, (1,2) -> 0.6859)
        |Player to move: (2,0). Opposing player: (3,1) -> Map((1,0) -> 0.7737802, (1,1) -> 0.0, (1,2) -> 0.0)
        |Player to move: (3,0). Opposing player: (4,4) -> Map((1,1) -> 0.0, (1,2) -> 0.76)
        |Player to move: (2,3). Opposing player: (4,1) -> Map((2,1) -> 0.0, (2,2) -> 0.79889417, (1,1) -> 0.0, (1,2) -> 0.0)
        |Player to move: (0,3). Opposing player: (0,4) -> Map((2,1) -> 0.758784, (2,2) -> 1.0)
        |Player to move: (4,0). Opposing player: (3,4) -> Map((1,0) -> 0.6859, (1,1) -> 0.0, (1,2) -> 0.95)
        |Player to move: (0,1). Opposing player: (0,4) -> Map((2,1) -> 0.95, (2,2) -> 0.0)
        |Player to move: (1,3). Opposing player: (1,2) -> Map((2,1) -> 0.85737497, (1,0) -> 0.81450623, (2,2) -> 0.81450623, (1,1) -> 0.81450623, (1,2) -> 0.81450623)""")) {
      qtable.getFirstNEntriesWithNon0Actions(10)
    }

    assertResult(strip("""numEntries=580""")) {
      qtable.toString
    }
  }

  test("check actions for normal move") {
    assertResult("(2,1) -> 0.0, (1,0) -> 0.0, (2,2) -> 0.0, (1,1) -> 0.0, (1,2) -> 0.49322638") {
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
