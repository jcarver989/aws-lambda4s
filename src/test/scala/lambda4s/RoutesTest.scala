package lambda4s

import org.scalatest._
import lambda4s._

class RoutesTest extends FlatSpec with Matchers {
  it should "match a GET route" in {
    val matched = Request(httpMethod = "GET", path = "/api/v1/login") match {
      case Get("api", "v1", "login") => true
      case _                         => false
    }

    matched should be(true)
  }

  it should "match a POST route" in {
    val matched = Request(httpMethod = "POST", path = "/api/v1/login") match {
      case Post("api", "v1", "login") => true
      case _                          => false
    }

    matched should be(true)
  }

  it should "match a PUT route" in {
    val matched = Request(httpMethod = "PUT", path = "/api/v1/login") match {
      case Put("api", "v1", "login") => true
      case _                         => false
    }

    matched should be(true)
  }

  it should "match a DELETE route" in {
    val matched = Request(httpMethod = "DELETE", path = "/api/v1/login") match {
      case Delete("api", "v1", "login") => true
      case _                            => false
    }

    matched should be(true)
  }

  it should "match a regex route" in {
    val userId = "123-456-abcd+"
    val route = Request(httpMethod = "GET", path = s"/api/v1/users/${userId}")
    route match {
      case Get("api", "v1", "users", userId) if Route.isId(userId) => "YAY"
      case _ => throw new RuntimeException("test failed")
    }
  }
}