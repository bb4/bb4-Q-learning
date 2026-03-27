package com.barrybecker4.qlearning.common

import scala.util.Random

/** Pick among tied best Q-values (random tie-break). */
object ActionSelection:

  /** @param maximize if true use max Q-value, else min (e.g. opponent to move). */
  def pickBest[T](actionList: Seq[(T, Float)], maximize: Boolean, rnd: Random): (T, Float) =
    if actionList.isEmpty then
      throw new IllegalArgumentException("actionList must be non-empty")
    val bestValue =
      if maximize then actionList.map(_._2).max
      else actionList.map(_._2).min
    val bestActions = actionList.filter(_._2 == bestValue)
    bestActions(rnd.nextInt(bestActions.length))
