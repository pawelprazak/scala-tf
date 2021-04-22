scalaVersion := "3.0.0-RC2"

Compile / PB.targets := Seq(
  scalapb.gen() -> (Compile / sourceManaged).value / "scalapb"
)

// (optional) If you need scalapb/scalapb.proto or anything from
// google/protobuf/*.proto
libraryDependencies ++= Seq(
  "com.thesamet.scalapb" %% "scalapb-runtime" % scalapb.compiler.Version.scalapbVersion % "protobuf"
)

libraryDependencies ++= Seq(
  "com.thesamet.scalapb" %% "scalapb-runtime-grpc" % scalapb.compiler.Version.scalapbVersion,
  "io.grpc" % "grpc-netty" % "1.36.1",
  "io.netty" % "netty-transport-native-kqueue" % "4.1.52.Final" classifier OS.mac,
  "io.netty" % "netty-transport-native-epoll" % "4.1.52.Final" classifier OS.linux
)
