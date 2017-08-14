package model

import scala.util.Random

object GameMove extends Enumeration {
  val ROCK, PAPER, SCISSORS, DYNAMITE, WATERBOMB = Value

  def randomMove: GameMove.Value = {
    values.toVector((new Random).nextInt(values.size))
  }

  def probabalisticDynamiteMove: GameMove.Value = {
    if (GameState.getRemainingDynamite > 0 && GameState.getRemainingTurns > 0 && (new Random).nextDouble < GameState.getRemainingDynamite / GameState.getRemainingTurns) {
      GameMove.DYNAMITE
    }
    else {
      val remainingMoves = List(GameMove.ROCK, GameMove.PAPER, GameMove.SCISSORS)
      remainingMoves.toVector((new Random).nextInt(remainingMoves.size))
    }
  }

  def copyOpponentMove: GameMove.Value = {
    if (GameState.opponentHistory.length > 0) {
      GameState.opponentHistory.last
    }
    else {
      values.toVector((new Random).nextInt(values.size))
    }
  }
}