package me.flamingo.pckd.frontend.common

import java.awt.Desktop
import java.nio.file.Files
import java.nio.file.Path

fun Path.openInFileManager(): Boolean {
    if (!Files.isDirectory(this)) return false
    val os = System.getProperty("os.name").lowercase()

    // Try AWT first
    runCatching {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
            Desktop.getDesktop().open(toFile())
            return true
        }
    }

    val cmd = when {
        "win" in os -> arrayOf("explorer.exe", toString())
        "mac" in os -> arrayOf("open", toString())
        else -> arrayOf("xdg-open", toString())
    }
    return runCatching { ProcessBuilder(*cmd).start(); true }.getOrDefault(false)
}