package model

import controllers.{MoveController, StartController}
import fakes.FakeMoveController
import org.scalatestplus.play.PlaySpec
import play.api.libs.json.Json._
import play.api.test.FakeRequest
import play.api.test.Helpers._

class GameStateSpec extends PlaySpec {
  "GameState getHistory" should {
    "return every move bot has made since /start was called" in {
      GameLogic.gameState = new GameState(pointsToWin = 1000, totalTurns = 2000, startingDynamite = 100)
      val controller = new FakeMoveController(stubControllerComponents())
      controller.move(GameMove.ROCK).apply(FakeRequest(GET, "/move"))
      controller.move(GameMove.PAPER).apply(FakeRequest(GET, "/move"))
      controller.move(GameMove.SCISSORS).apply(FakeRequest(GET, "/move"))
      controller.move(GameMove.DYNAMITE).apply(FakeRequest(GET, "/move"))
      controller.move(GameMove.WATERBOMB).apply(FakeRequest(GET, "/move"))

      GameLogic.gameState.getHistory must contain theSameElementsInOrderAs List(GameMove.ROCK, GameMove.PAPER, GameMove.SCISSORS, GameMove.DYNAMITE, GameMove.WATERBOMB)
    }
  }

  "GameState getOpponentHistory" should {
    "return every move opponent has made since /start was called" in {
      GameLogic.gameState = new GameState(pointsToWin = 1000, totalTurns = 2000, startingDynamite = 100)
      val controller = new MoveController(stubControllerComponents())
      controller.lastOpponentMove().apply(FakeRequest(POST, "/move").withJsonBody(lastMoveJson(GameMove.ROCK)))
      controller.lastOpponentMove().apply(FakeRequest(POST, "/move").withJsonBody(lastMoveJson(GameMove.PAPER)))
      controller.lastOpponentMove().apply(FakeRequest(POST, "/move").withJsonBody(lastMoveJson(GameMove.SCISSORS)))
      controller.lastOpponentMove().apply(FakeRequest(POST, "/move").withJsonBody(lastMoveJson(GameMove.DYNAMITE)))
      controller.lastOpponentMove().apply(FakeRequest(POST, "/move").withJsonBody(lastMoveJson(GameMove.WATERBOMB)))

      GameLogic.gameState.getOpponentHistory must contain theSameElementsInOrderAs List(GameMove.ROCK, GameMove.PAPER, GameMove.SCISSORS, GameMove.DYNAMITE, GameMove.WATERBOMB)
    }
  }

  "GameState getRemainingTurns" should {
    "return 1000 from the start" in {
      val totalTurns = 1000
      GameLogic.gameState = new GameState(pointsToWin = 1000, totalTurns = totalTurns, startingDynamite = 100)

      GameLogic.gameState.getRemainingTurns mustBe totalTurns
    }

    "return 990 after 10 turns" in {
      val totalTurns = 1000
      GameLogic.gameState = new GameState(pointsToWin = 1000, totalTurns = totalTurns, startingDynamite = 100)
      val controller = new MoveController(stubControllerComponents())
      for( i <- 0 to 9 ) {
        controller.move().apply(FakeRequest(GET, "/move"))
      }

      GameLogic.gameState.getRemainingTurns mustBe totalTurns - 10
    }
  }

  "GameState getRemainingDynamite" should {
    "return 100 from the start" in {
      val startingDynamite = 100
      GameLogic.gameState = new GameState(pointsToWin = 1000, totalTurns = 2000, startingDynamite = startingDynamite)

      GameLogic.gameState.getRemainingDynamite mustBe startingDynamite
    }

    "return 90 after 10 plays of DYNAMITE" in {
      val startingDynamite = 100
      GameLogic.gameState = new GameState(pointsToWin = 1000, totalTurns = 2000, startingDynamite = startingDynamite)
      val controller = new FakeMoveController(stubControllerComponents())
      for (i <- 0 to 9) {
        controller.move(GameMove.DYNAMITE).apply(FakeRequest(GET, "/move"))
      }

      GameLogic.gameState.getRemainingDynamite mustBe startingDynamite - 10
    }
  }

  def lastMoveJson(gameMove: GameMove.Value): _root_.play.api.libs.json.JsValue = {
    toJson(Map(
      "opponentLastMove" -> toJson(gameMove)
    ))
  }
}
