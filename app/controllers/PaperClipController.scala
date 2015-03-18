package controllers

import play.api.data.Form
import play.api.data.Forms._
import play.api.db._
import play.api.Play.current
import models.PaperClip
import models.JsonFormats._

import play.api._
import play.api.mvc._
import play.api.libs.json._

object PaperClipController extends Controller {

  case class paperClipMove(id:Int, posX: Int, posY: Int, posZ: Int)
  val paperClipMoveForm = Form(mapping (
    "id" -> number,
    "posX" -> number,
    "posY" -> number,
    "posZ" -> number
  )(paperClipMove.apply)(paperClipMove.unapply))

  case class paperClipDelete(id:Int)
  val paperClipDeleteForm = Form(mapping (
    "id" -> number
  )(paperClipDelete.apply)(paperClipDelete.unapply))

  case class paperClipChengeText(id:Int, text:String)
  val paperClipChangeTextForm = Form(mapping(
    "id" -> number,
    "text" -> text
  )(paperClipChengeText.apply)(paperClipChengeText.unapply))

  def insert = Action(parse.json) { implicit req =>
    req.body.validate[PaperClip].map {
      paperClip: PaperClip =>
        DB.withConnection { conn =>
          conn.prepareStatement("INSERT INTO PaperClips(tid,text,width,height,posX,posY,posZ) VALUES (" + paperClip.tid + ",'" + paperClip.text + "'," + paperClip.width + "," + paperClip.height + "," + paperClip.posX + "," + paperClip.posY + "," + paperClip.posZ + ")").execute()
            Ok(Json.obj(
              "id" -> conn.prepareStatement("SELECT id FROM PaperClips ORDER BY id DESC LIMIT 1").executeQuery().getInt(1)
            ))
        }
    }.recoverTotal {
      e => BadRequest("Detected error:" + JsError.toFlatJson(e))
    }
  }

  def selectAll = Action {
    DB.withConnection { conn =>
      val resultSet = conn.prepareStatement("select * from PaperClip").executeQuery()
      var list = List[PaperClip]()

      while (resultSet.next()) {
        list ::= PaperClip(
          resultSet.getInt("tid"),
          resultSet.getString("text"),
          resultSet.getInt("width"),
          resultSet.getInt("width"),
          resultSet.getInt("posX"),
          resultSet.getInt("posY"),
          resultSet.getInt("posZ")
        )
      }

      Ok(Json.toJson(list))
    }
  }

  def delete = Action { implicit req =>
    val form = paperClipDeleteForm.bindFromRequest()
    form.value.map{ form =>
      DB.withConnection{ conn =>
        conn.prepareStatement("DELETE FROM PinBoards where id=" +
          form.id).execute()
      }
      Ok("Rekordy pomyślnie usinięto")
    } getOrElse BadRequest("Dane nie są poprawne")
  }

  def movePaperClip = Action{ implicit req =>
    val form = paperClipMoveForm.bindFromRequest()
    form.value.map{ form =>
      DB.withConnection{ conn =>
        conn.prepareStatement("UPDATE PinBoards SET posX=" +
          form.posX +
          ", posY=" +
          form.posY +
          ", posZ=" +
          form.posZ +
          " where id=" +
          form.id).execute()
      }
      Ok("Rekordy pomyślnie zmienione")
    } getOrElse BadRequest("Dane nie są poprawne")
  }

  def changeText = Action { implicit req =>
    paperClipChangeTextForm.bindFromRequest().value.map{ form =>
      DB.withConnection{ conn =>
        conn.prepareStatement("UPDATE PaperClips SET text '" +
          form.text +
          "' WHERE id = " +
          form.id +
          "").execute()
      }
    }
    Ok("Ok")
  }
}
