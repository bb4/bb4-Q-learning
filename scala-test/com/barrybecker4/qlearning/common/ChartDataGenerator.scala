package com.barrybecker4.qlearning.common

object ChartDataGenerator {

  /**
    * Prints a table of data that can be imported into Plotly to show a 3D surface graph.
    * @param evaluator a function that returns fitness given q-learning parameters.
    */
  def createLearningRateByNumRunsData(epsSeq: Seq[Float], numRunsSeq: Seq[Int],
                                      learningRateSeq: Seq[Float], futureRewardDiscountSeq: Seq[Float],
                                      evaluator: (Double, Int, Float, Float) => Double): Unit = {
    var zmap = createZmap(epsSeq, numRunsSeq, learningRateSeq, futureRewardDiscountSeq, evaluator)

    // generate a csv file contents that can be imported into Plotly to show a surface
    val headers = Seq("learningRate, numRows") ++ (for (lr <- learningRateSeq) yield s"z[${lr.toFloat}]")

    for (eps <- epsSeq) {
      for (frd <- futureRewardDiscountSeq) {
        println(s"eps = $eps  futureRewardDiscount = $frd")
        println(headers.mkString(", "))
        for (idx <- 0 until Math.max(numRunsSeq.length, learningRateSeq.length)) {
          val learningRateValue = if (idx < learningRateSeq.length) learningRateSeq(idx) else ""
          val numRowsValue = if (idx < numRunsSeq.length) numRunsSeq(idx) else ""
          print(s"$learningRateValue, $numRowsValue, ")
          if (idx < numRunsSeq.length) {
            val vals = for (lr <- learningRateSeq) yield zmap((eps, frd, lr, numRunsSeq(idx)))
            println(vals.mkString(", "))
          }
          else println
        }
        println
      }
    }
  }

  /**
    * Prints a table of data that can be imported into Plotly to show a 3D surface graph.
    * @param evaluator a function that returns fitness given q-learning parameters.
    */
  def createEpsByNumRunsData(epsSeq: Seq[Float], numRunsSeq: Seq[Int],
                                     learningRateSeq: Seq[Float], futureRewardDiscountSeq: Seq[Float],
                                     evaluator: (Double, Int, Float, Float) => Double): Unit = {
    var zmap = createZmap(epsSeq, numRunsSeq, learningRateSeq, futureRewardDiscountSeq, evaluator)

    // generate a csv file contents that can be imported into Plotly to show a surface
    val headers = Seq("eps, numRows") ++ (for (eps <- epsSeq) yield s"z[${eps.toFloat}]")

    for (learningRate <- learningRateSeq) {
      for (frd <- futureRewardDiscountSeq) {
        println(s"learningRate = $learningRate futureRewardDiscount = $frd")
        println(headers.mkString(", "))
        for (idx <- 0 until Math.max(numRunsSeq.length, epsSeq.length)) {
          val epsValue = if (idx < epsSeq.length) epsSeq(idx) else ""
          val numRowsValue = if (idx < numRunsSeq.length) numRunsSeq(idx) else ""
          print(s"epsValue, numRowsValue, ")
          if (idx < numRunsSeq.length) {
            val vals = for (eps <- epsSeq) yield zmap((eps, frd, learningRate, numRunsSeq(idx)))
            println(vals.mkString(", "))
          }
          else println
        }
        println
      }
    }
  }

  private def createZmap(epsSeq: Seq[Float], numRunsSeq: Seq[Int],
                         learningRateSeq: Seq[Float], futureRewardDiscountSeq: Seq[Float],
                         evaluator: (Double, Int, Float, Float) => Double): Map[(Float, Float, Float, Int), Double] = {
    var zmap = Map[(Float, Float, Float, Int), Double]()

    for (eps <- epsSeq) {
      for (frd <- futureRewardDiscountSeq) {
        for (lr <- learningRateSeq) {
          for (numRuns <- numRunsSeq) {
            zmap += (eps, frd, lr, numRuns) -> (1.0 - evaluator(eps, numRuns, lr, frd))
          }
        }
      }
    }
    zmap
  }

}
