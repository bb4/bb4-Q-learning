package com.barrybecker4.qlearning.common

import org.scalatest.funsuite.AnyFunSuite

import scala.collection.mutable
import scala.util.Random

class QTableUpdateSuite extends AnyFunSuite {

  private enum FakeAction:
    case Go, Alt

  private case class FakeState(
      id: String,
      reward: Float,
      maximizing: Boolean,
      legal: Seq[FakeAction],
      transition: FakeAction => FakeState
  ) extends State[FakeAction]:

    def hasTransitions: Boolean = legal.nonEmpty

    def makeTransition(transition: FakeAction): FakeState = this.transition(transition)

    def getLegalTransitions: Seq[FakeAction] = legal

    def rewardForLastMove: Float = reward

    def selectBestAction(actionList: Seq[(FakeAction, Float)], rnd: Random): (FakeAction, Float) =
      ActionSelection.pickBest(actionList, maximize = maximizing, rnd)

  test("update applies TD rule on terminal next state") {
    val terminal = FakeState("t", reward = 1.0f, maximizing = true, legal = Seq.empty,
      _ => throw new IllegalStateException("terminal state"))
    val start = FakeState("s", reward = 0f, maximizing = true, legal = Seq(FakeAction.Go),
      _ => terminal)

    val table = mutable.Map[FakeAction, Float](FakeAction.Go -> 0.0f)
    val q = new QTable(start, Some(Map(start -> table, terminal -> mutable.Map.empty)), rnd = new Random(0))

    q.update(start, (FakeAction.Go, 0.0f), terminal, learningRate = 0.5f, futureRewardDiscount = 1.0f)

    assertResult(0.5f)(q.getActions(start)(FakeAction.Go))
  }

  test("update bootstraps from best value at non-terminal next state") {
    val next = FakeState("next", reward = 0f, maximizing = true, legal = Seq(FakeAction.Go, FakeAction.Alt),
      _ => throw new IllegalStateException("unreachable in test"))
    val start = FakeState("s", reward = 0f, maximizing = true, legal = Seq(FakeAction.Go),
      _ => next)

    val nextActions = mutable.Map(FakeAction.Go -> 0.8f, FakeAction.Alt -> 0.2f)
    val startActions = mutable.Map(FakeAction.Go -> 0.0f)
    val q = new QTable(start, Some(Map(start -> startActions, next -> nextActions)), rnd = new Random(0))

    q.update(start, (FakeAction.Go, 0.0f), next, learningRate = 1.0f, futureRewardDiscount = 0.5f)

    assertResult(0.4f)(q.getActions(start)(FakeAction.Go))
  }

  test("update uses minimizing perspective at next state when appropriate") {
    val next = FakeState("next", reward = 0f, maximizing = false, legal = Seq(FakeAction.Go, FakeAction.Alt),
      _ => throw new IllegalStateException("unreachable in test"))
    val start = FakeState("s", reward = 0f, maximizing = true, legal = Seq(FakeAction.Go),
      _ => next)

    val nextActions = mutable.Map(FakeAction.Go -> 0.9f, FakeAction.Alt -> 0.1f)
    val startActions = mutable.Map(FakeAction.Go -> 0.0f)
    val q = new QTable(start, Some(Map(start -> startActions, next -> nextActions)), rnd = new Random(0))

    q.update(start, (FakeAction.Go, 0.0f), next, learningRate = 1.0f, futureRewardDiscount = 1.0f)

    assertResult(0.1f)(q.getActions(start)(FakeAction.Go))
  }

  test("effectiveEpsilon is high early and approaches floor epsilon") {
    assertResult(1.1)(QTable.effectiveEpsilon(0.1, 0))
    assert((QTable.effectiveEpsilon(0.1, 10_000) - 0.1).abs < 0.001)
  }

  test("getNextAction requires at least one action in the Q-table") {
    val dead = FakeState("dead", reward = 0f, maximizing = true, legal = Seq.empty,
      _ => throw new IllegalStateException("unreachable in test"))
    val q = new QTable(dead, Some(Map(dead -> mutable.Map.empty[FakeAction, Float])), rnd = new Random(0))

    intercept[IllegalArgumentException] {
      q.getNextAction(dead, episodeNumber = 0)
    }
  }
}
