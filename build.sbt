import com.typesafe.sbt.SbtNativePackager.autoImport.maintainer
import com.typesafe.sbt.packager.docker.DockerChmodType

name := "ms-skeleton"
scalaVersion := "2.13.10"

lazy val catsVersion               = "2.9.0"
lazy val catsEffectVersion         = "3.5-639ac01"
lazy val scalaTestVersion          = "3.2.14"
lazy val scalaCheckVersion         = "1.17.0"
lazy val scalaMockVersion          = "5.2.0"
lazy val pureConfigVersion         = "0.17.2"
lazy val logbackVersion            = "1.4.5"
lazy val scalaLoggingVersion       = "3.9.5"
lazy val log4CatsVersion           = "2.5.0"
lazy val circeVersion              = "0.14.3"
lazy val circeGenericExtrasVersion = "0.14.3"
lazy val http4sVersion             = "0.23.16"
lazy val http4sBlazeVersion        = "0.23.12"
lazy val tapirVersion              = "1.2.3"
lazy val tapirSwaggerCirceVersion  = "0.20.2"
lazy val quicklensVersion          = "1.9.0"
lazy val fs2Version                = "3.5-1c0be5c"
lazy val chimneyVersion            = "0.6.2"
lazy val sttpVersion               = "3.8.3"
lazy val refinedVersion            = "0.10.1"
lazy val fs2PubSubVersion          = "0.21.0"
lazy val googlePubSubVersion       = "1.114.0"
lazy val zerowasteVersion          = "0.2.1"

lazy val pubSubDependencies = Seq(
  "com.permutive"    %% "fs2-google-pubsub-http" % fs2PubSubVersion,
  "com.google.cloud" % "google-cloud-pubsub"     % googlePubSubVersion
)

lazy val catsDependencies = Seq(
  "org.typelevel" %% "cats-core"          % catsVersion,
  "org.typelevel" %% "cats-effect"        % catsEffectVersion,
  "org.typelevel" %% "cats-effect-kernel" % catsEffectVersion,
  "org.typelevel" %% "cats-effect-std"    % catsEffectVersion,
  "org.typelevel" %% "cats-effect-laws"   % catsEffectVersion % Test
)

lazy val scalaTestDependencies = Seq(
  "org.scalactic" %% "scalactic" % scalaTestVersion % Test,
  "org.scalatest" %% "scalatest" % scalaTestVersion % Test
)

lazy val scalaCheckDependencies = Seq(
  "org.scalacheck" %% "scalacheck" % scalaCheckVersion % Test
)

lazy val scalaMockDependencies = Seq(
  "org.scalamock" %% "scalamock" % scalaMockVersion % Test
)

lazy val pureConfigDependencies = Seq(
  "com.github.pureconfig" %% "pureconfig" % pureConfigVersion
)

lazy val loggerDependencies = Seq(
  "ch.qos.logback"             % "logback-classic" % logbackVersion,
  "ch.qos.logback"             % "logback-core"    % logbackVersion,
  "com.typesafe.scala-logging" %% "scala-logging"  % scalaLoggingVersion,
  "org.typelevel"              %% "log4cats-slf4j" % log4CatsVersion
)

lazy val circeDependencies = Seq(
  "io.circe" %% "circe-core"           % circeVersion,
  "io.circe" %% "circe-generic"        % circeVersion,
  "io.circe" %% "circe-parser"         % circeVersion,
  "io.circe" %% "circe-literal"        % circeVersion,
  "io.circe" %% "circe-refined"        % circeVersion,
  "io.circe" %% "circe-generic-extras" % circeGenericExtrasVersion
)

lazy val http4sDependencies = Seq(
  "org.http4s" %% "http4s-dsl"          % http4sVersion,
  "org.http4s" %% "http4s-circe"        % http4sVersion,
  "org.http4s" %% "http4s-blaze-server" % http4sBlazeVersion,
  "org.http4s" %% "http4s-blaze-client" % http4sBlazeVersion
)

lazy val tapirDependencies = Seq(
  "com.softwaremill.sttp.tapir" %% "tapir-core"               % tapirVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-http4s-server"      % tapirVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-json-circe"         % tapirVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-openapi-docs"       % tapirVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-bundle"  % tapirVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-newtype"            % tapirVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-refined"            % tapirVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-enumeratum"         % tapirVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-openapi-circe-yaml" % tapirSwaggerCirceVersion
)

lazy val fs2Dependencies = Seq(
  "co.fs2" %% "fs2-core" % fs2Version
)

lazy val quicklensDependencies = Seq(
  "com.softwaremill.quicklens" %% "quicklens" % quicklensVersion
)

lazy val chimneyDependencies = Seq("io.scalaland" %% "chimney" % chimneyVersion)

lazy val sttpDependencies =
  Seq(
    "com.softwaremill.sttp.client3" %% "core"           % sttpVersion,
    "com.softwaremill.sttp.client3" %% "circe"          % sttpVersion,
    "com.softwaremill.sttp.client3" %% "http4s-backend" % sttpVersion
  )

lazy val refinedDependencies =
  Seq(
    "eu.timepit" %% "refined"            % refinedVersion,
    "eu.timepit" %% "refined-cats"       % refinedVersion,
    "eu.timepit" %% "refined-pureconfig" % refinedVersion
  )

lazy val zerowasteDependencies =
  Seq(
    compilerPlugin(
      "com.github.ghik" % "zerowaste" % zerowasteVersion cross CrossVersion.full
    )
  )

lazy val root = (project in file("."))
  .enablePlugins(JavaAppPackaging)
  .settings(
    resolvers += Resolver.sonatypeRepo("public"),
    scalacOptions ++= Seq(
      "-feature",
      "-deprecation",
      "-Xfatal-warnings",
      "-Ymacro-annotations",
      "-language:higherKinds",
      "-language:postfixOps",
      "-language:implicitConversions",
      "-Ylog-classpath"
    ),
    Test / scalacOptions --= Seq(
      "-Xfatal-warnings"
    ),
    libraryDependencies ++=
      catsDependencies
        ++ http4sDependencies
        ++ scalaCheckDependencies
        ++ scalaTestDependencies
        ++ scalaMockDependencies
        ++ pureConfigDependencies
        ++ loggerDependencies
        ++ circeDependencies
        ++ tapirDependencies
        ++ fs2Dependencies
        ++ quicklensDependencies
        ++ chimneyDependencies
        ++ sttpDependencies
        ++ refinedDependencies
        ++ pubSubDependencies
        ++ zerowasteDependencies
  )
