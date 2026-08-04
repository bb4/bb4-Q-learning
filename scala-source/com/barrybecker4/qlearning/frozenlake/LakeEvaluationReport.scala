package com.barrybecker4.qlearning.frozenlake

import scala.collection.mutable

/** Outcome counts from a batch of frozen-lake evaluation runs. */
private[frozenlake] case class LakeEvaluationReport(
    successes: (Int, Int),
    deaths: (Int, Int),
    timeouts: (Int, Int),
    numRuns: Int
):
  /** Fraction of runs that did not reach the goal. */
  def getError: Double = 1.0 - successes._1 / numRuns.toDouble

  override def toString: String =
    s"Error = $getError \n" +
      Seq(
        ("successes", successes),
        ("deaths", deaths),
        ("timeouts", timeouts)
      ).map { case (name, (num, steps)) =>
        val avg = if num == 0 then 0 else steps / num
        s"Num $name = $num  average steps = $avg"
      }.mkString("\n")

private[frozenlake] object LakeEvaluationReport:
  /** Run `solve` `numRuns` times and aggregate successes, deaths, and timeouts. */
  def run(solve: () => (LakeState, Int), numRuns: Int): LakeEvaluationReport =
    val map = mutable.Map(
      "deaths" -> (0, 0),
      "successes" -> (0, 0),
      "timeouts" -> (0, 0)
    )
    for _ <- 0 until numRuns do
      val (finalState, numSteps) = solve()
      val endState =
        if finalState.isGoalReached then "successes"
        else if finalState.isInHole then "deaths"
        else "timeouts"
      val value = map(endState)
      map(endState) = (value._1 + 1, value._2 + numSteps)

    LakeEvaluationReport(map("successes"), map("deaths"), map("timeouts"), numRuns)
