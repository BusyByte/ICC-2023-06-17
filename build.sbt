import Dependencies.{projectDependencies, Mockito}

ThisBuild / scalaVersion     := "2.13.10"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "example"

val `mocks-in-scala` = (project in file("./modules/mocks-in-scala"))
  .settings(libraryDependencies ++= projectDependencies ++ Mockito)
  .disablePlugins(TpolecatPlugin)

val `fakes-in-scala` = (project in file("./modules/fakes-in-scala"))
  .settings(libraryDependencies ++= projectDependencies)

val `type-driven-dev` = (project in file("./modules/type-driven-dev"))
  .settings(libraryDependencies ++= projectDependencies)

val root = (project in file("."))
  .settings(
    name := "ICC-2023-06-17"
  )
  .aggregate(`mocks-in-scala`, `fakes-in-scala`, `type-driven-dev`)
