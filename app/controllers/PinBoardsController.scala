package controllers

import play.api.db._
import play.api.Play.current
import models.PinBoard
import models.JsonFormats._

import play.api._
import play.api.mvc._
import play.api.libs.json._

object PinBoardsController extends Controller {

  def addPinBoards = Action(parse.json){ implicit req =>
    req.body.validate[PinBoard].map{ pinBoard: PinBoard =>
      DB.withConnection{ conn =>
        conn.prepareStatement("INSERT INTO PinBoards (Name) VALUES ('"+pinBoard.name+"')")
      }
    }
  }
}