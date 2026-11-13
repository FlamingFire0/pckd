import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.desktop)
    alias(libs.plugins.hot.reload)
    alias(libs.plugins.serialization)
}

val qualifier = "me.flamingo"
val appName   = rootProject.name
val namespace = "$qualifier.$appName.${project.name}"
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
            mainClass = "$namespace.App"

            jvmArgs(
                "--enable-native-access=ALL-UNNAMED"
            )

            nativeDistributions {
                targetFormats(TargetFormat.AppImage, TargetFormat.Exe)
                packageName = appName
                packageVersion = version.toString()

                //fileAssociation()

                modules(
                    "java.instrument", 
                    "jdk.security.auth", 
                    "jdk.unsupported"
                ) // ./gradlew suggestRuntimeModules

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

    register("assembleAppImage") {
        dependsOn("packageAppImage")

        group = "distribution"
        description = "Packages the application as an AppImage."

        val distributionName = appName
        val appArch = "x86_64"

        val jpackageImage = layout.buildDirectory.dir(
            "compose/binaries/main/app/$distributionName"
        )

        val appDir = layout.buildDirectory.dir(
            "appimage/$distributionName.AppDir"
        )

        val appImage = layout.buildDirectory.file(
            "distributions/$distributionName-$version-$appArch.AppImage"
        )

        val linuxIcon = layout.projectDirectory.file(
            "src/main/composeResources/drawable/pckd.svg"
        )

        val appImageTool = providers.gradleProperty("appimagetool")
            .orElse("appimagetool")

        inputs.dir(jpackageImage)
        inputs.file(linuxIcon)

        outputs.dir(appDir)
        outputs.file(appImage)

        doLast {
            fun runTool(
                executable: String,
                arguments: List<String>,
                optional: Boolean = false,
                environment: Map<String, String> = emptyMap()
            ) {
                val process = try {
                    ProcessBuilder(listOf(executable) + arguments)
                        .apply { environment().putAll(environment) }
                        .inheritIO()
                        .start()
                } catch (exception: java.io.IOException) {
                    if (optional) {
                        println("$executable not found; skipping.")
                        return
                    }

                    throw GradleException(
                        "Failed to start $executable. Is it installed?",
                        exception
                    )
                }

                val exitCode = process.waitFor()

                check(exitCode == 0) {
                    "$executable failed with exit code: $exitCode"
                }
            }

            val source = jpackageImage.get().asFile
            val destination = appDir.get().asFile

            check(source.isDirectory) {
                "jpackage application image doesn't exist: $source"
            }

            destination.deleteRecursively()
            source.copyRecursively(destination, overwrite = true)

            source.walkTopDown()
                .filter { it.isFile && it.canExecute() }
                .forEach { original ->
                    val copied = destination.resolve(original.relativeTo(source).path)

                    check(copied.setExecutable(true)) {
                        "Failed to preserve executable permission: $copied"
                    }
                }

            destination.resolve("AppRun").apply {
                writeText(
                    $$"""
                #!/bin/sh
                HERE="$(dirname "$(readlink -f "$0")")"
                exec "$HERE/bin/$$distributionName" "$@"
            """.trimIndent()
                )
                setExecutable(true)
            }

            destination.resolve("$distributionName.desktop").writeText("""
                [Desktop Entry]
                Type=Application
                Name=$distributionName
                Exec=AppRun
                Icon=$distributionName
                Terminal=false
                Categories=Utility;
            """.trimIndent())

            linuxIcon.asFile.copyTo(
                destination.resolve("$distributionName.svg"),
                overwrite = true
            )

            runTool(
                "rsvg-convert",
                listOf(
                    "-w", "256",
                    "-h", "256",
                    "-o", destination.resolve(".DirIcon").absolutePath,
                    linuxIcon.asFile.absolutePath
                ),
                optional = true
            )

            appImage.get().asFile.parentFile.mkdirs()

            runTool(
                appImageTool.get(),
                listOf(
                    destination.absolutePath,
                    appImage.get().asFile.absolutePath
                ),
                environment = mapOf("ARCH" to appArch)
            )

            val output = appImage.get().asFile

            check(output.setExecutable(true)) {
                "Failed to make AppImage executable: $output"
            }
        }
    }
}
