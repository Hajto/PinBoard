name := """Mendelson"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  ws,
  "org.xerial" % "sqlite-jdbc" % "3.8.0-SNAPSHOT"
)

resolvers += "SQLite-JDBC Repository" at "https://oss.sonatype.org/content/repositories/snapshots"
