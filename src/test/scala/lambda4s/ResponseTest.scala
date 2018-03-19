package lambda4s

import org.scalatest._

class ResponseTest extends FlatSpec with Matchers {
  it should "should parse JSON" in {
    implicit val json = JSON
    Response(Item("Foo", 25.0)).body should be("""{"name":"Foo","price":25.0}""")
  }
}