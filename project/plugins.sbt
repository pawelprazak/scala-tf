addSbtPlugin("com.thesamet" % "sbt-protoc" % "1.0.3")
addSbtPlugin("ch.epfl.lamp" % "sbt-dotty" % "0.5.3")

libraryDependencies ++= Seq(
  "com.thesamet.scalapb" %% "compilerplugin" % "0.11.1"
)
