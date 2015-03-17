package controllers

import play.api.db._
import play.api.Play.current
import models.PaperClip
import models.JsonFormats._

import play.api._
import play.api.mvc._
import play.api.libs.json.Json

object dataBaseController extends Controller{
  def insert = Action{
    val thing = PaperClip("Thing",1,1,1,1,1)
    DB.withConnection { conn =>
      val smth = conn.prepareStatement("select * from PaperClip").executeQuery()
      var list = List()

      while(smth.next()){
        list ::= PaperClip(
          smth.getString(2),
          smth.getInt(3),
          smth.getInt(4),
          smth.getInt(5),
          smth.getInt(6),
          smth.getInt(7)
        )
      }

      Ok(Json.toJson(list) + " Response " + list(0))
    }
    /*Ok(Json.toJson(
      Json.obj("data" -> Json.arr(
        Json.toJson(thing),
        Json.toJson(thing)
      )
      )
    )
    + " Thing ")*/
  }
}
