package me.flamingo.pckd.frontend.utilities

import java.nio.file.Path

fun Path.minify(): String {
    val abs = toAbsolutePath().normalize()
    val home = Path.of(System.getProperty("user.home")).toAbsolutePath().normalize()
    val sep = abs.fileSystem.separator

    // Inside home: "~" + the parts below it. Otherwise: the root + all parts.
    val inHome = abs.startsWith(home)
    val prefix = if (inHome) "~" else abs.root?.toString().orEmpty()
    val parts = (if (inHome) home.relativize(abs) else abs)
        .map { it.toString() }
        .filter { it.isNotEmpty() }   // relativize(home, home) yields one empty name

    if (parts.isEmpty()) return prefix

    val short = parts.mapIndexed { i, name ->
        if (i == parts.lastIndex) name else name.abbreviate()
    }.joinToString(sep)

    return if (inHome) "~$sep$short" else prefix + short
}

// First character, keeping the dot for hidden dirs (".config" -> ".c"), safe for emoji/surrogates.
private fun String.abbreviate(): String {
    val chars = if (startsWith(".") && length > 1) 2 else 1
    return substring(0, offsetByCodePoints(0, minOf(chars, codePointCount(0, length))))
}