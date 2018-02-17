package com.barrybecker4.qlearning.frozenlake

import com.barrybecker4.qlearning.common.{QLearner, QTable}
import com.barrybecker4.qlearning.frozenlake.Direction.Direction
import LakeSimulator._
import scala.collection.mutable
import scala.util.Random


object LakeSimulator {
  /** give up if not solved in this many moves */
  val MAX_MOVES = 1000
  val DEFAULT_WIND_FEQUENCY = 0.1
  val DEFAULT_EPSILON = 0.3  // exploitation vs exploration
  val DEFAULT_LEARNING_TRIALS = 10000
  val MAX_TEST_TRIALS = 100000

  def main(args:Array[String]) {
    val simulator = new LakeSimulator()
    println("Let's simulate the frozen lake problem.")
    println("Starting from S, try to retrieve the frisbee at G while avoiding holes (H) ")
    println(s"and being mindful of the random wind, " +
      s"which can occasionally blow you in a random direction.\n")
    simulator.simulate()
  }
}

/** Text based lake simulator.
  * First learn, then simulate.
  */
class LakeSimulator {

  private val input = new Input()
  private var table: QTable[Direction] = _

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
    do {
      val (finalState, numSteps) = solve()
      showOutcome(finalState, numSteps)
    } while (shouldContinue())
  }

  private def runLotsOfSimsAndReport(): Unit = {

    val numRuns = getNumRuns
    val map = mutable.Map("deaths" -> (0, 0), "successes" -> (0, 0), "timeouts" -> (0, 0))

    for (i <- 0 until numRuns) {
      val (finalState, numSteps) = solve(verbose = false)
      val endState = if (finalState.isGoalReached) "successes"
      else if (finalState.isInHole)"deaths"
      else "timeouts"

      val value = map(endState)
      map(endState) = (value._1 + 1, value._2 + numSteps)
    }

    Seq("successes", "deaths", "timeouts").foreach( v => {
      val num = map(v)._1
      val avg = if (num == 0) 0 else map(v)._2 / num
      println(s"Num $v = $num  average steps = $avg")
    })
  }

  private def getNumRuns: Int = {
    println(s"How many simulations do you want to run [1 - $MAX_TEST_TRIALS]?")
    input.getNumber(100, 1, MAX_TEST_TRIALS).toInt
  }

  private def shouldContinue(): Boolean = input.charQuery("Try to solve again?", Seq('y', 'n')) == 'y'

  private def learnHowToSolve(windFrequency: Double = DEFAULT_WIND_FEQUENCY,
                              epsilon: Double = DEFAULT_EPSILON,
                              numTrials: Int = DEFAULT_LEARNING_TRIALS): Unit = {

    println("How many learning trials (100 - 1000000)?")
    val numTrials = input.getNumber(DEFAULT_LEARNING_TRIALS, 100, 1000000).toInt
    println("What wind frequence (0 - 1)?")
    val windFrequency = input.getNumber(DEFAULT_WIND_FEQUENCY, 0, 1.0)
    println("What epsilon (0 - 1)? Higher value leads to more exploration, rather than exploitation.")
    val epsilon = input.getNumber(DEFAULT_EPSILON, 0, 1.0)

    val lake = Lake(windFrequency = windFrequency)
    table = new QTable[Direction](new LakeState(lake), Some(lake.initialTable()), epsilon = epsilon, new Random(1L))
    val learner = new QLearner[Direction](learningRate = 0.8f, futureRewardDiscount = 0.8f)
    print("Learning...")
    learner.learn(table, numTrials)
    println("...I just learned how to solve.\n")
  }

  /** @return the final game state when no moves left, and the number of steps it took to get there */
  private def solve(verbose: Boolean = true): (LakeState, Int) = {
    var state: LakeState = table.initialState.asInstanceOf[LakeState]
    var ct = 0

    while (state.hasTransitions && ct < MAX_MOVES) {
      if (verbose) {
        println(s"------------  step: $ct")
        println(state.toString)
      }
      state = state.makeTransition(table.getBestMove(state)._1)
      ct += 1
    }
    (state, ct)
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