parent?.let {
    group = it.group
    group = it.version
}

dependencies{
    implementation(springStarter("aop"))
}