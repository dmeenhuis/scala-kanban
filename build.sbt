name := "scala-kanban"

version := "1.0"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

/*unmanagedResourceDirectories in Compile <++= baseDirectory { base =>
  Seq( base / "src/main/webapp" )
}*/
unmanagedResourceDirectories in Compile += baseDirectory.value / "src/main/webapp"

libraryDependencies ++= {
  val akkaV = "2.3.6"
  val sprayV = "1.3.2"
  Seq(
    "io.spray"            %%  "spray-can"     % sprayV,
    "io.spray"            %%  "spray-routing" % sprayV,
    "io.spray"            %%  "spray-testkit" % sprayV  % "test",
    "com.typesafe.akka"   %%  "akka-actor"    % akkaV,
    "com.typesafe.akka"   %%  "akka-testkit"  % akkaV   % "test",
    "org.specs2"          %%  "specs2-core"   % "2.3.7" % "test",
    "com.h2database"      %   "h2"            % "1.2.137",
    "org.squeryl"         %   "squeryl_2.10"  % "0.9.5-6"
  )
}

lazy val root = (project in file("."))
                  .settings(H2TaskManager.startH2Task, H2TaskManager.stopH2Task)
                  .enablePlugins(SbtTwirl)

Revolver.settings

