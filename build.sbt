import Dependencies.*
import JSEnv.{Chrome, Firefox, NodeJS}
import org.scalajs.linker.interface.ModuleKind.ESModule
import org.scalajs.linker.interface.OutputPatterns
import sbt.Keys.description
import sbtcrossproject.CrossPlugin.autoImport.{CrossType, crossProject}
import org.scalajs.jsenv.nodejs.NodeJSEnv
import sbt.ThisBuild
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.{ChromeDriver, ChromeOptions}
import org.openqa.selenium.firefox.{FirefoxOptions, FirefoxProfile}
import org.openqa.selenium.remote.server.{DriverFactory, DriverProvider}
import org.scalajs.jsenv.selenium.SeleniumJSEnv

name := "rdf.model.js"

ThisBuild / tlBaseVersion          := "0.2"
ThisBuild / tlUntaggedAreSnapshots := true

ThisBuild / organization     := "net.bblfish.rdf"
ThisBuild / organizationName := "Henry Story"
ThisBuild / startYear        := Some(2021)
ThisBuild / developers := List(
  tlGitHubDev("bblfish", "Henry Story")
)

enablePlugins(TypelevelCiReleasePlugin)
enablePlugins(TypelevelSonatypePlugin)

ThisBuild / tlCiReleaseBranches := Seq()
ThisBuild / tlCiReleaseTags     := false // don't publish artifacts on github

ThisBuild / crossScalaVersions         := Seq("3.1.1")
ThisBuild / githubWorkflowJavaVersions := Seq(JavaSpec.temurin("17"))

tlReplaceCommandAlias("ciJS", List(CI.NodeJS, CI.Firefox, CI.Chrome).mkString)
addCommandAlias("ciNodeJS", CI.NodeJS.toString)
addCommandAlias("ciFirefox", CI.Firefox.toString)
addCommandAlias("ciChrome", CI.Chrome.toString)

addCommandAlias("prePR", "; root/clean; scalafmtSbt; +root/scalafmtAll; +root/headerCreate")

lazy val useJSEnv =
  settingKey[JSEnv]("Use Node.js or a headless browser for running Scala.js tests")

Global / useJSEnv := NodeJS

ThisBuild / Test / jsEnv := {
  val old = (Test / jsEnv).value

  useJSEnv.value match {
    case NodeJS => old
    case Firefox =>
      val options = new FirefoxOptions()
      options.setHeadless(true)
      new SeleniumJSEnv(options)
    case Chrome =>
      val options = new ChromeOptions()
      options.setHeadless(true)
      new SeleniumJSEnv(options)
  }
}

//ThisBuild / githubWorkflowBuildPreamble ++= Seq(
//  WorkflowStep.Use(
//    UseRef.Public("actions", "setup-node", "v2.4.0"),
//    name = Some("Setup NodeJS v14 LTS"),
//    params = Map("node-version" -> "14"),
//    cond = Some("matrix.ci == 'ciJS'")
//  )
//)
ThisBuild / homepage := Some(url("https://github.com/bblfish/rdf.scala.js"))
ThisBuild / scmInfo := Some(
  ScmInfo(url("https://github.com/bblfish/rdf.scala.js"), "git@github.com:bblfish/rdf.scala.js.git")
)

tlReplaceCommandAlias("ciJS", List(CI.Chrome, CI.Firefox).mkString)
addCommandAlias("ciFirefox", CI.Firefox.toString)

ThisBuild / scalaVersion := Ver.scala3

ThisBuild / organization := "net.bblfish.rdf"
headerLicenseStyle       := HeaderLicenseStyle.SpdxSyntax

lazy val commonSettings = Seq(
  description := "rdf models for JS from https://rdf.js.org",
  startYear   := Some(2021),
  updateOptions := updateOptions.value.withCachedResolution(
    true
  ) // to speed up dependency resolution
)

lazy val rdfModelJS = project.in(file("."))
  .enablePlugins(ScalaJSPlugin)
  // documentation here: https://scalablytyped.org/docs/library-developer
  // call stImport in sbt to generate new sources
  .settings(commonSettings*)
  .settings(
    scalacOptions ++= scala3jsOptions,
    scalaJSUseMainModuleInitializer := true,
    // https://github.com/com-lihaoyi/utest
    libraryDependencies += "com.lihaoyi" %%% "utest" % "0.7.10" % "test",
    testFrameworks += new TestFramework("utest.runner.Framework"),
    // see module doc https://www.scala-js.org/doc/project/module.html
    scalaJSLinkerConfig ~= {
      _.withModuleKind(ModuleKind.ESModule)
        //				 nodjs needs .mjs extension. See https://www.scala-js.org/doc/project/module.html
        .withOutputPatterns(OutputPatterns.fromJSFile("%s.mjs"))
    }
    // scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.CommonJSModule) }
  )
val scala3jsOptions = Seq(
  "-indent", // Together with -rewrite, remove {...} syntax when possible due to significant indentation.
  "-new-syntax", // Require `then` and `do` in control expressions.
  "-source:future", // Choices: future and future-migration. I use this to force future deprecation warnings, etc.
  "-Yexplicit-nulls" // For explicit nulls behavior.
)
