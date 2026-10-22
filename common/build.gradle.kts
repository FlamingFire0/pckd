plugins {
    alias(libs.plugins.kotlin)
    `java-library`
}

val qualifier = "me.flamingo"
val namespace = "$qualifier.${rootProject.name}.${project.name}"
group = namespace
version = "1.0.0"

dependencies {
    api(libs.bundles.common)
    testImplementation(libs.bundles.testing)
}

kotlin {
    jvmToolchain(libs.versions.java.get().toInt())
}

tasks {
    withType<JavaExec>().configureEach {
        jvmArgs("--enable-native-access=ALL-UNNAMED")
    }
    
    test {
        useJUnitPlatform()

        testLogging {
            events("passed", "skipped", "failed", "standardOut", "standardError")
            showStandardStreams = true
        }
    }
}