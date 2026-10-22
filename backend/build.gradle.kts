plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.serialization)
    `java-library`
}

val qualifier = "me.flamingo"
val namespace = "$qualifier.${rootProject.name}.${project.name}"
group = namespace

dependencies {
    api(project(":common"))
    
    api(libs.bundles.backend)
    testImplementation(libs.bundles.testing)
}

kotlin.jvmToolchain(libs.versions.java.get().toInt())

tasks.test {
    useJUnitPlatform()

    testLogging {
        events("passed", "skipped", "failed", "standardOut", "standardError")
        showStandardStreams = true
    }
}
