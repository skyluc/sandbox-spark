lazy val root = (project in file(".")).
  settings(
    name := "test-app-spark",
    version := "0.1",
    scalaVersion := "2.10.5",
    resolvers += "Artifactory Realm" at "http://repo.typesafe.com/typesafe/spark-test",
    libraryDependencies += "org.apache.spark" %% "spark-core" % "1.3.0-typesafe-001"
  )
