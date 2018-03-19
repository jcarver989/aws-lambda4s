package lambda4s

import org.scalatest._
case class Item(name: String, price: Double)

class RequestTest extends FlatSpec with Matchers {

  it should "should parse JSON" in {
    implicit val json = JSON
    val itemJSON = JSON.toJSON(Item("Foo", 25.0))
    Request(body = itemJSON).as[Item] should be(Item("Foo", 25.0))
  }
}