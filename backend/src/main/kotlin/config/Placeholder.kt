package me.flamingo.pckd.backend.config

import me.flamingo.pckd.backend.RootDirectory
import java.nio.file.Path
import kotlin.io.path.name

enum class Placeholder(
    val placeholder: String,
    val replacement: (path: Path, root: RootDirectory) -> String
    
) {
    Path  ("{path}"  , { path, _ -> path.toString()        } ),
    Parent("{parent}", { path, _ -> path.parent.toString() } ),
    Name  ("{name}"  , { path, _ -> path.name              } ),
    Root  ("{root}"  , { _, root -> root.path.toString()   } );
    
    fun replace(text: String, path: Path, root: RootDirectory): String = text.replace(placeholder, replacement(path, root))
    
    companion object {
        fun applyAll(text: String, path: Path, root: RootDirectory): String = 
            entries.fold(text) { text, placeholder -> placeholder.replace(text, path, root) }
    }
}