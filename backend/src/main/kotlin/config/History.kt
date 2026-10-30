package me.flamingo.pckd.backend.config

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import me.flamingo.pckd.common.Directories
import java.nio.file.Path

@Serializable
data class History(
    val history: MutableList<@Contextual Path> = mutableListOf(),
) {
    object File : ConfigFile<History>(
        Directories.localData / "history.json",
        History::serializer,
        ::History
    )
}