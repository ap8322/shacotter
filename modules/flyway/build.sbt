name := "flyway"

libraryDependencies += "org.flywaydb" % "flyway-core" % "4.0"

flywayLocations += "classpath:db/migration"

flywayUrl := "jdbc:mysql://localhost:3306/shacotter?useUnicode:=true&chracterEncoding:=utf8mb4"
flywayUser := "root"
flywayPassword := ""
