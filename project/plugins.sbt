//selenium testing
libraryDependencies += "org.scala-js" %% "scalajs-env-selenium" % "1.1.1"

//for sbt-typelevel when using SNAPSHOTS
resolvers += Resolver.sonatypeRepo("snapshots")

addSbtPlugin("org.scala-js" % "sbt-scalajs" % "1.7.1")

/** allows one to access JS npm distributions with the ease with which one can work with maven
  * @see
  *   https://search.maven.org/search?q=a:sbt-scalajs-bundler
  * @see
  *   https://scalacenter.github.io/scalajs-bundler/getting-started.html
  * @see
  *   https://github.com/scalacenter/scalajs-bundler
  * @see
  *   https://scalacenter.github.io/scalajs-bundler/
  */
addSbtPlugin("ch.epfl.scala" % "sbt-scalajs-bundler" % "0.21.0-RC1")

// https://typelevel.org/sbt-typelevel/index.html
addSbtPlugin("org.typelevel" % "sbt-typelevel" % "0.4.3")
addSbtPlugin("org.scala-js"  % "sbt-scalajs"   % "1.8.0")
addSbtPlugin("com.eed3si9n"  % "sbt-buildinfo" % "0.10.0")
