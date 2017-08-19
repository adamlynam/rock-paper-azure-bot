package controllers

import javax.inject.Inject

import model.{GameLogic, GameMove, GameState}
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}

class MoveController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def move() = Action { implicit request: Request[AnyContent] =>
    val moveToMake = GameMove.randomMove
    GameLogic.gameState.botHistory = GameLogic.gameState.botHistory :+ moveToMake
    Ok(Json.toJson(moveToMake)).as("application/json")
  }

  def lastOpponentMove() = Action { implicit request: Request[AnyContent] =>
    GameLogic.gameState.opponentHistory = GameLogic.gameState.opponentHistory :+ (request.body.asJson.get \ "opponentLastMove").asOpt[GameMove.Value].get
    Ok
  }
}
