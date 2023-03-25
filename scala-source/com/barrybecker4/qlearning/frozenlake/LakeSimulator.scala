package com.barrybecker4.qlearning.frozenlake

import com.barrybecker4.qlearning.common.{QLearner, QTable}
import com.barrybecker4.qlearning.frozenlake.Direction.Direction
import LakeSimulatorConsts._
import scala.util.Random


object LakeSimulator extends App {

  val simulator = new LakeSimulator()
  println("Let's simulate the frozen lake problem.")
  println("Starting from S, try to retrieve the frisbee at G while avoiding holes (H) ")
  println(s"and being mindful of the random wind, " +
    s"which can occasionally blow you in a random direction.\n")
  simulator.simulate()
}

object LakeSimulatorConsts {
  /** give up if not solved in this many moves */
  val MAX_MOVES = 1000
  val DEFAULT_WIND_FEQUENCY = 0.1
  val DEFAULT_EPSILON = 0.3  // exploitation vs exploration
  val DEFAULT_LEARNING_TRIALS = 20000
  val DEFAULT_TEST_TRIALS = 10000
  val DEFAULT_LEARNING_RATE = 0.3f
  val DEFAULT_FUTURE_REWARD_DISCOUNT = 0.7f
  val MAX_TEST_TRIALS = 100000

}

/** Text based lake simulator.
  * First learn, then simulate.
  */
class LakeSimulator {

  private val input = new Input()
  private var table: QTable[Direction] = _
  private var evaluator: LakeEvaluator = _

  def simulate(): Unit = {
    learnHowToSolve()

    if (shouldRunIndividualSims()) {
      runIndividualSims()
    } else {
      runLotsOfSimsAndReport()
    }
  }

  private def shouldRunIndividualSims(): Boolean = {
    val msg = "Do you want to run simulations one at a time (1) or do many and report the results (m)?"
    input.charQuery(msg, Seq('1', 'm')) == '1'
  }

  private def runIndividualSims(): Unit = {
    evaluator = new LakeEvaluator(table, DEFAULT_TEST_TRIALS, MAX_MOVES)
    var runAgain = true
    while (runAgain) {
      val (finalState, numSteps) = evaluator.solve()
      showOutcome(finalState, numSteps)
      runAgain = shouldContinue()
    }
  }

  private def runLotsOfSimsAndReport(): Unit = {
    val numRuns = getNumRuns
    evaluator = new LakeEvaluator(table, numRuns, MAX_MOVES)
    evaluator.evaluate()
    println(evaluator.getReport)
  }

  private def getNumRuns: Int = {
    println(s"How many simulations do you want to run [1 - $MAX_TEST_TRIALS]?")
    input.getNumber(DEFAULT_TEST_TRIALS, 1, MAX_TEST_TRIALS).toInt
  }

  private def shouldContinue(): Boolean = input.charQuery("Try to solve again?", Seq('y', 'n')) == 'y'

  private def learnHowToSolve(windFrequency: Double = DEFAULT_WIND_FEQUENCY,
                              epsilon: Double = DEFAULT_EPSILON,
                              numTrials: Int = DEFAULT_LEARNING_TRIALS): Unit = {

    println("How many learning trials (100 - 1000000)?")
    val numTrials = input.getNumber(DEFAULT_LEARNING_TRIALS, 100, 1000000).toInt

    println("What wind frequency (0 - 1)?")
    val windFrequency = input.getNumber(DEFAULT_WIND_FEQUENCY, 0, 1.0)

    println("What epsilon (0 - 1)? Higher value leads to more exploration, rather than exploitation.")
    val epsilon = input.getNumber(DEFAULT_EPSILON, 0, 1.0)

    println("What learning rate (0 - 1]? Higher value should be used if less chance involved.")
    val learningRate = input.getNumber(DEFAULT_LEARNING_RATE, 0.0, 1.0)

    println("What future reward discount (0 - 1]? Higher value should be used if less chance involved.")
    val frDiscount: Float = input.getNumber(DEFAULT_FUTURE_REWARD_DISCOUNT, 0.0, 1.0).toFloat

    val lake = Lake(windFrequency = windFrequency)
    table = new QTable[Direction](new LakeState(lake), Some(lake.initialTable()), epsilon = epsilon, new Random(1L))
    val learner = new QLearner[Direction](learningRate = DEFAULT_LEARNING_RATE, futureRewardDiscount = frDiscount)

    print("Learning...")
    learner.learn(table, numTrials)
    println("...I just learned how to solve.\n")
  }

  private def showOutcome(finalState: LakeState, numSteps: Int): Unit = {
    println("\n" + finalState.toString)
    if (finalState.isGoalReached)
      println(s"*** Congratulations - you found the frisbee after $numSteps steps! ***")
    else if (finalState.isInHole)
      println("*** You fell in a hole and drowned while searching :( ***")
    else
      println(s"*** You could not find it after searching for $MAX_MOVES moves ****")
  }
}