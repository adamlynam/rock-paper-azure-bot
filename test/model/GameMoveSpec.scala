package model

import org.scalatestplus.play.PlaySpec

class GameMoveSpec extends PlaySpec {
  "GameMove randomMove" should {
    "return a valid random move" in {
      GameMove.values contains GameMove.randomMove
    }
  }
}
