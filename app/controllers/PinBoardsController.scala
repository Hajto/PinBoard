package controllers


import play.api.data.Form
import play.api.data.Forms._
import play.api.db._
import play.api.Play.current
import models.{PinBoard}
import models.JsonFormats._

import play.api._
import play.api.mvc._
import play.api.libs.json._


object PinBoardsController extends Controller {

  case class PinData(name: String)
  val pinForm = Form(mapping (
    "name" -> text
  )(PinData.apply)(PinData.unapply))

  def addPinBoards() = Action(parse.json){ implicit req =>
    val formData = pinForm.bindFromRequest()
    formData.value.map{ form =>
      DB.withConnection{ conn =>
        conn.prepareStatement("INSERT INTO PinBoards (Name) VALUES ('"+form.name+"')").execute()
      }
      Ok("Ok")
    } getOrElse BadRequest("Coś jest nie tak z danymi które przesłałeś")
  }

  def searchAndListBoards = Action(parse.json){ implicit req =>
    val formData = pinForm.bindFromRequest()
    formData.value.map{ form =>
      DB.withConnection{ conn =>
        val resultSet = conn.prepareStatement("SELECT id FROM PinBoards WHERE NAME LIKE '"+form.name+"%'").executeQuery()

        var list = List[PinBoard]()
        while (resultSet.next()) {
          list ::= PinBoard(
            resultSet.getString(1),
            resultSet.getInt("id")
          )
        }

        Ok(Json.toJson(list))
      }
    } getOrElse BadRequest("Coś jest nie tak z danymi które przesłałeś")
  }

}