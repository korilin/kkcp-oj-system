buildscript {
    configRepository()
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.10")
    }
}

group = "com.korilin"
version = "1.0-SNAPSHOT"

plugins {
    id("org.springframework.boot") version "2.6.3"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.6.10"
    kotlin("plugin.spring") version "1.6.10"
}

allprojects {
    configRepository()

    apply {
        plugin("org.springframework.boot")
        plugin("io.spring.dependency-management")
        plugin("org.jetbrains.kotlin.jvm")
        plugin("org.jetbrains.kotlin.plugin.spring")
    }

    sourceSets {
        val main by getting {
            dependencies {
                // kotlin
                implementation(kotlin("stdlib"))
                implementation(kotlin("reflect"))
                implementation(kotlin("compiler"))

                // jackson
                implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
                implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

                // dev
                runtimeOnly("org.springframework.boot:spring-boot-devtools")
            }

        }
        val test by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(springStarter("test"))
            }
        }
    }
}
