plugins {
    kotlin("jvm") version "2.0.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    application
    idea
}

group = "dev.matytyma"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("dev.kord:kord-core:0.14.0")
    implementation("org.slf4j:slf4j-simple:2.0.13")
    implementation("io.github.cdimascio:dotenv-kotlin:6.4.1")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass = "dev.matytyma.MainKt"
}

kotlin {
    jvmToolchain(17)
}

idea.module {
    isDownloadJavadoc = true
    isDownloadSources = true
}
