
# Write AWS Lambda Functions In Scala, Easily.

This repository contains everything you need to create AWS Lambda functions with Scala. It includes: 

- Automagically convert Lambda's JSON input and output to/from Scala case classes
- Logging via AWs Lambda's log4j appender.
- Support for Lambdas that interface with API Gateway via Proxy integration
- Dead simple route matching for your Lambda APIs

## Getting Started

Just add these two lines to your `build.sbt`: 

```scala
resolvers += Resolver.bintrayRepo("jcarver989", "maven")
libraryDependencies += "com.jcarver989" %% "aws-lambda4s" % "latest.integration"
```

## Usage 

### Lambdas That Don't Integrate With API Gateway

```scala
import com.amazonaws.services.lambda.runtime.Context
import lambda4s._

case class InputItem(sku: String)
case class OutputItem(name: String, price: Double)

class MyLambda extends LambdaFunction[InputItem, OutputItem] {
    private val dbConnection = ??? // whatever you want
    override def handle(input: InputItem, context: Context): OutputItem = {
        val product = dbconnection.findById(input.sku)
        logger.info(s"Found Item ${input.sku}: ${product}") // logger is inherited from base class
        OutputItem(product.name, product.price)
    }
}
```

###  Lambdas That Integrate With API Gateway Via Proxy Integration

```scala
import com.amazonaws.services.lambda.runtime.Context
import lambda4s._

class MyAPI extends LambdaProxyFunction {
    private val dbConnection  = ??? // whatever you want
    override def handle(request: Request, context: Context): Response = {
        request match {
            case Get("users", userId) if Route.isId(userId) =>
              val user = dbConnection.findById(userId) // User is a case class, e.g. case class User(id: String, ...)
              Response(body = JSON.toJSON(user)) // automagically converts a User to JSON
            
            case Post("users") =>
              val user = JSON.fromJSON[User](request.body) // automagically converts the request's body from JSON => User
              dbConnection.create(user)
              Response(statusCode = 200, body = """{"status": "success"}""")
        }
    }
}

```

## Cute Toy Examples - But How Do I Use This In Production?

To deploy to AWS you'll need to create a "fat jar". If using SBT, you can do this easily using the [sbt-assembly](https://github.com/sbt/sbt-assembly) plugin.

For a complete deployable example, checkout [aws-lambda4s-example](https://github.com/jcarver989/aws-lambda4s-example) repo. It comes with a complete CloudFormation template, meaning you can run it locally and deploy it to AWS with just a single command using the offical AWS command line tools. You should be able to just clone the repository and have everything you need to start writing Lambda functions.
