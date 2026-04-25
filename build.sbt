ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.7"

val javafxVersion = "21.0.7"

lazy val root = (project in file("."))
  .settings(
    name := "sc-project",
    libraryDependencies ++= Seq(
      "org.openjfx" % "javafx-controls" % javafxVersion classifier "mac-aarch64",
      "org.openjfx" % "javafx-fxml" % javafxVersion classifier "mac-aarch64"
    )
  )
