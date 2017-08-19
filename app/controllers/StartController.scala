package controllers

import javax.inject._

import model.{GameLogic, GameState}
import play.api.mvc._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class StartController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def start() = Action { implicit request: Request[AnyContent] =>
    val json = request.body.asJson.get
    GameLogic.gameState = new GameState(
      pointsToWin = (json \ "pointsToWin").asOpt[Int].get,
      totalTurns = (json \ "maxRounds").asOpt[Int].get,
      startingDynamite = (json \ "dynamiteCount").asOpt[Int].get)
    Ok
  }
}
