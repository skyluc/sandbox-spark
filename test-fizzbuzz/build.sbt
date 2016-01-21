lazy val root = (project in file(".")).
  settings(
    name := "test-fizzbuzz",
    version := "0.1",
    scalaVersion := "2.10.6",
    libraryDependencies += "org.apache.spark" %% "spark-core" % "1.6.0" % "provided",
    libraryDependencies += "org.apache.spark" %% "spark-streaming" % "1.6.0" % "provided"
  )
