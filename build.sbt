import Dependencies.Ver
import org.scalajs.linker.interface.OutputPatterns
import sbt.Keys.description
import sbt.url

scalaVersion := Ver.scala3

ThisBuild / homepage := Some(url("https://github.com/bblfish/rdf.scala.js"))
ThisBuild / licenses += ("Apache 2.0", url("https://opensource.org/licenses/Apache-2.0"))
ThisBuild / organization := "net.bblfish.rdf"
ThisBuild / shellPrompt := ((s: State) => Project.extract(s).currentRef.project + "> ")
ThisBuild / versionScheme := Some("early-semver")

lazy val commonSettings = Seq(
	name := "rdf-model-js",
	version := "0.1-SNAPSHOT",
	description := "rdf.js.org specs libs for scalajs",
	startYear := Some(2021),
	scalaVersion := Ver.scala3,
	updateOptions := updateOptions.value.withCachedResolution(true) //to speed up dependency resolution
)
lazy val publicationSettings = Seq(
	sonatypeProfileName := "net.bblfish.rdf",
	publishTo := sonatypePublishToBundle.value,

	// To sync with Maven central, you need to supply the following information:
	publishMavenStyle := true,

	// Open-source license of your choice
	licenses += ("Apache 2.0", url("https://opensource.org/licenses/Apache-2.0")),
	homepage := Some(url("https://github.com/banana-rdf/")),
	scmInfo := Some(
		ScmInfo(
			url("https://github.com/bblfish/rdf.scala.js"),
			"git@github.com:bblfish/rdf.scala.js.git"
		)
	),
	developers := List(
		Developer(id = "bblfish", name = "Henry Story", email = "henry.story@bblfish.net", url = url("https://bblfish.net/")),
	)
)
lazy val rdfModelJS = project.in(file("."))
	.enablePlugins(ScalaJSPlugin)
	//documentation here: https://scalablytyped.org/docs/library-developer
	// call stImport in sbt to generate new sources
	.settings(commonSettings: _*)
	.settings(publicationSettings: _*)
	.settings(
		scalacOptions ++= scala3jsOptions,
		scalaJSUseMainModuleInitializer := true,
		// https://github.com/com-lihaoyi/utest
		libraryDependencies += "com.lihaoyi" %%% "utest" % "0.7.10" % "test",
		testFrameworks += new TestFramework("utest.runner.Framework"),
		//see module doc https://www.scala-js.org/doc/project/module.html
		scalaJSLinkerConfig ~= {
			_.withModuleKind(ModuleKind.ESModule)
				//				 nodjs needs .mjs extension. See https://www.scala-js.org/doc/project/module.html
				.withOutputPatterns(OutputPatterns.fromJSFile("%s.mjs"))
		}
		// scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.CommonJSModule) }
	)
val scala3jsOptions = Seq(
	// "-classpath", "foo:bar:...",         // Add to the classpath.
	//"-encoding", "utf-8",                // Specify character encoding used by source files.
	"-deprecation", // Emit warning and location for usages of deprecated APIs.
	"-unchecked", // Enable additional warnings where generated code depends on assumptions.
	"-feature", // Emit warning and location for usages of features that should be imported explicitly.
	//"-explain",                          // Explain errors in more detail.
	//"-explain-types",                    // Explain type errors in more detail.
	"-indent", // Together with -rewrite, remove {...} syntax when possible due to significant indentation.
	// "-no-indent",                        // Require classical {...} syntax, indentation is not significant.
	"-new-syntax", // Require `then` and `do` in control expressions.
	// "-old-syntax",                       // Require `(...)` around conditions.
	// "-language:Scala2",                  // Compile Scala 2 code, highlight what needs updating
	//"-language:strictEquality",          // Require +derives Eql+ for using == or != comparisons
	// "-rewrite",                          // Attempt to fix code automatically. Use with -indent and ...-migration.
	// "-scalajs",                          // Compile in Scala.js mode (requires scalajs-library.jar on the classpath).
	"-source:future", // Choices: future and future-migration. I use this to force future deprecation warnings, etc.
	// "-Xfatal-warnings",                  // Fail on warnings, not just errors
	// "-Xmigration",                       // Warn about constructs whose behavior may have changed since version.
	// "-Ysafe-init",                       // Warn on field access before initialization
	"-Yexplicit-nulls" // For explicit nulls behavior.
)
