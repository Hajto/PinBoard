package controllers

import models.PaperClip
import play.api._
import play.api.mvc._

import models.JsonFormats._
import play.api.libs.json.{JsArray, Json, JsObject}
import play.api.libs.json.Json.JsValueWrapper

object Application extends Controller {

  def index = Action {
    Ok("TEST")
  }

  def showTable(tid: Int) = Action { req =>
    Ok(views.html.pinBoard(tid, Json.toJson(PaperClipController.selectPinsFromDBAsJson(tid)).toString(),req.host))
  }

}