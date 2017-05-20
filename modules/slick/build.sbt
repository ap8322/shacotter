libraryDependencies ++= Seq(
  "com.zaxxer" % "HikariCP" % "2.4.1",
  "com.typesafe.slick" %% "slick" % "3.1.1",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.1.1"
)

slickCodegenSettings
slickCodegenDatabaseUrl := "jdbc:mysql://localhost:3310/shacotter?useUnicode:=true&chracterEncoding:=utf8mb4"
slickCodegenDatabaseUser := "root"
slickCodegenDatabasePassword := ""
slickCodegenDriver := slick.driver.MySQLDriver
slickCodegenJdbcDriver := "com.mysql.jdbc.Driver"
slickCodegenOutputPackage := "com.shacotter.user.slick" // 動作確認の為、一旦適当なpackageに出力する。
slickCodegenExcludedTables := Seq("schema_version")

sourceGenerators in Compile <+= slickCodegen
