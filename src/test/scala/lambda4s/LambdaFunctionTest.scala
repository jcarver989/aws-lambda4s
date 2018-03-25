package lambda4s

import java.io.{ ByteArrayInputStream, ByteArrayOutputStream }
import java.nio.charset.StandardCharsets

import org.scalatest.{ FlatSpec, Matchers }

import com.amazonaws.services.lambda.runtime.{ ClientContext, CognitoIdentity, Context, LambdaLogger }

/** A simple lambda that inherits from Lambda Proxy Function, so we can test the abstract class  */
class DoublePriceLambda extends LambdaFunction[Item, Item] {
  private implicit val json = JSON
  override def handle(item: Item, context: Context): Item = {
    item.copy(price = item.price * 2)
  }
}


class LambdaFunctionTest extends FlatSpec with Matchers {
  it should "handle JSON input / output correctly" in {
    val json = """{"name": "Foo", "price": 25.0}"""
    val in = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8))
    val out = new ByteArrayOutputStream()
    new DoublePriceLambda().handleRequest(in, out, new StubContext())
    val output = new String(out.toByteArray)
    output should be("""{"name":"Foo","price":50.0}""")
  }
}