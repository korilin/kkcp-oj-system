parent?.let {
    group = it.group
    group = it.version
}

val ktorVersion = "1.6.7"

dependencies{
    implementation(project(":common"))
    implementation(springStarter("data-redis"))
}