package controllers

import play.api.db._
import play.api.Play.current
import models.PaperClip
import models.JsonFormats._

import play.api._
import play.api.mvc._
import play.api.libs.json._

object PaperClipController extends Controller {

  def insert = Action(parse.json) { implicit req =>
    req.body.validate[PaperClip].map {
      paperClip: PaperClip =>
        DB.withConnection { conn =>
          conn.prepareStatement("INSERT INTO PAPERCLIP (tid,text,width,height,posX,posY,posZ) VALUES (" + paperClip.tid + ",'" + paperClip.text + "'," + paperClip.width + "," + paperClip.height + "," + paperClip.posX + "," + paperClip.posY + "," + paperClip.posZ + ")").execute()
        }
        Ok("Hello " + paperClip.height + ", you're " + paperClip.height)
    }.recoverTotal {
      e => BadRequest("Detected error:" + JsError.toFlatJson(e))
    }
  }

  def selectAll = Action {
    DB.withConnection { conn =>
      val smth = conn.prepareStatement("select * from PaperClip").executeQuery()
      var list = List[PaperClip]()

      while (smth.next()) {
        list ::= PaperClip(
          smth.getInt("tid"),
          smth.getString("text"),
          smth.getInt("width"),
          smth.getInt("width"),
          smth.getInt("posX"),
          smth.getInt("posY"),
          smth.getInt("posZ")
        )
      }

      Ok(Json.toJson(list))
    }
  }
}
