package com.barrybecker4.qlearning.common

import org.scalatest.funsuite.AnyFunSuite

import java.io.{ByteArrayOutputStream, PrintStream}
import java.nio.charset.StandardCharsets

class ChartDataGeneratorSuite extends AnyFunSuite {

  test("createEpsByNumRunsData row includes eps and numRuns columns") {
    val buf = new ByteArrayOutputStream()
    Console.withOut(new PrintStream(buf, true, StandardCharsets.UTF_8)) {
      ChartDataGenerator.createEpsByNumRunsData(
        epsSeq = Seq(0.1f, 0.2f),
        numRunsSeq = Seq(100),
        learningRateSeq = Seq(0.3f),
        futureRewardDiscountSeq = Seq(1.0f),
        evaluator = (_, _, _, _) => 0.5
      )
    }
    val s = buf.toString(StandardCharsets.UTF_8)
    assert(s.contains("0.1, 100"))
    // Second grid row has no numRuns when idx exceeds numRunsSeq (by design of the generator)
    assert(s.contains("0.2, "))
  }
}
