package com.barrybecker4.qlearning.common

import co.theasi.plotly.{Plot, draw, writer}
import co.theasi.plotly

import util.Random


object ExamplePlot extends App {

  // Generate uniformly distributed x
  val xs = 0 until 200

  // Generate random y
  val ys = (0 until 200).map { i => i + 5.0 * Random.nextDouble }

  val p = Plot().withScatter(xs, ys)

  // writes the plot to plotly account
  draw(p, "basic-scatter2", writer.FileOptions(overwrite=true))
  // returns  PlotFile(pbugnion:173,basic-scatter)

  /*
  val xs = 0.0 to 2.0 by 0.1
  val ys = xs.map { x => x*x }

  val plot = Plot().withScatter(xs, ys)

  draw(plot, "my-first-plot")
  */
}
