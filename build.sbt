name := "Twindr"

version := "1.0"

lazy val `twindr` = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.5"

resolvers +=
  "Sonatype OSS Releases" at "https://oss.sonatype.org/content/repositories/releases"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  ws,
  "org.mongodb" %% "casbah" % "2.7.5",
  "com.novus" %% "salat" % "1.9.9",
  "org.scalatest" %% "scalatest" % "2.2.1" % "test"
)

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )
