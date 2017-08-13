package controllers

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import model.GameMove
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.test.Helpers.{GET, OK, POST, defaultAwaitTimeout, status, stubControllerComponents}
import play.api.test.{FakeRequest, Injecting}

class MoveControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {

  implicit val actorSystem = ActorSystem() ;
  implicit val materializer = ActorMaterializer()

  "MoveController GET" should {
    "fetch the next move from our bot" in {
      val controller = new MoveController(stubControllerComponents())

      val response = controller.move().apply(FakeRequest(GET, "/move"))
      status(response) mustBe OK
      GameMove.values contains GameMove.withName(play.api.test.Helpers.contentAsString(response))
    }
  }

  "MoveController POST" should {
    "deliver the previous move from the opponent" in {
      val controller = new MoveController(stubControllerComponents())

      status(controller.lastOpponentMove().apply(FakeRequest(POST, "/move").withTextBody(GameMove.ROCK.toString))) mustBe OK
    }
  }
}