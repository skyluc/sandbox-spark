lazy val root = (project in file(".")).
  settings(
    name := "test-app-spark",
    version := "0.1",
    scalaVersion := "2.11.4",
    resolvers += "bintray-typesafe-maven-releases" at "http://dl.bintray.com/typesafe/maven-releases",
    libraryDependencies += "org.apache.spark" %% "spark-core" % "1.3.1-typesafe-001"
  )
