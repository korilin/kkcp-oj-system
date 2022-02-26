parent?.let {
    group = it.group
    group = it.version
}
val ktorVersion = "1.6.7"

dependencies{
    implementation(project(":common"))
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-serialization:$ktorVersion")
    implementation("io.ktor:ktor-client-jackson:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
    implementation(springStarter("data-redis"))
}