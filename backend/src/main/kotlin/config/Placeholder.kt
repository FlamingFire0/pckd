package me.flamingo.pckd.backend.config

import me.flamingo.pckd.backend.RootDirectory
import me.flamingo.pckd.backend.key.Key
import java.nio.file.Path
import kotlin.io.path.name

enum class Placeholder(
    val placeholder: String,
    val replacement: (parameters: PlaceholderParameters) -> Any?
    
) {
    Path  ("{path}"  , { it.path         } ),
    Parent("{parent}", { it.path?.parent } ),
    Name  ("{name}"  , { it.path?.name   } ),
    Root  ("{root}"  , { it.root?.path   } ),
    Key   ("{key}"   , { it.key          } ),
    
    ;
    
    fun replace(text: String, parameters: PlaceholderParameters): String {
        val replacement = replacement(parameters)?.toString() ?: return text
        return text.replace(placeholder, replacement)
    }
    
    companion object {
        fun applyAll(text: String, parameters: PlaceholderParameters): String = 
            entries.fold(text) { text, placeholder -> placeholder.replace(text, parameters) }
        
        fun applyAll(text: String, builder: (PlaceholderParameters) -> Unit): String = 
            applyAll(text, PlaceholderParameters().apply(builder))
    }
    
    class PlaceholderParameters(
        var path: Path?          = null,
        var root: RootDirectory? = null,
        var key:  Key?           = null,
    )
}