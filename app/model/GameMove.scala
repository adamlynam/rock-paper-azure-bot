package model

import play.api.libs.json._

import scala.util.Random

object GameMove extends Enumeration {
  val ROCK, PAPER, SCISSORS, DYNAMITE, WATERBOMB = Value

  def randomMove: GameMove.Value = {
    values.toVector((new Random).nextInt(values.size))
  }

  def probabalisticDynamiteMove: GameMove.Value = {
    if (GameLogic.gameState.getRemainingDynamite > 0 && GameLogic.gameState.getRemainingTurns > 0 && (new Random).nextDouble < GameLogic.gameState.getRemainingDynamite / GameLogic.gameState.getRemainingTurns) {
      GameMove.DYNAMITE
    }
    else {
      val remainingMoves = List(GameMove.ROCK, GameMove.PAPER, GameMove.SCISSORS)
      remainingMoves.toVector((new Random).nextInt(remainingMoves.size))
    }
  }

  def copyOpponentMove: GameMove.Value = {
    if (GameLogic.gameState.opponentHistory.length > 0) {
      GameLogic.gameState.opponentHistory.last
    }
    else {
      values.toVector((new Random).nextInt(values.size))
    }
  }

  implicit val moveFormat: Format[GameMove.Value] = Format(Reads.enumNameReads(GameMove), Writes.enumNameWrites)
}