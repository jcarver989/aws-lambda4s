package lambda4s

import org.scalatest._

case class User(firstName: String, lastName: String, phoneNumber: Option[String])

class JSONTest extends FlatSpec with Matchers {
  val user = User(
    firstName = "John",
    lastName = "Smith",
    phoneNumber = Some("123"))

  val userJSON = """{"firstName":"John","lastName":"Smith","phoneNumber":"123"}"""

  it should "serialize a User to JSON" in {
    JSON.toJSON(user) should be(userJSON)
  }

  it should "deserialize a User from JSON" in {
    JSON.fromJSON[User](userJSON) should be(user)
  }

  it should "be able to deserialize what it serializes" in {
    JSON.fromJSON[User](JSON.toJSON(user)) should be(user)
  }

  it should "parse json as a Map" in {
    case class Stuff(a: String, b: String)
    JSON.fromJSON[Map[String, _]]("""{ "a": "foo", "b": "bar" }""") should be(Map("a" -> "foo", "b" -> "bar"))
  }

  it should "serialize fields with value set to None" in {
    val person = User("John", "Smith", None)

    val json = """{"firstName":"John","lastName":"Smith"}"""
    JSON.toJSON(person) should be(json)
    JSON.fromJSON[User](json) should be(person)
  }

}