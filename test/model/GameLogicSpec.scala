package model

import controllers.{MoveController, StartController}
import org.scalatestplus.play.PlaySpec
import play.api.libs.json.Json
import play.api.libs.json.Json.toJson
import play.api.test.FakeRequest
import play.api.test.Helpers.{GET, POST, defaultAwaitTimeout, stubControllerComponents}

class GameLogicSpec extends PlaySpec {
  "GameLogic winningMoveAgainst" should {
    "calculate the winning move against a specific move" in {
      GameLogic.winningMoveAgainst(GameMove.ROCK) mustBe GameMove.PAPER
      GameLogic.winningMoveAgainst(GameMove.PAPER) mustBe GameMove.SCISSORS
      GameLogic.winningMoveAgainst(GameMove.SCISSORS) mustBe GameMove.ROCK
      GameLogic.winningMoveAgainst(GameMove.DYNAMITE) mustBe GameMove.WATERBOMB
      Set(GameMove.ROCK, GameMove.PAPER, GameMove.SCISSORS) contains GameLogic.winningMoveAgainst(GameMove.DYNAMITE)
    }
  }

  "GameLogic losingMoveAgainst" should {
    "calculate the winning move against a specific move" in {
      GameLogic.losingMoveAgainst(GameMove.ROCK) mustBe GameMove.SCISSORS
      GameLogic.losingMoveAgainst(GameMove.PAPER) mustBe GameMove.ROCK
      GameLogic.losingMoveAgainst(GameMove.SCISSORS) mustBe GameMove.PAPER
      Set(GameMove.ROCK, GameMove.PAPER, GameMove.SCISSORS) contains GameLogic.losingMoveAgainst(GameMove.WATERBOMB)
      GameLogic.losingMoveAgainst(GameMove.WATERBOMB) mustBe GameMove.DYNAMITE
    }
  }

  "GameLogic calculateResult" should {
    "return WIN, DRAW, LOSE depending on specific moves played" in {
      GameLogic.calculateResult(GameMove.ROCK, GameMove.ROCK) mustBe GameResult.DRAW
      GameLogic.calculateResult(GameMove.ROCK, GameMove.PAPER) mustBe GameResult.LOSE
      GameLogic.calculateResult(GameMove.ROCK, GameMove.SCISSORS) mustBe GameResult.WIN
      GameLogic.calculateResult(GameMove.ROCK, GameMove.DYNAMITE) mustBe GameResult.LOSE
      GameLogic.calculateResult(GameMove.ROCK, GameMove.WATERBOMB) mustBe GameResult.WIN
      GameLogic.calculateResult(GameMove.PAPER, GameMove.ROCK) mustBe GameResult.WIN
      GameLogic.calculateResult(GameMove.PAPER, GameMove.PAPER) mustBe GameResult.DRAW
      GameLogic.calculateResult(GameMove.PAPER, GameMove.SCISSORS) mustBe GameResult.LOSE
      GameLogic.calculateResult(GameMove.PAPER, GameMove.DYNAMITE) mustBe GameResult.LOSE
      GameLogic.calculateResult(GameMove.PAPER, GameMove.WATERBOMB) mustBe GameResult.WIN
      GameLogic.calculateResult(GameMove.SCISSORS, GameMove.ROCK) mustBe GameResult.LOSE
      GameLogic.calculateResult(GameMove.SCISSORS, GameMove.PAPER) mustBe GameResult.WIN
      GameLogic.calculateResult(GameMove.SCISSORS, GameMove.SCISSORS) mustBe GameResult.DRAW
      GameLogic.calculateResult(GameMove.SCISSORS, GameMove.DYNAMITE) mustBe GameResult.LOSE
      GameLogic.calculateResult(GameMove.SCISSORS, GameMove.WATERBOMB) mustBe GameResult.WIN
      GameLogic.calculateResult(GameMove.DYNAMITE, GameMove.ROCK) mustBe GameResult.WIN
      GameLogic.calculateResult(GameMove.DYNAMITE, GameMove.PAPER) mustBe GameResult.WIN
      GameLogic.calculateResult(GameMove.DYNAMITE, GameMove.SCISSORS) mustBe GameResult.WIN
      GameLogic.calculateResult(GameMove.DYNAMITE, GameMove.DYNAMITE) mustBe GameResult.DRAW
      GameLogic.calculateResult(GameMove.DYNAMITE, GameMove.WATERBOMB) mustBe GameResult.LOSE
      GameLogic.calculateResult(GameMove.WATERBOMB, GameMove.ROCK) mustBe GameResult.LOSE
      GameLogic.calculateResult(GameMove.WATERBOMB, GameMove.PAPER) mustBe GameResult.LOSE
      GameLogic.calculateResult(GameMove.WATERBOMB, GameMove.SCISSORS) mustBe GameResult.LOSE
      GameLogic.calculateResult(GameMove.WATERBOMB, GameMove.DYNAMITE) mustBe GameResult.WIN
      GameLogic.calculateResult(GameMove.WATERBOMB, GameMove.WATERBOMB) mustBe GameResult.DRAW
    }
  }

  "GameLogic totalWins" should {
    "calculate the number of wins the bot has had directly after starting" in {
      GameLogic.gameState = new GameState(pointsToWin = 1000, totalTurns = 2000, startingDynamite = 100)

      GameLogic.calculateWins(GameLogic.gameState.getHistory, GameLogic.gameState.opponentHistory) mustBe 0
    }

    "calculate the number of wins the bot has had after winning a game" in {
      GameLogic.gameState = new GameState(pointsToWin = 1000, totalTurns = 2000, startingDynamite = 100)
      val controller = new MoveController(stubControllerComponents())
      Json.fromJson[GameMove.Value](play.api.test.Helpers.contentAsJson(controller.move().apply(FakeRequest(GET, "/move")))).map(
        moveMade => {
          controller.lastOpponentMove().apply(FakeRequest(POST, "/move").withJsonBody(lastMoveJson(GameLogic.losingMoveAgainst(moveMade))))
          GameLogic.calculateWins(GameLogic.gameState.getHistory, GameLogic.gameState.opponentHistory) mustBe 1
        }
      )
    }

    "calculate the number of wins the bot has had after losing a game" in {
      GameLogic.gameState = new GameState(pointsToWin = 1000, totalTurns = 2000, startingDynamite = 100)
      val controller = new MoveController(stubControllerComponents())
      Json.fromJson[GameMove.Value](play.api.test.Helpers.contentAsJson(controller.move().apply(FakeRequest(GET, "/move")))).map(
        moveMade => {
          controller.lastOpponentMove().apply(FakeRequest(POST, "/move").withJsonBody(lastMoveJson(GameLogic.winningMoveAgainst(moveMade))))
          GameLogic.calculateWins(GameLogic.gameState.getHistory, GameLogic.gameState.opponentHistory) mustBe 0
        }
      )
    }

    "calculate the number of wins the bot has had after drawing a game" in {
      GameLogic.gameState = new GameState(pointsToWin = 1000, totalTurns = 2000, startingDynamite = 100)
      val controller = new MoveController(stubControllerComponents())
      Json.fromJson[GameMove.Value](play.api.test.Helpers.contentAsJson(controller.move().apply(FakeRequest(GET, "/move")))).map(
        moveMade => {
          controller.lastOpponentMove().apply(FakeRequest(POST, "/move").withJsonBody(lastMoveJson(moveMade)))
          GameLogic.calculateWins(GameLogic.gameState.getHistory, GameLogic.gameState.opponentHistory) mustBe 0
        }
      )
    }
  }

  def lastMoveJson(gameMove: GameMove.Value): _root_.play.api.libs.json.JsValue = {
    toJson(Map(
      "opponentLastMove" -> toJson(gameMove)
    ))
  }
}