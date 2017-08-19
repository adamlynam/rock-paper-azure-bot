package controllers

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import model.GameMove
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.libs.json.Json.{fromJson, toJson}
import play.api.test.Helpers._
import play.api.test.{FakeRequest, Injecting}

class MoveControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {

  implicit val actorSystem = ActorSystem() ;
  implicit val materializer = ActorMaterializer()

  "MoveController GET" should {
    "fetch the next move from our bot" in {
      val controller = new MoveController(stubControllerComponents())

      val response = controller.move().apply(FakeRequest(GET, "/move"))
      status(response) mustBe OK
      contentType(response).get mustBe "application/json"
      GameMove.values contains fromJson[GameMove.Value](contentAsJson(response)).get
    }
  }

  "MoveController POST" should {
    "deliver the previous move from the opponent" in {
      val controller = new MoveController(stubControllerComponents())

      status(controller.lastOpponentMove().apply(FakeRequest(POST, "/move").withJsonBody(
        toJson(Map(
          "opponentLastMove" -> toJson(GameMove.ROCK)
        ))))) mustBe OK
    }
  }
}