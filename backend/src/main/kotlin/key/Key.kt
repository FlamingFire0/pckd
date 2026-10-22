package me.flamingo.pckd.backend.key

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import me.flamingo.pckd.backend.RootDirectory
import me.flamingo.pckd.backend.resources.ResourceInfo
import java.nio.file.Path
import kotlin.io.path.div

@Serializable(with = Key.Companion::class)
data class Key(
    val namespace: String,
    val value: String,
) {
    companion object : KSerializer<Key> {
        const val DELIMITER = ":"
        const val DEFAULT_NAMESPACE = "minecraft"
        
        override val descriptor: SerialDescriptor = 
            SerialDescriptor(Key::class.qualifiedName!!, String.serializer().descriptor)
        
        override fun serialize(encoder: Encoder, value: Key) = 
            encoder.encodeString(value.toString())

        override fun deserialize(decoder: Decoder): Key =
            Key(decoder.decodeString())
        
        fun isValid(string: String): Boolean {
            if (string.isBlank()) return false
            if (!string.contains(DELIMITER)) return true
            if (string.substringBefore(DELIMITER, "").trim().isBlank()) return false
            if (string.substringAfter(DELIMITER, "").trim().isBlank()) return false
            return true
        }
    }

    constructor(string: String): this(
        string.substringBefore(DELIMITER, DEFAULT_NAMESPACE),
        string.substringAfter(DELIMITER),
    )
    
    fun toPath(root: RootDirectory, resource: ResourceInfo<*, *>, addExtension: Boolean = true): Path =
        root.assets / namespace / resource.directory / value.run {
            if(addExtension) (this + ".${resource.extension}") else this
        }
    
    override fun toString(): String = toString(false)
    fun toString(short: Boolean) = if(short) value.substringAfterLast("/") else "$namespace:$value"
}