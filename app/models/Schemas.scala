package models

object JsonFormats {

  import play.api.libs.json.Json

  implicit val paperClipFormat = Json.format[PaperClip]
  implicit val pinBoardFormat = Json.format[PinBoard]
}

case class PaperClip
( id: Int,
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
  name: String,
  id: Int
  )