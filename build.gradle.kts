plugins {
  id("java")
  id("eu.cloudnetservice.juppiter") version "0.4.0"
}

group = "dev.playo"
version = "1.0-SNAPSHOT"

tasks.withType<JavaCompile> {
  sourceCompatibility = JavaVersion.VERSION_23.toString()
  targetCompatibility = JavaVersion.VERSION_23.toString()
}

repositories {
  mavenCentral()
  maven("https://repository.derklaro.dev/releases")
  maven("https://s01.oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
  annotationProcessor("org.projectlombok:lombok:1.18.36")

  moduleLibrary("org.projectlombok:lombok:1.18.36")
  implementation("eu.cloudnetservice.cloudnet:node:4.0.0-RC11.2")
}

moduleJson {
  name = "CloudNet-Log-Report"
  author = "0utplay"
  main = "dev.playo.cloudnet.report.CloudNetLogReportModule"
}

tasks.test {
  useJUnitPlatform()
}
