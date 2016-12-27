enablePlugins(JavaAppPackaging)

name := """ah-advert-api"""
organization := "ah"
version := "1.0"
scalaVersion := "2.11.8"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

libraryDependencies ++= {
  val akkaV = "2.4.14"
  val akkaHttpV = "10.0.0"
  val slickV = "3.1.1"
  val scalaTestV = "3.0.0"
  Seq(
    "com.typesafe.akka"   %% "akka-actor"                         % akkaV,
    "com.typesafe.akka"   %% "akka-stream"                        % akkaV,
    "com.typesafe.akka"   %% "akka-http"                          % akkaHttpV,
    "com.typesafe.akka"   %% "akka-http-spray-json"               % akkaHttpV,
    "com.typesafe.akka"   %% "akka-http-testkit"                  % akkaHttpV % "test",
    "com.typesafe.akka"   %% "akka-slf4j"                         % akkaV,
    "ch.megard"           %% "akka-http-cors"                     % "0.1.5",
    "org.scalactic"       %% "scalactic"                          % scalaTestV % "test",
    "org.scalatest"       %% "scalatest"                          % scalaTestV % "test",
    "com.typesafe.slick"  %% "slick"                              % slickV,
    "com.typesafe.slick"  %% "slick-hikaricp"                     % slickV,
    "ch.qos.logback"      %  "logback-classic"                    % "1.1.7",
    "org.postgresql"      % "postgresql"                          % "9.3-1100-jdbc41",
    "com.softwaremill.akka-http-session" %% "core"                % "0.2.7",
    "com.softwaremill.akka-http-session" %% "jwt"                 % "0.2.7",
    "com.softwaremill.macwire" %% "macros"                        % "2.2.4" % "provided",
    "org.scalamock" %% "scalamock-scalatest-support"              % "3.3.0" % "test"
  )
}

Revolver.settings

parallelExecution in Test := false

flywayUrl := "jdbc:postgresql:advert"

flywayUser := "advert"

assemblyJarName in assembly := "advert.jar"

mainClass in assembly := Some("ah.advert.Main")

assemblyExcludedJars in assembly <<= (fullClasspath in assembly) map { cp =>
  cp filter {x => x.data.getName.matches(".*akka-http-experimental.*") }
}