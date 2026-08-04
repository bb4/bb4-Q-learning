package com.barrybecker4.qlearning.frozenlake

import com.barrybecker4.qlearning.common.{Evaluator, QTable}
import LakeEvaluator.MAX_MOVES


object LakeEvaluator:
  /** give up if not solved in this many moves */
  val MAX_MOVES = 1000


/** @param table the qtable to evaluate the performance of
  * @param numRuns the number of test trials. The more the better the accuracy
  * @param maxMoves max moves before timing out and declaring unsuccessful.
  */
class LakeEvaluator(table: QTable[Direction], numRuns: Int, maxMoves: Int = MAX_MOVES)
    extends Evaluator[Direction]:

  private var report: Option[LakeEvaluationReport] = None

  def evaluate(): Double =
    val r = LakeEvaluationReport.run(() => solve(verbose = false), numRuns)
    report = Some(r)
    r.getError

  /** Call after evaluate is called to get more info. */
  def getReport: String =
    report.map(_.toString).getOrElse:
      throw new IllegalStateException("evaluate() must be called before getReport")

  /** @return the final game state when no moves left, and the number of steps it took to get there */
  def solve(verbose: Boolean = true): (LakeState, Int) =
    var state: LakeState = table.initialState match
      case ls: LakeState => ls
      case other => throw new IllegalStateException(s"Expected LakeState, got ${other.getClass.getName}")
    var ct = 0

    while state.hasTransitions && ct < maxMoves do
      if verbose then
        println(s"------------  step: $ct")
        println(state.toString)
      state = state.makeTransition(table.getBestMove(state)._1)
      ct += 1
    (state, ct)
