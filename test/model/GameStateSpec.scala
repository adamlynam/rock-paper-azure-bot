package model

import controllers.{MoveController, StartController}
import fakes.FakeMoveController
import org.scalatestplus.play.PlaySpec
import play.api.test.FakeRequest
import play.api.test.Helpers.{GET, OK, POST, defaultAwaitTimeout, status, stubControllerComponents}

class GameStateSpec extends PlaySpec {
  "GameState getHistory" should {
    "return every move bot has made since /start was called" in {
      new StartController(stubControllerComponents()).start().apply(FakeRequest(GET, "/start"))
      val controller = new FakeMoveController(stubControllerComponents())
      controller.move(GameMove.ROCK).apply(FakeRequest(GET, "/move"))
      controller.move(GameMove.PAPER).apply(FakeRequest(GET, "/move"))
      controller.move(GameMove.SCISSORS).apply(FakeRequest(GET, "/move"))
      controller.move(GameMove.DYNAMITE).apply(FakeRequest(GET, "/move"))
      controller.move(GameMove.WATERBOMB).apply(FakeRequest(GET, "/move"))

      GameState.getHistory must contain theSameElementsInOrderAs List(GameMove.ROCK, GameMove.PAPER, GameMove.SCISSORS, GameMove.DYNAMITE, GameMove.WATERBOMB)
    }
  }

  "GameState getOpponentHistory" should {
    "return every move opponent has made since /start was called" in {
      new StartController(stubControllerComponents()).start().apply(FakeRequest(GET, "/start"))
      val controller = new MoveController(stubControllerComponents())
      controller.lastOpponentMove().apply(FakeRequest(POST, "/move").withTextBody(GameMove.ROCK.toString))
      controller.lastOpponentMove().apply(FakeRequest(POST, "/move").withTextBody(GameMove.PAPER.toString))
      controller.lastOpponentMove().apply(FakeRequest(POST, "/move").withTextBody(GameMove.SCISSORS.toString))
      controller.lastOpponentMove().apply(FakeRequest(POST, "/move").withTextBody(GameMove.DYNAMITE.toString))
      controller.lastOpponentMove().apply(FakeRequest(POST, "/move").withTextBody(GameMove.WATERBOMB.toString))

      GameState.getOpponentHistory must contain theSameElementsInOrderAs List(GameMove.ROCK, GameMove.PAPER, GameMove.SCISSORS, GameMove.DYNAMITE, GameMove.WATERBOMB)
    }
  }

  "GameState getRemainingTurns" should {
    "return 1000 from the start" in {
      new StartController(stubControllerComponents()).start().apply(FakeRequest(GET, "/start"))

      GameState.getRemainingTurns mustBe 1000
    }

    "return 990 after 10 turns" in {
      new StartController(stubControllerComponents()).start().apply(FakeRequest(GET, "/start"))
      val controller = new MoveController(stubControllerComponents())
      for( i <- 0 to 9 ) {
        controller.move().apply(FakeRequest(GET, "/move"))
      }

      GameState.getRemainingTurns mustBe 990
    }
  }

  "GameState getRemainingDynamite" should {
    "return 100 from the start" in {
      new StartController(stubControllerComponents()).start().apply(FakeRequest(GET, "/start"))

      GameState.getRemainingDynamite mustBe 100
    }

    "return 90 after 10 plays of DYNAMITE" in {
      new StartController(stubControllerComponents()).start().apply(FakeRequest(GET, "/start"))
      val controller = new FakeMoveController(stubControllerComponents())
      for (i <- 0 to 9) {
        controller.move(GameMove.DYNAMITE).apply(FakeRequest(GET, "/move"))
      }

      GameState.getRemainingDynamite mustBe 90
    }
  }
}
