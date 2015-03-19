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

  case class paperClipChangeText(id: Int, text: String)

  val paperClipChangeTextForm = Form(mapping(
    "id" -> number,
    "text" -> text
  )(paperClipChangeText.apply)(paperClipChangeText.unapply))

  case class paperClipResize(id:Int, width: Int, height: Int)
  val paperClipResizeForm = Form(mapping(
    "id" -> number,
    "width" -> number,
    "height" -> number
  )(paperClipResize.apply)(paperClipResize.unapply))

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

  //Debug use only
  def selectAll = Action {
    Ok(Json.toJson(selectPinsFromDB("SELECT * FROM PaperClips")))
  }
  
  def selectPinsForBoard(id:Int) = Action { implicit  req =>
      Ok(Json.toJson(selectPinsFromDB("select * from PaperClips WHERE tid="+id)))
  }

  def delete(id:Int) = Action { implicit req =>
      DB.withConnection{ conn =>
        conn.prepareStatement("DELETE FROM PaperClips where id=" + id).execute()
      }
      Ok("DELETE FROM PinBoards where id=" + id)
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
        Ok("Ok")
      }
    } getOrElse BadRequest("Dane nie są poprawne")
  }

  def resize = Action { implicit req =>
    paperClipResizeForm.bindFromRequest.value.map{ form =>
      DB.withConnection{ conn =>
        conn.prepareStatement("UPDATE PaperClips SET width=" +
          form.width +
          ", height=" +
          form.height +
          " WHERE id=" +
          form.id).execute()
      }
    }
    Ok("It's probably working")
  }

  def selectPinsFromDB = { query : String =>
    var list = List[PaperClip]()
    DB.withConnection { conn =>
      val resultSet = conn.prepareStatement(query).executeQuery()

      while (resultSet.next()) {
        list ::= PaperClip(
          Some[Int](resultSet.getInt("id")),
          resultSet.getInt("tid"),
          resultSet.getString("text"),
          resultSet.getInt("width"),
          resultSet.getInt("width"),
          resultSet.getInt("posX"),
          resultSet.getInt("posY"),
          resultSet.getInt("posZ")
        )
      }

    }
    list
  }
}