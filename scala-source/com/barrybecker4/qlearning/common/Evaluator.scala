package com.barrybecker4.qlearning.common

/**
  * Evaluates a Q-learning model
  * T - the type of transition
  */
trait Evaluator[T] {

  /** @return a measure of the error between 0 and 1. Zero meaning no error, and one is maximum error. */
  def evaluate(): Double
}
