parent?.let {
    group = it.group
    group = it.version
}

dependencies {
    implementation(project(":user"))
}