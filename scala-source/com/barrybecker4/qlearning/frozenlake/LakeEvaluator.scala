package com.barrybecker4.qlearning.frozenlake

import com.barrybecker4.qlearning.common.{Evaluator, QTable}

import scala.collection.mutable
import LakeEvaluator.MAX_MOVES
import com.barrybecker4.qlearning.frozenlake.Direction.Direction


object LakeEvaluator {
  /** give up if not solved in this many moves */
  val MAX_MOVES = 1000
}


/** @param table the qtable to evaluate performance of
  * @param numRuns the number of test trials. The more the better the accuracy
  * @param maxMoves max moves before timing out and declaring unsuccessful.
  */
class LakeEvaluator(table: QTable[Direction], numRuns: Int, maxMoves: Int = MAX_MOVES) extends Evaluator[Direction] {

  var report: Report = _

  class Report {
    val map = mutable.Map("deaths" -> (0, 0), "successes" -> (0, 0), "timeouts" -> (0, 0))

    for (i <- 0 until numRuns) {
      val (finalState, numSteps) = solve(verbose = false)
      val endState = if (finalState.isGoalReached) "successes"
      else if (finalState.isInHole) "deaths"
      else "timeouts"

      val value = map(endState)
      map(endState) = (value._1 + 1, value._2 + numSteps)
    }

    def getError: Double = 1.0 - map("successes")._1 / numRuns.toDouble

    override def toString: String = {
      s"Error = $getError \n" +
      Seq("successes", "deaths", "timeouts").map( v => {
        val num = map(v)._1
        val avg = if (num == 0) 0 else map(v)._2 / num
        s"Num $v = $num  average steps = $avg"
      }).mkString("\n")
    }
  }

  def evaluate(): Double = {
    report = new Report
    report.getError
  }

  /** call after evaluate is called to get more info */
  def getReport: String = report.toString

  /** @return the final game state when no moves left, and the number of steps it took to get there */
  def solve(verbose: Boolean = true): (LakeState, Int) = {
    var state: LakeState = table.initialState.asInstanceOf[LakeState]
    var ct = 0

    while (state.hasTransitions && ct < maxMoves) {
      if (verbose) {
        println(s"------------  step: $ct")
        println(state.toString)
      }
      state = state.makeTransition(table.getBestMove(state)._1)
      ct += 1
    }
    (state, ct)
  }
}
