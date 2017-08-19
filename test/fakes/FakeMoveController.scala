package fakes

import javax.inject.Inject

import model.{GameLogic, GameMove}
import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}

class FakeMoveController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def move(moveToMake: GameMove.Value) = Action { implicit request: Request[AnyContent] =>
    GameLogic.gameState.botHistory = GameLogic.gameState.botHistory :+ moveToMake
    Ok(moveToMake.toString)
  }

  def lastOpponentMove() = Action { implicit request: Request[AnyContent] =>
    GameLogic.gameState.opponentHistory = GameLogic.gameState.opponentHistory :+ GameMove.withName(request.body.asText.get)
    Ok
  }
}
