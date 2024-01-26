plugins {
  id("java")
  id("eu.cloudnetservice.juppiter") version "0.4.0"
}

group = "dev.playo"
version = "1.0-SNAPSHOT"

tasks.withType<JavaCompile> {
  sourceCompatibility = JavaVersion.VERSION_17.toString()
  targetCompatibility = JavaVersion.VERSION_17.toString()
}

repositories {
  mavenCentral()
  maven("https://repository.derklaro.dev/releases")
}

dependencies {
  annotationProcessor("org.projectlombok:lombok:1.18.30")

  moduleLibrary("org.projectlombok:lombok:1.18.30")

  implementation("eu.cloudnetservice.cloudnet:node:4.0.0-RC9")
}

moduleJson {
  name = "CloudNet-Log-Report"
  author = "0utplay"
  main = "dev.playo.cloudnet.report.CloudNetLogReportModule"
}

tasks.test {
  useJUnitPlatform()
}
