plugins {
  kotlin("jvm") version "2.1.0"
  application
}

group = "clido"

version = "1.0-SNAPSHOT"

repositories { mavenCentral() }

dependencies { testImplementation(kotlin("test")) }

tasks.test { useJUnitPlatform() }

application { mainClass.set("clido.MainKt") }
