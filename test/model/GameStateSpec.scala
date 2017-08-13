package model

import controllers.{MoveController, StartController}
import org.scalatestplus.play.PlaySpec
import play.api.test.FakeRequest
import play.api.test.Helpers.{GET, POST, OK, defaultAwaitTimeout, status, stubControllerComponents}

class GameStateSpec extends PlaySpec {
  "MoveController getHistory" should {
    "return every move bot has made since /start was called" in {
      new StartController(stubControllerComponents()).start().apply(FakeRequest(GET, "/start"))
      val controller = new MoveController(stubControllerComponents())
      var movesMade = List.empty[GameMove.Value]
      for( i <- 0 to 5 ) {
        val response = controller.move().apply(FakeRequest(GET, "/move"))
        status(response) mustBe OK
        movesMade = movesMade :+ GameMove.withName(play.api.test.Helpers.contentAsString(response))
      }

      GameState.getHistory must contain theSameElementsInOrderAs movesMade
    }
  }

  "MoveController getOpponentHistory" should {
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
}
