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

class StubContext extends Context {
  override def getAwsRequestId(): String = ???
  override def getLogGroupName(): String = ???
  override def getLogStreamName(): String = ???
  override def getFunctionName(): String = ???
  override def getFunctionVersion(): String = ???
  override def getInvokedFunctionArn(): String = ???
  override def getIdentity(): CognitoIdentity = ???
  override def getClientContext(): ClientContext = ???
  override def getRemainingTimeInMillis(): Int = ???
  override def getMemoryLimitInMB(): Int = ???
  override def getLogger(): LambdaLogger = ???
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