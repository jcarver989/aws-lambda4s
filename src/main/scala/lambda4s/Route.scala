package lambda4s

abstract class Route(httpMethod: String) {
  def unapplySeq(request: Request): Option[List[String]] = {
    request.httpMethod match {
      case `httpMethod` => Some(splitPath(request))
      case _ => None
    }
  }

  private def splitPath(request: Request): List[String] = {
    request.path.split("/").filterNot { _.isEmpty }.toList
  }
}

object Route {
  private val pathPrefix = "/api/v1"
  private val idRegex = "([A-Za-z0-9\\-\\+]+)"

  def isId(id: String): Boolean = {
    id.matches(idRegex)
  }

}

object Get extends Route("GET")
object Post extends Route("POST")
object Put extends Route("PUT")
object Delete extends Route("DELETE")
object Options extends Route("OPTIONS")