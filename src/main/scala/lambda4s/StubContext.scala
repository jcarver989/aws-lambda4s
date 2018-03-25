package lambda4s

import com.amazonaws.services.lambda.runtime.{ ClientContext, CognitoIdentity, Context, LambdaLogger }

/** A very basic stub of a Lambda Context */
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
