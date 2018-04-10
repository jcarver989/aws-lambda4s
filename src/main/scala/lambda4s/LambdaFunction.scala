package lambda4s

import java.io.{ InputStream, OutputStream, OutputStreamWriter }
import java.nio.charset.StandardCharsets

import org.apache.commons.io.IOUtils
import org.apache.logging.log4j.scala.Logging

import com.amazonaws.services.lambda.runtime.{ Context, RequestStreamHandler }

/**
 * An AWS Lambda function.
 *
 *  All your Scala lambdas extend this class, e.g.:
 *
 *  class MyLambda extends LambdaFunction[InputType, OutputType] {
 *     override def handle(input: InputType): OutputType = { ??? }
 *  }
 *
 */
abstract class LambdaFunction[T <: AnyRef: Manifest, U <: AnyRef] extends RequestStreamHandler with Logging {
  protected def jsonSerializer: JSON = JSON

  override final def handleRequest(input: InputStream, output: OutputStream, context: Context): Unit = {
    val json = IOUtils.toString(input, StandardCharsets.UTF_8)
    logger.info(s"Raw Request Recieved: ${json}")
    input.close()
    val request = jsonSerializer.fromJSON[T](json)
    val response = jsonSerializer.toJSON(handle(request, context))
    val writer = new OutputStreamWriter(output, StandardCharsets.UTF_8)
    writer.write(response)
    writer.close()
  }

  def handle(input: T, context: Context): U
}

/**
 * An AWS Lambda Function that integrates with API Gateway via Proxy integration.
 *
 */
abstract class LambdaProxyFunction extends LambdaFunction[Request, Response]