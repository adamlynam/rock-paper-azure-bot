package controllers

import model.GameLogic
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.libs.json.Json.toJson
import play.api.test._
import play.api.test.Helpers._

class StartControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {
  "StartController POST" should {
    "allow a new game to start" in {
      val controller = new StartController(stubControllerComponents())

      status(controller.start.apply(FakeRequest(POST, "/start").withJsonBody(
        toJson(Map(
          "opponentName" -> toJson("[Opponent Name]"),
          "pointsToWin" -> toJson(1000),
          "maxRounds" -> toJson(2000),
          "dynamiteCount" -> toJson(100)
        ))))) mustBe OK
    }

    "sets the GameLogic.gameState correctly" in {
      val controller = new StartController(stubControllerComponents())
      val pointsToWin = 1000
      val totalTurns = 2000
      val startingDynamite = 100
      controller.start.apply(FakeRequest(POST, "/start").withJsonBody(
        toJson(Map(
          "opponentName" -> toJson("[Opponent Name]"),
          "pointsToWin" -> toJson(pointsToWin),
          "maxRounds" -> toJson(totalTurns),
          "dynamiteCount" -> toJson(startingDynamite)
        ))))

      GameLogic.gameState.getPointsLeftToWin mustBe pointsToWin
      GameLogic.gameState.getRemainingTurns mustBe totalTurns
      GameLogic.gameState.getRemainingDynamite mustBe startingDynamite
    }
  }
}
