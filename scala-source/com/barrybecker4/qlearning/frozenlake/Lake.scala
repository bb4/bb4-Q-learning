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

  val SIMPLE_4x4_LAKE = new Lake()
  val LARGE_7x10_LAKE = new Lake(7, 10,
    start = Location(1, 1), goal = Location(6, 8),
    holes = Set(Location(0, 0), Location(4, 2), Location(2, 3), Location(3, 8), Location(6, 5)),
    windFrequency = 0.1, new Random(1L))
}

/** The lake configuration.
  * The FrozenLake environment consists of a M1 x M2 grid of blocks,
  * each one either the start block, the goal block, a safe frozen block, or a dangerous hole.
  * The objective is to learn to navigate from the start to the goal without falling nto a hole.
  * At any given time the agent can choose to move either up, down, left, or right.
  * A wind occasionally blows the agent onto a space they didn’t choose, making perfect performance impossible.
  * Learning to avoid the holes and reach the goal are still doable.
  * The reward for steps where you survive is 0, die is -1, and reach the goal is 1.
  * The 4x4 frozen lake config from OpenAI gym is "SFFFFHFHFFFHHFFG" (https://github.com/openai/gym)
  * @param numRows width of the lake in blocks
  * @param numColumns length of the length in blocks
  * @param start initial starting point of the agent
  * @param goal reward of 1 when you reach the goal
  * @param holes set of holes in the ice. If you land on one, you fall in and die. Reward of -1
  * @param windFrequency a number between 0 and 1. The frequency with which the wind blows and moves you a random
  *                      direction on each attempted movement.
  * @author Barry Becker
  */
case class Lake(numRows: Int = 4, numColumns: Int = 4,
                start: Location = Location(0, 0),
                goal: Location = Location(3, 3),
                holes: Set[Location] = Set(Location(1, 1), Location(1, 3), Location(2, 3), Location(3, 0)),
                windFrequency: Double = 0.1, rnd: Random = RND) {

  def isInHole(loc: Location): Boolean = holes.contains(loc)
  def isGoal(loc: Location): Boolean = goal == loc


  /** @return the location ended up in after attempting to move from start, in direction dir */
  def attemptToMoveInDirection(intialLoc: Location, dir: Direction): Location = {
    var desiredLoc = intialLoc.move(dir)
    if (rnd.nextDouble() < windFrequency)
      desiredLoc = desiredLoc.move(Direction.VALUES(rnd.nextInt(4)))  // wind moves you randomly
    makeInBound(desiredLoc)
  }

  def getLegalTransitionsFrom(loc: Location): Seq[Direction] = {
    var moves: Seq[Direction] = Seq()
    if (!(isGoal(loc) || isInHole(loc))) {
      Direction.VALUES.foreach { d => moves = addIfInBounds(loc.move(d), d, moves) }
    }
    moves
  }

  private def makeInBound(loc: Location): Location = {
    Location(Math.min(numRows - 1, Math.max(0, loc.row)), Math.min(numColumns - 1, Math.max(0, loc.col)))
  }

  private def addIfInBounds(candidateLoc: Location, dir: Direction, moves: Seq[Direction]): Seq[Direction] =
    if (inBounds(candidateLoc)) moves :+ dir else moves

  private def inBounds(loc: Location): Boolean =
    loc.row >= 0 && loc.row < numRows && loc.col >= 0 && loc.col < numColumns


  /** @return all states and transactions from them */
  def initialTable(): Map[State[Direction], mutable.Map[Direction, Float]] = {
    (for (i <- 0 until numRows * numColumns)
        yield (LakeState(Location(i / numColumns, i % numColumns), this),
          mutable.Map(Direction.VALUES.map(d => (d, 0.0f)): _*))
      ).toMap
  }

  def toString(currentLoc: Option[Location]): String = {
    var s = ""
    for (i <- 0 until numRows) {
      for (j <- 0 until numColumns) {
        val loc = Location(i, j)
        val symb = if (currentLoc.isDefined && currentLoc.get == loc) '@'
                   else if (isInHole(loc)) 'H' else if (isGoal(loc)) 'G' else if (start == loc) 'S' else '.'
        s += symb
      }
      s += "\n"
    }
    s
  }
  override def toString: String = toString(None)
}