plugins {
    kotlin("jvm") version "1.6.21"
}

group = "me.myeolchi"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    mavenLocal()

}

dependencies {
    compileOnly("io.github.monun:tap-api:4.8.0")
    compileOnly("io.papermc.paper:paper-api:1.19.3-R0.1-SNAPSHOT")
    compileOnly("io.github.monun:heartbeat-coroutines:0.0.4")
    compileOnly("io.github.monun:kommand-api:2.14.0")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

tasks {
    jar {
        archiveFileName.set("obe.jar")
    }
}