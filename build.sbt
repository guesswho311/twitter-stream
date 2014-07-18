name := "twitter"

version := "0.1"

scalaVersion := "2.10.4"

Revolver.settings

resolvers ++= Seq(
  "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases",
  "spray repo" at "http://repo.spray.io"
)

libraryDependencies ++= Seq(
  "org.twitter4j" % "twitter4j-core" % "2.1.5",
  "org.twitter4j" % "twitter4j-stream" % "3.0.5",
  "com.codahale.metrics" % "metrics-core" % "3.0.1",
  "com.github.nscala-time" %% "nscala-time" % "1.2.0",
  "com.typesafe.akka" % "akka-actor_2.10" % "2.3.1"
)
