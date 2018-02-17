package com.barrybecker4.qlearning.frozenlake

import java.util.Scanner

class Input {

  private val scanner: Scanner = new Scanner(System.in)

  /**
    * @param queryMsg the prompt
    * @param alternatives set of possible responses (lower case)
    * @return given a prompt return one of several specified upper case characters fro the user */
  def charQuery(queryMsg: String, alternatives: Seq[Character], default: Option[Character] = None): Character = {
    val theDefault = if (default.isDefined) default.get else alternatives.head
    println(queryMsg + " " + alternatives.mkString("(", "/", ")"))
    print(s"[$theDefault]:")
    val answer = scanner.nextLine()
    if (answer.isEmpty) theDefault else answer.head.toLower
  }

  /**
    * @param default default value to use if one not provided
    * @param min minimum allowed
    * @param max maximum allowed
    * @return the user specified number.
    */
  def getNumber(default: Double, min: Double, max: Double): Double = {
    print(s"[$default]")
    var answer = scanner.nextLine()
    if (answer.isEmpty) default
    else {
      while (invalidNum(answer, min, max)) {
        println(s"Invalid number '$answer'. Enter a number between $min and $max, inclusive.")
        answer = scanner.nextLine()
      }
      answer.toDouble
    }
  }

  private def invalidNum(answer: String, mini: Double, maxi: Double): Boolean = {
    var num: Double = 0.0
    try {
      num = answer.trim.toDouble
    } catch {
      case e: NumberFormatException => println(s"bad = $num"); false
      case _: Throwable => false
    }
    num < mini || num > maxi
  }
}
