package com.barrybecker4.qlearning.frozenlake

import java.util.Scanner

import com.barrybecker4.qlearning.common.{QLearner, QTable}
import com.barrybecker4.qlearning.frozenlake.Direction.Direction
import LakeSimulator.MAX_MOVES

import scala.util.Random


object LakeSimulator {
  /** give up if not solved in this many moves */
  val MAX_MOVES = 1000

  def main(args:Array[String]) {
    val simulator = new LakeSimulator()
    println("Let's simulate the frozen lake problem.")
    println("Starting from S, try to retrieve the frisbee at G while avoiding holes (H) ")
    println("and being mindful of the random wind which can occasionally blow you in an unexpected direction.\n")
    simulator.simulate()
  }
}

/** Text based lake simulator.
  * First learn, then simulate.
  */
class LakeSimulator {

  private val lake = Lake() //Lake(4, 4, Location(1, 1), Location(3, 3), holes = Set(Location(2, 2)), 0.1)

  private val table = new QTable[Direction](new LakeState(lake), Some(lake.initialTable()), 0.05, new Random(1L))
  private val learner = new QLearner[Direction]()
  private val scanner = new Scanner(System.in)

  def simulate(): Unit = {
    learnHowToSolve()

    val finalState = solve()
    showOutcome(finalState)
  }

  private def learnHowToSolve(): Unit = {
    print("Learning...")
    learner.learn(table, 5000)
    println("...I just learned how to solve.\n")
  }

  /** @return the final game state when no moves left */
  private def solve(): LakeState = {
    var state: LakeState = table.initialState.asInstanceOf[LakeState] //new LakeState(lake)
    var ct = 0

    while (state.hasTransitions && ct < MAX_MOVES) {
      println("------------")
      println(state.toString)
        state = state.makeTransition(table.getBestMove(state)._1)
    }
    state
  }

  private def showOutcome(finalState: LakeState): Unit = {
    println("\n" + finalState.toString)
    if (finalState.isGoalReached)
      println("*** Congratulations - you found the frisbee! ***")
    else if (finalState.isInHole)
      println("*** You fell in a hole and drowned while searching :( ***")
    else
      println(s"*** You could not find it after searching for $MAX_MOVES moves ****")
  }
}