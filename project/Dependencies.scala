import sbt._

object Dependencies {
  val scalaTestV               = "3.2.16"
  val scalaTestPlusScalaCheckV = "3.2.16.0"
  val scalaTestPlusMockitoV    = "3.2.16.0"
  val mockitoV                 = "4.11.0"
  val catsEffectV              = "3.5.0"
  val fs2V                     = "3.7.0"
  val testcontainersScalaV     = "0.40.16"
  val doobieV                  = "1.0.0-RC1"
  val postgreSqlV              = "42.5.1"
  val flywayV                  = "9.19.4"
  val circeV                   = "0.14.5"
  val circeGeneriExtrasV       = "0.14.3"

  val Testing = Seq(
    "org.scalactic"     %% "scalactic"                       % scalaTestV,
    "org.scalatest"     %% "scalatest"                       % scalaTestV               % Test,
    "org.scalatestplus" %% "scalacheck-1-17"                 % scalaTestPlusScalaCheckV % Test,
    "com.dimafeng"      %% "testcontainers-scala-scalatest"  % testcontainersScalaV     % Test,
    "com.dimafeng"      %% "testcontainers-scala-postgresql" % testcontainersScalaV     % Test,
    "org.tpolecat"      %% "doobie-scalatest"                % doobieV                  % Test
  )

  val Mockito = Seq(
    "org.mockito"        % "mockito-core" % mockitoV              % Test,
    "org.scalatestplus" %% "mockito-4-11" % scalaTestPlusMockitoV % Test
  )

  val Cats = Seq(
    "org.typelevel" %% "cats-effect" % catsEffectV
  )

  val FS2 = Seq(
    "co.fs2" %% "fs2-core" % fs2V
  )

  val Postgres = Seq(
    "org.postgresql" % "postgresql"      % postgreSqlV,
    "org.tpolecat"  %% "doobie-postgres" % doobieV,
    "org.flywaydb"   % "flyway-core"     % flywayV
  )

  val Circe = Seq("io.circe" %% "circe-generic-extras" % circeGeneriExtrasV) ++ Seq(
    "io.circe" %% "circe-core",
    "io.circe" %% "circe-generic",
    "io.circe" %% "circe-parser"
  ).map(_ % circeV)

  val projectDependencies = Testing ++ Cats ++ FS2 ++ Postgres ++ Circe

}
