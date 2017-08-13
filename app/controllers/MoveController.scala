package controllers

import javax.inject.Inject

import model.{GameMove, GameState}
import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}

class MoveController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def move() = Action { implicit request: Request[AnyContent] =>
    val moveToMake = GameMove.randomMove
    GameState.botHistory = GameState.botHistory :+ moveToMake
    Ok(moveToMake.toString)
  }

  def lastOpponentMove() = Action { implicit request: Request[AnyContent] =>
    GameState.opponentHistory = GameState.opponentHistory :+ GameMove.withName(request.body.asText.get)
    Ok
  }
}
