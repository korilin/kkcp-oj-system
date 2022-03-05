parent?.let {
    group = it.group
    group = it.version
}

dependencies {
    implementation(project(":common"))
    implementation(project(":user"))
    implementation(project(":admin"))
    implementation(project(":domain"))
}