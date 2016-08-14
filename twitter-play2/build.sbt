name := """twitter-play2"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  cache,
  ws,
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test,
  "com.typesafe.play" %% "play-slick" % "2.0.0",
  "com.typesafe.play" %% "play-slick-evolutions" % "2.0.0",
  "com.typesafe.slick" %% "slick-codegen" % "3.1.0-M2",
  "jp.t2v" %% "play2-auth"        % "0.14.2",
  "jp.t2v" %% "play2-auth-social" % "0.14.2",
  "jp.t2v" %% "play2-auth-test"   % "0.14.2" % "test",
  "org.flywaydb" %% "flyway-play" % "3.0.1",
  play.sbt.Play.autoImport.cache,
  "org.mindrot" % "jbcrypt" % "0.3m",
  "mysql" % "mysql-connector-java" % "5.1.34"
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

//slick <<= slickCodeGenTask
//
//sourceGenerators in Compile <+= slickCodeGenTask
//
//lazy val slick = TaskKey[Seq[File]]("gen-tables")
//lazy val slickCodeGenTask = (sourceManaged, dependencyClasspath in Compile, runner in Compile, streams) map { (dir, cp, r, s) =>
//  val outputDir = "app"
//  val username = "root"
//  val password = "Hy19920423"
//  val url = "jdbc:mysql://localhost/test"
//  val jdbcDriver = "com.mysql.jdbc.Driver"
//  val slickDriver = "slick.driver.MySQLDriver"
//  val pkg = "models"
//  toError(r.run("slick.codegen.SourceCodeGenerator", cp.files, Array(slickDriver, jdbcDriver, url, outputDir, pkg, username, password), s.log))
//  val fname = outputDir + "/models/Tables.scala"
//  Seq(file(fname))
//}