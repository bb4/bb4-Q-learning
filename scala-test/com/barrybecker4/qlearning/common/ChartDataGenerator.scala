package com.barrybecker4.qlearning.common

object ChartDataGenerator {

  /**
    * Prints a table of data that can be imported into Plotly to show a 3D surface graph.
    * @param evaluator a function that returns fitness given q-learning parameters.
    */
  def createLearningRateByNumRunsData(epsSeq: Seq[Float], numRunsSeq: Seq[Int],
                                      learningRateSeq: Seq[Float], futureRewardDiscountSeq: Seq[Float],
                                      evaluator: (Double, Int, Float, Float) => Double): Unit = {
    val zmap = createZmap(epsSeq, numRunsSeq, learningRateSeq, futureRewardDiscountSeq, evaluator)

    for (eps <- epsSeq; frd <- futureRewardDiscountSeq) {
      val headers = Seq("learningRate, numRows") ++ (for (lr <- learningRateSeq) yield s"z[${lr.toFloat}]")
      println(s"eps = $eps  futureRewardDiscount = $frd")
      println(headers.mkString(", "))
      printGridByLearningRate(zmap, eps, frd, numRunsSeq, learningRateSeq)
      println()
    }
  }

  /**
    * Prints a table of data that can be imported into Plotly to show a 3D surface graph.
    * @param evaluator a function that returns fitness given q-learning parameters.
    */
  def createEpsByNumRunsData(epsSeq: Seq[Float], numRunsSeq: Seq[Int],
                             learningRateSeq: Seq[Float], futureRewardDiscountSeq: Seq[Float],
                             evaluator: (Double, Int, Float, Float) => Double): Unit = {
    val zmap = createZmap(epsSeq, numRunsSeq, learningRateSeq, futureRewardDiscountSeq, evaluator)

    for (learningRate <- learningRateSeq; frd <- futureRewardDiscountSeq) {
      val headers = Seq("eps, numRows") ++ (for (eps <- epsSeq) yield s"z[${eps.toFloat}]")
      println(s"learningRate = $learningRate futureRewardDiscount = $frd")
      println(headers.mkString(", "))
      printGridByEps(zmap, epsSeq, frd, learningRate, numRunsSeq)
      println()
    }
  }

  /** One row of the learning-rate × num-runs grid. */
  private def printGridByLearningRate(
      zmap: Map[(Float, Float, Float, Int), Double],
      eps: Float,
      frd: Float,
      numRunsSeq: Seq[Int],
      learningRateSeq: Seq[Float]): Unit =
    for (idx <- 0 until Math.max(numRunsSeq.length, learningRateSeq.length)) {
      val learningRateValue = if (idx < learningRateSeq.length) learningRateSeq(idx) else ""
      val numRowsValue = if (idx < numRunsSeq.length) numRunsSeq(idx) else ""
      print(s"$learningRateValue, $numRowsValue, ")
      if (idx < numRunsSeq.length) {
        val vals = for (lr <- learningRateSeq) yield zmap((eps, frd, lr, numRunsSeq(idx)))
        println(vals.mkString(", "))
      } else println()
    }

  /** One row of the eps × num-runs grid. */
  private def printGridByEps(
      zmap: Map[(Float, Float, Float, Int), Double],
      epsSeq: Seq[Float],
      frd: Float,
      learningRate: Float,
      numRunsSeq: Seq[Int]): Unit =
    for (idx <- 0 until Math.max(numRunsSeq.length, epsSeq.length)) {
      val epsValue = if (idx < epsSeq.length) epsSeq(idx) else ""
      val numRowsValue = if (idx < numRunsSeq.length) numRunsSeq(idx) else ""
      print(s"$epsValue, $numRowsValue, ")
      if (idx < numRunsSeq.length) {
        val vals = for (eps <- epsSeq) yield zmap((eps, frd, learningRate, numRunsSeq(idx)))
        println(vals.mkString(", "))
      } else println()
    }

  private def createZmap(epsSeq: Seq[Float], numRunsSeq: Seq[Int],
                         learningRateSeq: Seq[Float], futureRewardDiscountSeq: Seq[Float],
                         evaluator: (Double, Int, Float, Float) => Double): Map[(Float, Float, Float, Int), Double] =
    (for {
      eps <- epsSeq
      frd <- futureRewardDiscountSeq
      lr <- learningRateSeq
      numRuns <- numRunsSeq
    } yield (eps, frd, lr, numRuns) -> (1.0 - evaluator(eps, numRuns, lr, frd))).toMap
}
