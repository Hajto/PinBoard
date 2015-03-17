package controllers

import models.PaperClip
import play.api._
import play.api.mvc._

import models.JsonFormats._
import play.api.libs.json.{JsArray, Json, JsObject}
import play.api.libs.json.Json.JsValueWrapper

object Application extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }



}