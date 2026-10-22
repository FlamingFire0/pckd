package me.flamingo.pckd.backend.config

import kotlinx.serialization.Serializable
import me.flamingo.pckd.backend.key.Key

@Serializable
class Groups(
    val groups: MutableSet<Key> = mutableSetOf(),
)