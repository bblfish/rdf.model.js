
addSbtPlugin("org.scala-js" % "sbt-scalajs" % "1.7.1")

/**
 * allows one to access JS npm distributions with the ease with which one can work with maven
 * @see https://search.maven.org/search?q=a:sbt-scalajs-bundler
 * @see https://scalacenter.github.io/scalajs-bundler/getting-started.html
 */
addSbtPlugin("ch.epfl.scala" % "sbt-scalajs-bundler" % "0.21.0-RC1")

/**
* sbtsonatype plugin used to publish artifact to maven central via sonatype nexus
* @see https://github.com/xerial/sbt-sonatype
*/
addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "3.9.10")


/**
 * plugin used to sign the artifcat with pgp keys
 * @see https://github.com/sbt/sbt-pgp
 */
addSbtPlugin("com.github.sbt" % "sbt-pgp" % "2.1.2")

