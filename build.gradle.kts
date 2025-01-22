plugins {
  kotlin("jvm") version "2.1.0"
  application
}

group = "clido"

version = "1.0-SNAPSHOT"

repositories { mavenCentral() }

dependencies { 
  implementation("com.github.ajalt.clikt:clikt:5.0.2")
  testImplementation(kotlin("test")) 
}

tasks.test { useJUnitPlatform() }

application { mainClass.set("clido.MainKt") }
