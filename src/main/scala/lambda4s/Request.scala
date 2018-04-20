package lambda4s

case class Request(
  resource:              String                      = "",
  path:                  String                      = "",
  httpMethod:            String                      = "",
  headers:               Map[String, String]         = Map(),
  queryStringParameters: Option[Map[String, String]] = None,
  pathParameters:        Option[Map[String, String]] = None,
  stageVariables:        Option[Map[String, String]] = None,
  requestContext:        RequestContext              = RequestContext(),
  body:                  String                      = "",
  isBase64Encoded:       Boolean                     = false) {
  def as[T <: AnyRef: Manifest](implicit json: JSON): T = {
    json.fromJSON[T](body)
  }
}

case class RequestContext(
  accountId:    String                 = "",
  resourceId:   String                 = "",
  stage:        String                 = "",
  requestId:    String                 = "",
  identity:     Identity               = Identity(),
  resourcePath: String                 = "",
  httpMethod:   String                 = "",
  apiId:        String                 = "",
  authorizer:   Option[AuthorizerInfo] = None)

case class AuthorizerInfo(claims: Map[String, String])

case class Identity(
  cognitoIdentityPoolId:         String = "",
  accountId:                     String = "",
  cognitoIdentityId:             String = "",
  caller:                        String = "",
  apiKey:                        String = "",
  sourceIp:                      String = "",
  cognitoAuthenticationType:     String = "",
  cognitoAuthenticationProvider: String = "",
  userArn:                       String = "",
  userAgent:                     String = "",
  user:                          String = "")