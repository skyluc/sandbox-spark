lazy val root = (project in file(".")).
  settings(
    name := "test-streaming-spark",
    version := "0.1",
    scalaVersion := "2.11.4",
    resolvers += "bintray-typesafe-maven-releases" at "http://dl.bintray.com/typesafe/maven-releases",
    libraryDependencies ++= Seq(
      "org.apache.spark" %% "spark-core" % "1.3.1-typesafe-001",
      "org.apache.spark" %% "spark-streaming" % "1.3.1-typesafe-001"
    )
  )
