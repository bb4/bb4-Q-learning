package com.barrybecker4.qlearning.frozenlake

import com.barrybecker4.qlearning.frozenlake.Direction.Direction
import Lake.RND
import com.barrybecker4.qlearning.common.State

import scala.collection.mutable
import scala.util.Random


object Direction extends Enumeration {
  type Direction = Value
  val N, S, E, W = Value
  val VALUES = Seq(N, S, E, W)
}

case class Location(row: Int, col: Int) {
  def move(dir: Direction): Location = dir match {
    case Direction.N => Location(row - 1, col)
    case Direction.S => Location(row + 1, col)
    case Direction.E => Location(row, col + 1)
    case Direction.W => Location(row, col - 1)
  }
}

object Lake {
  private val RND = new Random()
}

/** The lake configuration.
  * The FrozenLake environment consists of a M1 x M2 grid of blocks,
  * each one either the start block, the goal block, a safe frozen block, or a dangerous hole.
  * The objective is to learn to navigate from the start to the goal without falling nto a hole.
  * At any given time the agent can choose to move either up, down, left, or right.
  * A wind occasionally blows the agent onto a space they didn’t choose, making perfect performance impossible.
  * Learning to avoid the holes and reach the goal are still doable.
  * The reward for steps where you survive is 0, die is -1, and reach the goal is 1.
  * @param numRows width of the lake in blocks
  * @param numColumns length of the length in blocks
  * @param start initial starting point of the agent
  * @param goal reward of 1 when you reach the goal
  * @param holes set of holes in the ice. If you land on one, you fall in and die. Reward of -1
  * @param windFrequency a number between 0 and 1. The frequency with which the wind blows and moves you a random
  *                      direction on each attempted movement.
  * @author Barry Becker
  */
case class Lake(numRows: Int, numColumns: Int, start: Location,
           goal: Location, holes: Set[Location],
           windFrequency: Double = 0, rnd: Random = RND) {

  def isInHole(loc: Location): Boolean = holes.contains(loc)
  def isGoal(loc: Location): Boolean = goal == loc

  /** @return the location ended up in after attempting to move from start, in direction dir */
  def attemptToMoveInDirection(intialLoc: Location, dir: Direction): Location = {
    val desiredLoc = intialLoc.move(dir)
    if (rnd.nextDouble() < windFrequency)
      desiredLoc.move(Direction.VALUES(rnd.nextInt(4)))  // wind moves you randomly
    else desiredLoc
  }

  def getLegalTransitionsFrom(loc: Location): Seq[Direction] = {
    var moves: Seq[Direction] = Seq()
    Direction.VALUES.foreach { d => addIfInBounds(loc, d, moves) }
    moves
  }

  def addIfInBounds(candidateLoc: Location, dir: Direction, moves: Seq[Direction]): Seq[Direction] =
    if (inBounds(candidateLoc)) moves :+ dir else moves

  def inBounds(loc: Location): Boolean = {
    loc.row >= 0 && loc.row < numRows && loc.col >= 0 && loc.col < numColumns
  }

  /** @return all states and transactions from them */
  def initialTable(): Map[State[Direction], mutable.Map[Direction, Float]] = {
    (for (i <- 0 until numRows * numColumns)
        yield (LakeState(Location(i / numColumns, i % numColumns), this),
          mutable.Map(Direction.VALUES.map(d => (d, 0.0f)): _*))
      ).toMap
  }

  override def toString: String = {
    var s = ""
    for (i <- 0 until numRows) {
      for (j <- 0 until numColumns) {
        val loc = Location(i, j)
        s += (if (isInHole(loc)) 'H' else if (isGoal(loc)) 'G' else if (start == loc) 'S' else ".")
      }
      s += "\n"
    }
    s
  }
}