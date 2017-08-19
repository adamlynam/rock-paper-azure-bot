package model


object GameLogic {
  val DEFAULT_POINTS_TO_WIN: Int = 1000
  val DEFAULT_TOTAL_TURNS: Int = 2000
  val DEFAULT_STARTING_DYNAMITE: Int = 100

  var gameState = new GameState(pointsToWin = DEFAULT_POINTS_TO_WIN, totalTurns = DEFAULT_TOTAL_TURNS, startingDynamite = DEFAULT_STARTING_DYNAMITE)

  val winningMoveAgainst = Map(
    GameMove.ROCK -> GameMove.PAPER,
    GameMove.PAPER -> GameMove.SCISSORS,
    GameMove.SCISSORS -> GameMove.ROCK,
    GameMove.DYNAMITE -> GameMove.WATERBOMB,
    GameMove.WATERBOMB -> GameMove.ROCK
  )
  val losingMoveAgainst = Map(
    GameMove.ROCK -> GameMove.SCISSORS,
    GameMove.PAPER -> GameMove.ROCK,
    GameMove.SCISSORS -> GameMove.PAPER,
    GameMove.DYNAMITE -> GameMove.ROCK,
    GameMove.WATERBOMB -> GameMove.DYNAMITE
  )

  def calculateResult(botMove: GameMove.Value, opponentMove: GameMove.Value): GameResult.Value = {
    botMove match {
      case GameMove.ROCK => {
        opponentMove match {
          case GameMove.ROCK => GameResult.DRAW
          case GameMove.PAPER => GameResult.LOSE
          case GameMove.SCISSORS => GameResult.WIN
          case GameMove.DYNAMITE => GameResult.LOSE
          case GameMove.WATERBOMB => GameResult.WIN
        }
      }
      case GameMove.PAPER => {
        opponentMove match {
          case GameMove.ROCK => GameResult.WIN
          case GameMove.PAPER => GameResult.DRAW
          case GameMove.SCISSORS => GameResult.LOSE
          case GameMove.DYNAMITE => GameResult.LOSE
          case GameMove.WATERBOMB => GameResult.WIN
        }
      }
      case GameMove.SCISSORS => {
        opponentMove match {
          case GameMove.ROCK => GameResult.LOSE
          case GameMove.PAPER => GameResult.WIN
          case GameMove.SCISSORS => GameResult.DRAW
          case GameMove.DYNAMITE => GameResult.LOSE
          case GameMove.WATERBOMB => GameResult.WIN
        }
      }
      case GameMove.DYNAMITE => {
        opponentMove match {
          case GameMove.ROCK => GameResult.WIN
          case GameMove.PAPER => GameResult.WIN
          case GameMove.SCISSORS => GameResult.WIN
          case GameMove.DYNAMITE => GameResult.DRAW
          case GameMove.WATERBOMB => GameResult.LOSE
        }
      }
      case GameMove.WATERBOMB => {
        opponentMove match {
          case GameMove.ROCK => GameResult.LOSE
          case GameMove.PAPER => GameResult.LOSE
          case GameMove.SCISSORS => GameResult.LOSE
          case GameMove.DYNAMITE => GameResult.WIN
          case GameMove.WATERBOMB => GameResult.DRAW
        }
      }
    }
  }

  def calculateWins(botMoves: List[GameMove.Value], opponentMoves: List[GameMove.Value]): Int = {
    var wins = 0
    for (i <- botMoves.indices) {
      calculateResult(botMoves(i), opponentMoves(i)) match {
        case GameResult.WIN => wins = wins + 1
        case GameResult.DRAW =>
        case GameResult.LOSE =>
      }
    }
    wins
  }
}
