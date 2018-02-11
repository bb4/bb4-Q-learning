package com.barrybecker4.qlearning.frozenlake

import java.util.Scanner

import com.barrybecker4.qlearning.common.{QLearner, QTable}
import com.barrybecker4.qlearning.frozenlake.Direction.Direction
import LakeSimulator._

import scala.util.Random


object LakeSimulator {
  /** give up if not solved in this many moves */
  val MAX_MOVES = 1000
  val WIND_FEQUENCY = 0.1

  def main(args:Array[String]) {
    val simulator = new LakeSimulator()
    println("Let's simulate the frozen lake problem.")
    println("Starting from S, try to retrieve the frisbee at G while avoiding holes (H) ")
    println(s"and being mindful of the random wind, " +
      s"which can occasionally blow you in a random direction with probability $WIND_FEQUENCY.\n")
    simulator.simulate()
  }
}

/** Text based lake simulator.
  * First learn, then simulate.
  */
class LakeSimulator {

  private val lake = Lake(windFrequency = WIND_FEQUENCY)
  //Lake(4, 4, Location(1, 1), Location(3, 3), holes = Set(Location(2, 2)), 0.1)

  private val table = new QTable[Direction](new LakeState(lake), Some(lake.initialTable()), 0.05, new Random(1L))
  private val learner = new QLearner[Direction](learningRate = 0.8f, futureRewardDiscount = 0.8f)
  private val scanner = new Scanner(System.in)

  def simulate(): Unit = {
    learnHowToSolve()

    val (finalState, numSteps) = solve()
    showOutcome(finalState, numSteps)
  }

  private def learnHowToSolve(): Unit = {
    print("Learning...")
    learner.learn(table, 50000)
    println("...I just learned how to solve.\n")
  }

  /** @return the final game state when no moves left, and the number of steps it took to get there */
  private def solve(): (LakeState, Int) = {
    var state: LakeState = table.initialState.asInstanceOf[LakeState] //new LakeState(lake)
    var ct = 0

    while (state.hasTransitions && ct < MAX_MOVES) {
      println("------------")
      println(state.toString)
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