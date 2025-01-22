plugins {
  kotlin("jvm") version "1.5.20"
  application
}

group = "clido"

version = "1.0-SNAPSHOT"

repositories { mavenCentral() }

dependencies { testImplementation(kotlin("test")) }

tasks.test { useJUnitPlatform() }

application { mainClass.set("clido.MainKt") }
