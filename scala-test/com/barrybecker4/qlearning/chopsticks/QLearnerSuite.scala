package com.barrybecker4.qlearning.chopsticks

import com.barrybecker4.qlearning.TestHelper.strip
import com.barrybecker4.qlearning.common.{QLearner, QTable}
import org.scalatest.funsuite.AnyFunSuite
import scala.util.Random


class QLearnerSuite extends AnyFunSuite {

  val learner = new QLearner[ChopsticksAction](learningRate = 0.8f, futureRewardDiscount = 0.95f)
  val qtable = new QTable(ChopsticksState(), None, epsilon = 0.1, new Random(0))
  learner.learn(qtable, 100000)

  test("Learning short, epsilon - 0.01") {
    val learner = new QLearner[ChopsticksAction]()
    val qtable = new QTable(ChopsticksState(), None, epsilon = 0.01, new Random(0))    // 0.01 likely too small

    learner.learn(qtable, 20000)

    assertResult(strip(
      """First player fingers: (4,4).  Second player fingers: (1,4) -> HashMap(Tap(2,2) -> 1.0, Tap(1,2) -> 0.0, Tap(2,1) -> 0.0, Tap(1,1) -> 0.0)
        |First player fingers: (1,4).  Second player fingers: (2,0) -> HashMap(Tap(2,1) -> 1.0, Tap(1,1) -> -0.95954144)
        |First player fingers: (3,3).  Second player fingers: (1,0) -> HashMap(Tap(1,2) -> 1.0, Tap(1,1) -> 1.0)
        |First player fingers: (1,4).  Second player fingers: (2,0) -> HashMap(Split -> 0.0, Tap(1,2) -> 1.0, Tap(1,1) -> 1.0)
        |First player fingers: (3,2).  Second player fingers: (1,0) -> HashMap(Tap(1,2) -> 0.0, Tap(1,1) -> 1.0)
        |First player fingers: (0,3).  Second player fingers: (0,3) -> HashMap(Tap(2,2) -> 1.0)
        |First player fingers: (2,3).  Second player fingers: (4,1) -> HashMap(Tap(2,2) -> 0.0, Tap(1,2) -> 0.0, Tap(2,1) -> 1.0, Tap(1,1) -> 1.0)
        |First player fingers: (0,1).  Second player fingers: (1,2) -> HashMap(Tap(2,2) -> 1.0, Tap(1,2) -> 0.0)
        |First player fingers: (3,4).  Second player fingers: (2,0) -> HashMap(Split -> 0.0, Tap(1,2) -> 1.0, Tap(1,1) -> 1.0)
        |First player fingers: (3,1).  Second player fingers: (2,1) -> HashMap(Tap(2,2) -> 0.0, Split -> 0.0, Tap(1,2) -> -0.7935164, Tap(2,1) -> 0.0, Tap(1,1) -> 1.0)""")) {
      qtable.getFirstNEntriesWithNon0Actions(10)
    }
  }

  test("Learning short, epsilon - 0.1") {
    val learner = new QLearner[ChopsticksAction](learningRate = 0.8f, futureRewardDiscount = 0.95f)
    val qtable = new QTable(ChopsticksState(), None, epsilon = 0.1, new Random(0))

    learner.learn(qtable, 20000)

    assertResult(strip(
      """First player fingers: (4,4).  Second player fingers: (1,4) -> HashMap(Tap(2,2) -> 0.9025, Tap(1,2) -> 0.722, Tap(2,1) -> 0.44185933, Tap(1,1) -> 0.0)
        |First player fingers: (1,4).  Second player fingers: (2,0) -> HashMap(Tap(2,1) -> 1.0, Tap(1,1) -> -0.85051596)
        |First player fingers: (3,3).  Second player fingers: (1,0) -> HashMap(Tap(1,2) -> 0.95, Tap(1,1) -> 0.95)
        |First player fingers: (1,4).  Second player fingers: (2,0) -> HashMap(Split -> 0.0, Tap(1,2) -> 0.85737497, Tap(1,1) -> 0.95)
        |First player fingers: (3,2).  Second player fingers: (1,0) -> HashMap(Tap(1,2) -> 0.0, Tap(1,1) -> 0.95)
        |First player fingers: (3,3).  Second player fingers: (2,1) -> HashMap(Tap(2,2) -> 0.0, Tap(1,2) -> 0.0, Tap(2,1) -> 0.0, Tap(1,1) -> 0.6792252)
        |First player fingers: (0,3).  Second player fingers: (0,3) -> HashMap(Tap(2,2) -> 1.0)
        |First player fingers: (2,3).  Second player fingers: (4,1) -> HashMap(Tap(2,2) -> 0.0, Tap(1,2) -> 0.0, Tap(2,1) -> 0.85737497, Tap(1,1) -> 0.85737497)
        |First player fingers: (0,1).  Second player fingers: (1,2) -> HashMap(Tap(2,2) -> 0.85737497, Tap(1,2) -> 0.0)
        |First player fingers: (3,4).  Second player fingers: (2,0) -> HashMap(Split -> 0.0, Tap(1,2) -> 0.95, Tap(1,1) -> 0.95)""")) {
      qtable.getFirstNEntriesWithNon0Actions(10)
    }
  }

  test("Learning short, epsilon - 0.2") {
    val learner = new QLearner[ChopsticksAction](learningRate = 0.8f, futureRewardDiscount = 0.95f)
    val qtable = new QTable(ChopsticksState(), None, epsilon = 0.2, new Random(0))  // 0.2 is too big

    learner.learn(qtable, 20000)

    assertResult(strip(
      """First player fingers: (4,4).  Second player fingers: (1,4) -> HashMap(Tap(2,2) -> 0.0, Tap(1,2) -> 0.9025, Tap(2,1) -> 0.0, Tap(1,1) -> 0.5529853)
        |First player fingers: (1,4).  Second player fingers: (2,0) -> HashMap(Tap(2,1) -> 1.0, Tap(1,1) -> -0.8573749)
        |First player fingers: (3,3).  Second player fingers: (1,0) -> HashMap(Tap(1,2) -> 0.95, Tap(1,1) -> 0.95)
        |First player fingers: (1,4).  Second player fingers: (2,0) -> HashMap(Split -> 0.0, Tap(1,2) -> 0.85737497, Tap(1,1) -> 0.95)
        |First player fingers: (3,2).  Second player fingers: (1,0) -> HashMap(Tap(1,2) -> 0.0, Tap(1,1) -> 0.95)
        |First player fingers: (3,3).  Second player fingers: (2,1) -> HashMap(Tap(2,2) -> 0.0, Tap(1,2) -> 0.6858999, Tap(2,1) -> 0.0, Tap(1,1) -> 0.6859)
        |First player fingers: (0,3).  Second player fingers: (0,3) -> HashMap(Tap(2,2) -> 1.0)
        |First player fingers: (2,3).  Second player fingers: (4,1) -> HashMap(Tap(2,2) -> 0.0, Tap(1,2) -> 0.0, Tap(2,1) -> 0.85737497, Tap(1,1) -> 0.85737497)
        |First player fingers: (0,1).  Second player fingers: (1,2) -> HashMap(Tap(2,2) -> 0.85737497, Tap(1,2) -> 0.0)
        |First player fingers: (3,4).  Second player fingers: (2,0) -> HashMap(Split -> 0.0, Tap(1,2) -> 0.95, Tap(1,1) -> 0.95)""")) {
      qtable.getFirstNEntriesWithNon0Actions(10)
    }
  }

  test("Learning (long)") {
    assertResult(strip(
      """First player fingers: (4,4).  Second player fingers: (1,4) -> HashMap(Tap(2,2) -> 0.9025, Tap(1,2) -> 0.9022112, Tap(2,1) -> 0.73274595, Tap(1,1) -> 0.72920096)
        |First player fingers: (1,4).  Second player fingers: (2,0) -> HashMap(Tap(2,1) -> 1.0, Tap(1,1) -> -0.85737497)
        |First player fingers: (3,3).  Second player fingers: (1,0) -> HashMap(Tap(1,2) -> 0.95, Tap(1,1) -> 0.95)
        |First player fingers: (1,4).  Second player fingers: (2,0) -> HashMap(Split -> 0.0, Tap(1,2) -> 0.85737497, Tap(1,1) -> 0.95)
        |First player fingers: (3,2).  Second player fingers: (1,0) -> HashMap(Tap(1,2) -> 0.0, Tap(1,1) -> 0.95)
        |First player fingers: (3,3).  Second player fingers: (2,1) -> HashMap(Tap(2,2) -> 0.0, Tap(1,2) -> 0.6859, Tap(2,1) -> 0.0, Tap(1,1) -> 0.82174504)
        |First player fingers: (0,3).  Second player fingers: (0,3) -> HashMap(Tap(2,2) -> 1.0)
        |First player fingers: (2,3).  Second player fingers: (4,1) -> HashMap(Tap(2,2) -> 0.0, Tap(1,2) -> 0.0, Tap(2,1) -> 0.85737497, Tap(1,1) -> 0.85737497)
        |First player fingers: (0,1).  Second player fingers: (1,2) -> HashMap(Tap(2,2) -> 0.85737497, Tap(1,2) -> 0.0)
        |First player fingers: (3,4).  Second player fingers: (2,0) -> HashMap(Split -> 0.0, Tap(1,2) -> 0.95, Tap(1,1) -> 0.95)""")) {
    qtable.getFirstNEntriesWithNon0Actions(10)
    }

    assertResult(strip("""numEntries=948""")) {
      qtable.toString
    }
  }

  test("check actions for normal move") {
    assertResult("Tap(2,2) -> 0.0, Tap(1,2) -> 0.0, Tap(2,1) -> 0.0, Tap(1,1) -> 0.0") {
      qtable.getActions(ChopsticksState((1, 1), (2, 2))).mkString(", ")
    }
  }

  test("check actions after player disables opponent hand") {
    assertResult("Tap(2,2) -> 0.0, Split -> 0.0, Tap(2,1) -> -0.95") {
      qtable.getActions(ChopsticksState((0, 2), (2, 3))).mkString(", ")
    }
  }

  test("check actions when disabling a hand is possible") {
    assertResult("Split -> 0.0, Tap(1,2) -> -0.85737497, Tap(1,1) -> -0.95") {
      qtable.getActions(ChopsticksState((2, 0), (1, 4))).mkString(", ")
    }
  }
}
