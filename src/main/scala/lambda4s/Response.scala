package lambda4s

case class Response(
  statusCode:      Int                 = 200,
  headers:         Map[String, String] = Map("Content-Type" -> "application/json"),
  body:            String              = "",
  isBase64Encoded: Boolean             = false)

object Response {
  val internalServerError = Response(500, body = "Internal Server Error")

  def apply[T <: AnyRef](item: T)(implicit json: JSON): Response = {
    Response(200, body = json.toJSON(item))
  }
}