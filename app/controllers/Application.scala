package controllers

import models.PaperClip
import play.api._
import play.api.mvc._

import models.JsonFormats._
import play.api.libs.json.{JsArray, Json, JsObject}
import play.api.libs.json.Json.JsValueWrapper
import play.twirl.api.Html

object Application extends Controller {

  def index = Action {
    Ok(views.html.choose())
  }

  def showTable(tName: String) = Action { req =>

    if (tName != "")
      Ok(views.html.pinBoard(tName, Html(Json.toJson(PaperClipController.selectPinsFromDBAsJson(tName)).toString()), req.host))
    else
      Ok("Zaraz ci tom strone zrobie")
  }

}