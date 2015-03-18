package models

object JsonFormats {

  import play.api.libs.json.Json

  implicit val paperClipFormat = Json.format[PaperClip]
}

case class PaperClip
(
  tid: Int,
  text: String,
  width: Int,
  height: Int,
  posX: Int,
  posY: Int,
  posZ: Int
  )

case class PinBoard
(
  Id: Int,
  Name: String
  )