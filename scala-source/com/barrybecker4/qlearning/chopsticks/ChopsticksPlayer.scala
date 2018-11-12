package com.barrybecker4.qlearning.chopsticks

import java.util.Scanner
import ChopsticksState.TransType
import com.barrybecker4.qlearning.common.{QLearner, QTable}


object ChopsticksPlayer extends App {
  val gamePlayer = new ChopsticksPlayer()
  println("Let's play Finger-Chopsticks!")
  gamePlayer.playGameAgainstHuman()
}

/** Text based finger-chopsticks game player. First learn, then play.
  * @author Barry Becker
  */
class ChopsticksPlayer {

  private val table: QTable[TransType] = new QTable(ChopsticksState(), None, epsilon = 0.2)
  private val learner = new QLearner[TransType](learningRate = 0.8f, futureRewardDiscount = 0.99f)
  private val scanner = new Scanner(System.in)

  def playGameAgainstHuman(): Unit = {
    learnHowToPlay()
    val humanGoesFirst = determineIfHumanFirst()
    playTheGame(humanGoesFirst)
  }

  private def learnHowToPlay(): Unit = {
    print("Learning...")
    learner.learn(table, 20000)
    println("...I just learned how to play.\n")
  }

  private def determineIfHumanFirst(): Boolean = {
    println("Would you like to go first? (y/n)")
    print("[y]")
    val answer = scanner.nextLine()
    answer.isEmpty || answer.head.toUpper == 'Y'
  }

  /** @return the final game state when no moves left */
  private def playTheGame(humanGoesFirst: Boolean): ChopsticksState = {
    var state = ChopsticksState()
    var humanToMove = humanGoesFirst

    while (state.hasTransitions) {
      println("--------")
      println(state.toString)
      state = if (humanToMove) doHumanMove(state, humanGoesFirst) else doComputerMove(state)
      humanToMove = !humanToMove
    }
    showOutcome(state, humanToMove)
    state
  }

  private def doHumanMove(state: ChopsticksState, humanGoesFirst: Boolean): ChopsticksState = {
    val handOpts = "1 or 2"
    println("Enter a two digit number.\n" +
      s"The first digit is your hand to use for tapping ($handOpts),\n" +
      "and the second digit is the hand to be tapped " +
      s"($handOpts for one of their hands, or 0 for your own other hand): ")
    var action = getAction(scanner.nextInt())
    while (!isValid(action, state, humanGoesFirst)) {
      println("Invalid. Try again.")
      action = getAction(scanner.nextInt())
    }
    state.makeTransition(action)
  }

  private def doComputerMove(state: ChopsticksState): ChopsticksState = {
    println("The choices are: " + table.getActions(state))
    val computerMove = table.getBestMove(state)._1
    if (computerMove._2 == 0)
      println("The computer taps its own hand and does a split.")
    else
      println(s"The computer chooses to tap your hand #${computerMove._2} with its hand #${computerMove._1}.")
    state.makeTransition(computerMove)
  }

  private def showOutcome(finalState: ChopsticksState, computerMovedLast: Boolean): Unit = {
    println("\n" + finalState.toString)
    if (computerMovedLast) println("*** You lost! Better luck next time ***")
    else println("*** Congratulations - you won! ***")
  }

  private lazy val validInputs: Set[(Byte, Byte)] =
    (for {i <- 1 to 2; j <- 0 to 2} yield (i.toByte, j.toByte)).toSet

  private def isValid(action: (Byte, Byte), state: ChopsticksState, humanGoesFirst: Boolean): Boolean =
    validInputs.contains(action) &&
      validSplitIfSplit(action, state, humanGoesFirst) &&
      !inactiveHandTapped(action, state, humanGoesFirst)

  private def validSplitIfSplit(action: (Byte, Byte), state: ChopsticksState, humanGoesFirst: Boolean): Boolean = {
    val hands = if (humanGoesFirst) state.firstHands else state.secondHands
    val v = (action._2 != 0) || (hands._1 + hands._2) % 2 == 0
    if (!v) println("You can't split if you do not have an even sum of fingers!")
    v
  }

  private def inactiveHandTapped(action: (Byte, Byte), state: ChopsticksState, humanGoesFirst: Boolean): Boolean = {
    if (action._2 == 0) return false // self tap of inactive hand is ok to do split
    val (h1, h2) = if (humanGoesFirst) (state.firstHands, state.secondHands) else (state.secondHands, state.firstHands)
    val inactiveTapped = action match {
      case (1, 1) => h1._1 == 0 || h2._1 == 0
      case (2, 1) => h1._2 == 0 || h2._1 == 0
      case (1, 2) => h1._1 == 0 || h2._2 == 0
      case (2, 2) => h1._2 == 0 || h2._2 == 0
      case _ => false
    }
    if (inactiveTapped) println("An inactive hand cannot tap or be tapped")
    inactiveTapped
  }

  private def getAction(input: Int) = ((input / 10).toByte, (input % 10).toByte)
}