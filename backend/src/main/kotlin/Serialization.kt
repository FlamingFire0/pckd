package me.flamingo.pckd.backend

import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule

val commonSerializersModule = SerializersModule {
    
}

val json = Json {
    prettyPrint = true
    ignoreUnknownKeys = true
    encodeDefaults = true
    serializersModule = commonSerializersModule
}