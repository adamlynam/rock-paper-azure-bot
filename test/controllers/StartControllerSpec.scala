package controllers

import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.test._
import play.api.test.Helpers._

class StartControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {
  "StartController POST" should {
    "allow a new game to start" in {
      val controller = new StartController(stubControllerComponents())

      status(controller.start.apply(FakeRequest(POST, "/start"))) mustBe OK
    }
  }
}
