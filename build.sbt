// Project name
name := "ScalaFXPro"

// Current version
version := "1.0.0-SNAPSHOT"

// Version of scala to use
scalaVersion := "2.9.2"

// Set the main Scala source directory to be <base>/src
scalaSource in Compile <<= baseDirectory(_ / "src")

resourceDirectory <<= baseDirectory(_ / "src")

// Set the Scala test directory to be <base>/test/src
scalaSource in Test <<= baseDirectory(_ / "test/src")

// append -deprecation to the options passed to the Scala compiler
scalacOptions += "-deprecation"

// ScalaFX is assumed to be in local repo (build locally)
resolvers += "Local Maven Repository" at "file:///"+Path.userHome.absolutePath+"/.m2/repository"

libraryDependencies += "org.scalafx" % "scalafx" % "1.0-SNAPSHOT"

// Test dependencies
libraryDependencies += "junit" % "junit" % "4.+" % "test"

libraryDependencies += "org.scalatest" %% "scalatest" % "1.7.+" % "test"

// Add JavaFX 2 to the unmanaged classpath
// For Java 7 update 06+ the JFXRT JAR is part of the Java Runtime Environment
unmanagedJars in Compile += Attributed.blank(file(System.getenv("JAVA_HOME") + "/jre/lib/jfxrt.jar"))


// Fork a new JVM for 'run' and 'test:run'
fork := false

// Fork a new JVM for 'test:run', but not 'run'
fork in Test := false

// Only use a single thread for building
parallelExecution := false

// Execute tests in the current project serially
parallelExecution in Test := false


