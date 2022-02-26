parent?.let {
    group = it.group
    group = it.version
}

dependencies{
    implementation(project(":common"))
    implementation(springStarter("data-redis"))
}
