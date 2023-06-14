import sbt._

object Dependencies {
  val scalaTestV               = "3.2.16"
  val scalaTestPlusScalaCheckV = "3.2.16.0"
  val scalaTestPlusMockitoV    = "3.2.16.0"
  val mockitoV                 = "4.11.0"
  val catsEffectV              = "3.5.0"
  val fs2V                     = "3.7.0"

  val Testing = Seq(
    "org.scalactic"     %% "scalactic"       % scalaTestV,
    "org.scalatest"     %% "scalatest"       % scalaTestV               % Test,
    "org.scalatestplus" %% "scalacheck-1-17" % scalaTestPlusScalaCheckV % Test
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

  val projectDependencies = Testing ++ Cats ++ FS2

}
