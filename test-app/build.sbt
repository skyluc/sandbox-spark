lazy val root = (project in file(".")).
  settings(
    name := "test-app-spark",
    version := "0.1",
    scalaVersion := "2.11.7",
    resolvers += "bintray-typesafe-maven-releases" at "http://dl.bintray.com/typesafe/maven-releases",
    resolvers += "spark rc" at "https://repository.apache.org/content/repositories/orgapachespark-1152",
    libraryDependencies += "org.apache.spark" %% "spark-core" % "1.5.2" % "provided"
  )
