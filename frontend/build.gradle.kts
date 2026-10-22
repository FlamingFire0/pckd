
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.desktop)
    alias(libs.plugins.hot.reload)
}

val qualifier = "me.flamingo"
val namespace = "$qualifier.${rootProject.name}.${project.name}"
group = namespace
version = "1.0.0"

dependencies {
    implementation(project(":common"))
    implementation(project(":backend"))
    
    implementation(compose.desktop.currentOs)

    implementation(libs.bundles.frontend)
    testImplementation(libs.bundles.testing)
}

compose {
    desktop {
        application {
            mainClass = "$namespace.MainKt"

            jvmArgs(
                "--enable-native-access=ALL-UNNAMED"
            )

            nativeDistributions {
                targetFormats(TargetFormat.AppImage, TargetFormat.Exe)
                packageName = rootProject.name
                packageVersion = version.toString()

                modules("jdk.security.auth")
            }
        }
    }

    resources {
        packageOfResClass = "$namespace.resources"
    }
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
