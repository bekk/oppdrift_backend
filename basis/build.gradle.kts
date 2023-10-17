plugins {
    kotlin("jvm") version "1.9.0"
    application
}

group = "no.bekk"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("MainKt")
}

kotlin {
    jvmToolchain(8)
}
