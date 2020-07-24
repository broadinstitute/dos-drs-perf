scalaVersion := "2.12.10"

enablePlugins(GatlingPlugin)

libraryDependencies ++= List(
  "io.gatling" % "gatling-test-framework" % "3.3.1",
  "io.gatling.highcharts" % "gatling-charts-highcharts" % "3.3.1",
  "com.google.auth" % "google-auth-library-oauth2-http" % "0.21.1",
  "ch.qos.logback" % "logback-classic" % "1.2.3",
).map(_ % Test)

val baseSettings = List(
  "-unchecked",
  "-deprecation",
  "-feature",
  "-explaintypes",
  "-target:jvm-1.8",
  "-encoding",
  "UTF-8",
)

val warningSettings = List(
  "-Xfuture",
  "-Xlint:adapted-args",
  "-Xlint:by-name-right-associative",
  "-Xlint:constant",
  "-Xlint:delayedinit-select",
  "-Xlint:doc-detached",
  "-Xlint:inaccessible",
  "-Xlint:infer-any",
  "-Xlint:missing-interpolator",
  "-Xlint:nullary-override",
  "-Xlint:nullary-unit",
  "-Xlint:option-implicit",
  "-Xlint:package-object-classes",
  "-Xlint:poly-implicit-overload",
  "-Xlint:private-shadow",
  "-Xlint:stars-align",
  "-Xlint:type-parameter-shadow",
  "-Xlint:unsound-match",
  "-Yno-adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard",
  "-Ywarn-inaccessible",
  "-Ywarn-unused:implicits",
  "-Ywarn-unused:privates",
  "-Ywarn-unused:locals",
  "-Ypartial-unification",
  "-Ywarn-unused:patvars",
)

val consoleHostileSettings = List(
  "-Ywarn-unused:imports", // warns about every unused import on every command.
  "-Xfatal-warnings", // makes those warnings fatal.
)

scalacOptions ++= baseSettings ++ warningSettings ++ consoleHostileSettings
scalacOptions in (Compile, doc) ++= baseSettings
// No console-hostile options, otherwise the console is effectively unusable.
// https://github.com/sbt/sbt/issues/1815
scalacOptions in (Compile, console) --= consoleHostileSettings
