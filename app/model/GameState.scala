package model

object GameState {

  val TOTAL_TURNS = 1000
  val STARTING_DYNAMITE = 100

  var botHistory = List.empty[GameMove.Value]
  var opponentHistory = List.empty[GameMove.Value]

  def reset = {
    botHistory = List.empty[GameMove.Value]
    opponentHistory = List.empty[GameMove.Value]
  }

  def getHistory = {
    botHistory
  }

  def getOpponentHistory = {
    opponentHistory
  }

  def getRemainingTurns = {
    TOTAL_TURNS - botHistory.length
  }

  def getRemainingDynamite = {
    STARTING_DYNAMITE - botHistory.count(_ == GameMove.DYNAMITE)
  }
}
