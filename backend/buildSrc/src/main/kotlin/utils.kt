import org.gradle.api.Project
import org.gradle.kotlin.dsl.ScriptHandlerScope
import org.gradle.kotlin.dsl.maven
import org.gradle.kotlin.dsl.repositories

fun Project.configRepository() {
    repositories {
        mavenLocal()
        maven("https://maven.aliyun.com/nexus/content/groups/public/")
        mavenCentral()
    }
}

fun ScriptHandlerScope.configRepository() {
    repositories {
        mavenLocal()
        maven("https://maven.aliyun.com/nexus/content/groups/public/")
        mavenCentral()
    }
}

fun springStarter(id: String, version: String? = null): String {
    return "org.springframework.boot:spring-boot-starter-$id" + if (version != null) ":$version" else ""
}