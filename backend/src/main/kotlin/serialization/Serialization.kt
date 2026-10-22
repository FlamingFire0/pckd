package me.flamingo.pckd.backend.serialization

import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import java.nio.file.Path

val commonSerializersModule = SerializersModule {
    contextual(Path::class, PathSerializer)
}

val json = Json {
    prettyPrint = true
    ignoreUnknownKeys = true
    encodeDefaults = true
    serializersModule = commonSerializersModule
}