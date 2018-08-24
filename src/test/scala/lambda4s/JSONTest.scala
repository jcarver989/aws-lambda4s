package lambda4s

import org.json4s.NoTypeHints
import org.json4s.native.Serialization
import org.scalatest.{ FlatSpec, Matchers }

case class User(firstName: String, lastName: String, phoneNumber: Option[String])

sealed trait FooType
case object Foo extends FooType
case object Boo extends FooType
object FooType extends CaseObject[FooType] {
  override def fromJSON(item: String): FooType = {
    item match {
      case "foo" => Foo
      case "boo" => Boo
      case _     => throw new RuntimeException("BOOM!")
    }
  }
}

case class Thing(foo: FooType)

object MyJSON extends JSON {
  override implicit val formats = {
    Serialization.formats(NoTypeHints) +
      new CaseObjectSerializer(FooType)
  }
}

class JSONTest extends FlatSpec with Matchers {
  val user = User(
    firstName = "John",
    lastName = "Smith",
    phoneNumber = Some("123"))

  val userJSON = """{"firstName":"John","lastName":"Smith","phoneNumber":"123"}"""

  it should "serialize a case class to JSON" in {
    MyJSON.toJSON(Thing(Foo)) should be("""{"foo":"foo"}""")
    MyJSON.toJSON(Thing(Boo)) should be("""{"foo":"boo"}""")
  }

  it should "deserialize a case class from JSON" in {
    MyJSON.fromJSON[Thing]("""{"foo":"foo"}""") should be(Thing(Foo))
    MyJSON.fromJSON[Thing]("""{"foo":"boo"}""") should be(Thing(Boo))

    MyJSON.fromJSON[Thing]("""{"foo":"Foo"}""") should be(Thing(Foo))
    MyJSON.fromJSON[Thing]("""{"foo":"Boo"}""") should be(Thing(Boo))
  }

  it should "serialize a User to JSON" in {
    MyJSON.toJSON(user) should be(userJSON)
  }

  it should "deserialize a User from JSON" in {
    MyJSON.fromJSON[User](userJSON) should be(user)
  }

  it should "be able to deserialize what it serializes" in {
    MyJSON.fromJSON[User](MyJSON.toJSON(user)) should be(user)
  }

  it should "parse json as a Map" in {
    case class Stuff(a: String, b: String)
    MyJSON.fromJSON[Map[String, _]]("""{ "a": "foo", "b": "bar" }""") should be(Map("a" -> "foo", "b" -> "bar"))
  }

  it should "serialize fields with value set to None" in {
    val person = User("John", "Smith", None)

    val json = """{"firstName":"John","lastName":"Smith"}"""
    MyJSON.toJSON(person) should be(json)
    MyJSON.fromJSON[User](json) should be(person)
  }

  // from a real Lambda request, with PII scrubbed
  it should "serialize lambda response" in {
    val json = """{
       "resource":"/api/v1/{proxy+}",
       "path":"/foo/bar/baz",
       "httpMethod":"GET",
       "headers":{
         "Accept":"application/json",
         "Accept-Encoding":"br, gzip, deflate",
         "Accept-Language":"en-us",
         "CloudFront-Forwarded-Proto":"https",
         "CloudFront-Is-Desktop-Viewer":"true",
         "CloudFront-Is-Mobile-Viewer":"false",
         "CloudFront-Is-SmartTV-Viewer":"false",
         "CloudFront-Is-Tablet-Viewer":"false",
         "CloudFront-Viewer-Country":"US",
         "content-type":"application/json",
         "Host":"api.qwillapp.com",
         "User-Agent":"User Agent 5.0",
         "Via":"2.0 abdcd.cloudfront.net (CloudFront)",
         "X-Amz-Cf-Id":"ID",
         "X-Amzn-Trace-Id":"Root-DD",
         "X-Forwarded-For":"123.456, 78.123",
         "X-Forwarded-Port":"443","X-Forwarded-Proto":"https"
         },
         "queryStringParameters":null,
         "pathParameters":{"proxy":"foo/bar/baz"},
         "stageVariables":null,
         "requestContext":{
         "resourceId":"imAnId",
         "resourcePath":"/api/v1/{proxy+}",
         "httpMethod":"GET",
         "extendedRequestId":"abcd",
         "requestTime":"01/Jan/2018:00:00:00 +0000",
         "path":"/api/foo/bar",
         "accountId":"1234",
         "protocol":"HTTP/1.1",
         "stage":"dev",
         "requestTimeEpoch":1000,
         "requestId":"abcd",
         "identity":{
         "cognitoIdentityPoolId":null,
         "accountId":null,
         "cognitoIdentityId":null,
         "caller":null,
         "sourceIp":"123.456",
         "accessKey":null,
         "cognitoAuthenticationType":null,
         "cognitoAuthenticationProvider":null,
         "userArn":null,
         "userAgent":"Foo/1 CFNetwork/1 Darwin/2.1.0",
         "user":null
         },
         "apiId":"123"
         },
         "body":null,
         "isBase64Encoded":false
         }"""

    noException should be thrownBy { MyJSON.fromJSON[Request](json) }
  }

}