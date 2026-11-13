package me.flamingo.pckd.frontend.utilities

import java.nio.file.Path

fun Path.minify(): String {
    val shortened = shorten()
    
    val absolute = toAbsolutePath().normalize()
    val separator = absolute.fileSystem.separator

    val parts = shortened.split(separator)

    return parts.mapIndexed { index, part ->
        if(index == parts.lastIndex) return@mapIndexed part
        part.abbreviate()
    }.joinToString(separator)
}

fun Path.shorten(): String {
    val absolute = toAbsolutePath().normalize()
    val home = Path.of(System.getProperty("user.home")).toAbsolutePath().normalize()
    val homePrefix = "~"
    
    return absolute.toString().replaceFirst(home.toString(), homePrefix)
}

private fun String.abbreviate(): String {
    val chars = if (startsWith(".") && length > 1) 2 else 1
    return substring(0, offsetByCodePoints(0, minOf(chars, codePointCount(0, length))))
}