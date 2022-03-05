parent?.let {
    group = it.group
    group = it.version
}

dependencies{
    implementation(project(":common"))
    implementation(project(":domain"))
    implementation(springStarter("data-redis"))
}
