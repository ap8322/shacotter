package shacotter.sbt

import com.typesafe.config.ConfigFactory
import collection.JavaConverters._
import java.nio.file.Paths

import slick.codegen.SourceCodeGenerator

object Base {
  def slickCodeGenerator(basePath: String): Unit = {
    println("Generate Slick Code Task")

    ConfigFactory
      .parseFileAnySyntax(
        Paths.get(s"$basePath/conf", "application.conf").toFile
      )
      .getConfig("slick.dbs")
      .root
      .entrySet
      .asScala.foreach { c =>

      val config = c.getValue.atPath(c.getKey).getConfig(c.getKey)

      SourceCodeGenerator.main(
        Array(
          config.getString("driver").stripSuffix("$"),
          config.getString("db.driver"),
          config.getString("db.url"),
          basePath + "/" + config.getString("outDir"),
          config.getString("outPackage"),
          config.getString("db.user"),
          config.getString("db.password")
        )
      )

      println(s"Generated slick code done.")
    }
  }
}

