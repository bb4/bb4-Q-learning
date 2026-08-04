package com.barrybecker4.qlearning.common


/**
  * Evaluates by taking the root mean squared error between this table and a provided gold standard.
  */
class RmsEvaluator[T](table: QTable[T], goldStandard: QTable[T]) extends Evaluator[T]:

  def evaluate(): Double =
    val (sumsq, ct) =
      table.table.iterator.flatMap { case (state, actions) =>
        actions.keys.iterator.map { action =>
          val v: Double = actions(action) - goldStandard.table(state)(action)
          v * v
        }
      }.foldLeft((0.0, 0)) { case ((sum, n), sq) => (sum + sq, n + 1) }

    if ct == 0 then 0.0
    else Math.sqrt(sumsq / ct)
