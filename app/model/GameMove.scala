package model

import scala.util.Random

object GameMove extends Enumeration {
  val ROCK, PAPER, SCISSORS, DYNAMITE, WATERBOMB = Value

  def randomMove = {
    values.toVector((new Random).nextInt(values.size))
  }
}