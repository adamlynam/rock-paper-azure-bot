package model

object GameState {
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
}
