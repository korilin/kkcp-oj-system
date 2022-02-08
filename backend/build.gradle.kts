plugins {
    kotlin("jvm") version "1.6.10"
}

group = "cn.shenzhen-kug"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}