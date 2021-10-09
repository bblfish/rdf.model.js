
addSbtPlugin("org.scala-js" % "sbt-scalajs" % "1.7.1")

/**
 * allows one to access JS npm distributions with the ease with which one can work with maven
 * @see https://search.maven.org/search?q=a:sbt-scalajs-bundler
 * @see https://scalacenter.github.io/scalajs-bundler/getting-started.html
 */
addSbtPlugin("ch.epfl.scala" % "sbt-scalajs-bundler" % "0.21.0-RC1")
