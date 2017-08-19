package model

class GameState(pointsToWin: Int, totalTurns: Int, startingDynamite: Int) {
  var botHistory = List.empty[GameMove.Value]
  var opponentHistory = List.empty[GameMove.Value]

  def getHistory = {
    botHistory
  }

  def getOpponentHistory = {
    opponentHistory
  }

  def getPointsLeftToWin = {
    pointsToWin
  }

  def getRemainingTurns = {
    totalTurns - botHistory.length
  }

  def getRemainingDynamite = {
    startingDynamite - botHistory.count(_ == GameMove.DYNAMITE)
  }
}
