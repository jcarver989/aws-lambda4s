scalaVersion := "2.12.4"
name := "aws-lambda4s"
version := "1.0.4"
organization := "com.jcarver989"

licenses += ("Apache-2.0", url("https://www.apache.org/licenses/LICENSE-2.0"))
 
// Java
libraryDependencies += "com.amazonaws" % "aws-lambda-java-core" % "latest.integration"
libraryDependencies += "com.amazonaws" % "aws-lambda-java-events" % "latest.integration"
libraryDependencies += "com.amazonaws" % "aws-lambda-java-log4j2" % "latest.integration"

libraryDependencies += "commons-io" % "commons-io" % "latest.integration"
libraryDependencies += "org.apache.logging.log4j" % "log4j-core" % "2.8.2"
libraryDependencies += "org.apache.logging.log4j" % "log4j-api" % "2.8.2"

// Scala
libraryDependencies += "org.json4s" %% "json4s-native" % "latest.integration"
libraryDependencies += "org.apache.logging.log4j" %% "log4j-api-scala" % "latest.integration"

// Test
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test"
libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.5" % "test"
