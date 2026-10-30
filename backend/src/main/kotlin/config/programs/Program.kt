package me.flamingo.pckd.backend.config.programs

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import me.flamingo.pckd.backend.RootDirectory
import me.flamingo.pckd.backend.resources.Resource
import java.awt.Desktop
import java.nio.file.Path
import kotlin.io.path.name

@Serializable
open class Program(
    @Contextual val file: Path,
    val arguments: List<String> = listOf("{path}")
) {
    override fun toString(): String = file.toString()
    
    open fun run(path: Path, rootDirectory: RootDirectory) {
        val pathString = path.toString()
        val parent     = path.parent?.toString() ?: ""
        val name       = path.name
        val root       = rootDirectory.path.toString()


        val args = arguments.map { it
            .replace(PATH_PLACEHOLDER  , pathString)
            .replace(PARENT_PLACEHOLDER, parent    )
            .replace(NAME_PLACEHOLDER  , name      )
            .replace(ROOT_PLACEHOLDER  , root      )
        }

        ProcessBuilder(file.toString(), *args.toTypedArray()).start()
    }
    
    
    fun run(resource: Resource<*, *>) = run(resource.getFile().path, resource.rootDirectory)
    
    companion object {
        const val PATH_PLACEHOLDER   = "{path}"
        const val PARENT_PLACEHOLDER = "{parent}"
        const val NAME_PLACEHOLDER   = "{name}"
        const val ROOT_PLACEHOLDER   = "{root}"
    }
    
    object DEFAULT : Program(Path.of(""), emptyList()) {
        override fun run(path: Path, rootDirectory: RootDirectory) {
            Desktop.getDesktop().open(path.toFile())
        }
    }
}